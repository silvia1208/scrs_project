package it.uniroma2.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller
 */
@Controller
public class CaptchaController {

    /**
     * Metodo per visualizzare la pagina di autenticazione
     * @param capthcha_image
     * @param model
     * @return
     */
    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public String authentication(String capthcha_image, Model model ) {

        //Stringa contenente id della figura
        String capthcaIdentifier = estrazioneNomeFileCaptcha();

        String valueImage = "";
        try {
            InputStream is = getClass().getResourceAsStream("/"+capthcaIdentifier+".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = reader.readLine()) != null) {
                valueImage += str;
            }
        }catch (Exception e){}

        model.addAttribute("XXXVALORI",valueImage);
        model.addAttribute("capthcha_image", capthcaIdentifier);

        return "authentication_page";
    }


    /**
     *
     * @param dominio
     * @param username
     * @param captchaPin
     * @param passwordInserita
     * @param msg
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/verificaCredenzialiAccesso", method = RequestMethod.POST)
    public @ResponseBody ResponseAuthentication verificaCredenzialiAccesso(@RequestParam("Dominio")String dominio, @RequestParam("Username")String username, @RequestParam("captchaPin")String captchaPin, @RequestParam("Password")String passwordInserita, String msg, Model model, HttpServletRequest request ) throws Exception {

        //HttpSession session = request.getSession();
        //Properties properties = new Properties();
        String passwordCodificataNelFile = null;


        dominio = dominio.concat(username);
        dominio = dominio.concat(captchaPin);
        String dominioUsernamePin = dominio;

        byte[] encodedhash = hashing256(dominioUsernamePin);

        String KeyCifratura = codificaStringa(encodedhash);
        System.out.println("La KEYcifratura in fase di autenticazione è: " + KeyCifratura);

        //modifica 26/08
        String indice = Base64.getEncoder().encodeToString(encodedhash);


        //leggo il file per scoprire le credenziali di accesso
        InputStream is = getClass().getResourceAsStream("/credenziali.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        //da gestire questo reader

        //while((reader.readLine())!=null){



        for (String str = reader.readLine(); str != null; str = reader.readLine()) {
            System.out.println("Stringa letta da file: "+str);

            if(str.isEmpty()){continue;}


            String stringaDaConfrontare=str.substring(0,str.indexOf(" ")-1);

            System.out.println("stringaDaConfrontare        :" + stringaDaConfrontare);
            System.out.println("indice        :" + indice);
            System.out.println("KeyCifratura        :" + KeyCifratura);


            //if(stringaDaConfrontare.equals(KeyCifratura)){

            //modifica 26/08
            if(stringaDaConfrontare.equals(indice)){

                //passwordCodificataNelFile = str.substring(33,str.length());

                //modifica 26/08
                passwordCodificataNelFile = str.substring(str.indexOf(" ")+1,str.length());

                byte[] decoded = Base64.getDecoder().decode(passwordCodificataNelFile);
                String password = decrypt(decoded  , KeyCifratura);
                System.out.println("Il valore della password criptata nel file è :" + password);

                //qua dovremmo rimandare ad una pagina che ci fa vedere a video la password

                //inserito nel momento in cui abbiamo inserito la password nella jsp

                if(password.equals(passwordInserita)){
                   // return "redirect:https://www.google.com/";

                    return new ResponseAuthentication(dominio,"correct");
                }else{
                   // model.addAttribute("messaggio", "Password errata");

                    //devo ripassare il numero casuale per nuovo inserimento captcha
                    //creare funzione random
                    Random random = new Random();

                    //generazione di un numero casuale tra 0 e 4999
                    int k = random.nextInt(4999);

                    String number =Integer.toString(k);
                    //generazione di una stringa per il richiamo di un identificativo della gif
                    String mtfa = "mtfa_00";
                    number=mtfa.concat(number);

                    msg = number;
                    model.addAttribute("msg", msg);

                 //   return "authentication_page";

                    return new ResponseAuthentication(dominio,"wrong");
                }


                //msg = password;
                // model.addAttribute("msg", msg);
                // return "visualizzazione_password";
            }


        }

        reader.close();


//
//        //in caso di errore
//        Random random = new Random();
//
//        //generazione di un numero casuale tra 0 e 4999
//        int k = random.nextInt(4999);
//
//        String number =Integer.toString(k);
//        //generazione di una stringa per il richiamo di un identificativo della gif
//        String mtfa = "mtfa_00";
//        number=mtfa.concat(number);
//
//        msg = number;
//    //    model.addAttribute("msg", msg);
//
//    //    model.addAttribute("messaggio", "credenziali di accesso errate");
//    //    return "authentication_page";
       return  new ResponseAuthentication(dominio,"wrong");
    }














    @RequestMapping(value = "/autenticazione", method = RequestMethod.GET)
    public String autenticazione( @RequestParam("Dominio")String dominio, @RequestParam("Username")String username, @RequestParam("captcha") String captcha, Model model, HttpServletRequest request ) throws IOException, URISyntaxException, NoSuchAlgorithmException  {
        System.out.println("Redirecting Result To The Final Page");

        //salva in sessione il dominio e l'username e porta alla pagina del captcha

        HttpSession session = request.getSession();

        session.setAttribute("dominio", dominio);
        session.setAttribute("username", username);




        return "pagina_captcha_autenticazione";

    }





    @RequestMapping(value = "/registrazione", method = RequestMethod.GET)
    public String indirizzaPaginaRegistrazione( Model model ) throws IOException {

        String nomeFileCaptcha = estrazioneNomeFileCaptcha();

        String stringaCaptcha = new String();

        System.out.println("Nome del file"+nomeFileCaptcha);
        System.out.println("Stringa captcha"+ stringaCaptcha);
        InputStream is = getClass().getResourceAsStream("/"+nomeFileCaptcha+".txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        for (String str = reader.readLine(); str != null; str = reader.readLine()) {
            stringaCaptcha = stringaCaptcha + str;
        }

        System.out.println("Stringa captcha"+ stringaCaptcha);

        model.addAttribute("capthcha_image", nomeFileCaptcha);
        model.addAttribute("XXXVALORI", stringaCaptcha);


        System.out.println("Sto indirizzando alla registrazione");
        return "registration_page";
    }





    @RequestMapping(value = "/redirectAuthentication", method = RequestMethod.GET)
    public String redirectAuthentication(String msg, Model model ) throws IOException {
        System.out.println("Sto indirizzando alla registrazione");


        String nomeFileCaptcha = estrazioneNomeFileCaptcha();

        String stringaCaptcha = new String();

        InputStream is = getClass().getResourceAsStream(nomeFileCaptcha);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        for (String str = reader.readLine(); str != null; str = reader.readLine()) {

            stringaCaptcha = stringaCaptcha + str;

        }


        model.addAttribute("nomeFileCaptcha", nomeFileCaptcha);
        model.addAttribute("contenutoFileCaptcha", stringaCaptcha);


        return "authentication_page";
    }


    //funzione
    public String estrazioneNomeFileCaptcha(){

        //creare funzione random
        Random random = new Random();

        //generazione di un numero casuale tra 0 e 4999
        int k = random.nextInt(4999);

        String number =Integer.toString(k);

        //qua devi fare uscire un numero con tanti zeri iniziali quante sono le cifre che mancano
        // a comporre un numero di 4 cifre

        switch(number.length()){

            case 1:
                number = "000"+number;

            case 2:
                number = "00"+number;

            case 3:
                number = "0"+number;

            default:
                System.out.println("il numero generato ha 4 cifre");
        }

        //generazione di una stringa per il richiamo di un identificativo della gif
       // String slash = "/";
        String mtfa = "mtfa_00";
       // String estensione = ".txt";
      //  String nomefileCaptcha = slash +mtfa+ number+estensione;
        String nomefileCaptcha = mtfa + number;
        System.out.println("Il file captcha cercato: " + nomefileCaptcha);

        return nomefileCaptcha;
    }








    @RequestMapping(value = "/creaPasswordCifrata", method = RequestMethod.GET)
    public String creaPasswordCifrata(@RequestParam("dominio")String dominio, @RequestParam("username")String username,@RequestParam("password")String password,@RequestParam("captchaPin")String captchaPin, String msg, Model model,HttpServletRequest request) throws Exception  {

        HttpSession session = request.getSession();


        //Fai lo sha 256 di dominio+username+pin

        //1) crea la stringa da concatenare
        dominio = dominio.concat(username);
        System.out.println("Dominio    "+dominio);

        String dominioUsernamePin = dominio.concat(captchaPin);
        System.out.println("Dominio    "+dominio);
        System.out.println("dominioUsernamePin    "+dominioUsernamePin);
        //String dominioUsernamePin = dominio;



        //il mio sha che devo codificare a livello di stringa per essere indice dell'hashmap
        byte[] encodedhash = hashing256(dominioUsernamePin);
        String KeyCifratura = codificaStringa(encodedhash);

        System.out.println("La chiave di cifratura è : "+KeyCifratura);
        System.out.println("La password è : "+password);
        byte[] encrypted = encrypt(password, KeyCifratura);

        //tale password cifrata deve essere salvata nel file di testo come:  codificaStringone(hash256)=passwordCriptata
        //ma prima dobbiamo controllare che per le credenziali di accesso non siano già state salvate

        /*inizio 02/09*/
        InputStream is = getClass().getResourceAsStream("/credenziali.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        for (String str = reader.readLine(); str != null; str = reader.readLine()) {



            if(str.isEmpty()){continue;}

            //controllo che non si stiano registrando credenziali per lo stesso username e dominio
            byte[] dominioUsernameHash = hashing256(dominio);
            String dominioUsername = Base64.getEncoder().encodeToString(dominioUsernameHash);

            //in modo da non far andare in errore String stringaDaConfrontare=str.substring(0,str.indexOf(" ")-1);
            String spazio = " ";
            dominioUsername=dominioUsername+spazio;

            System.out.println("dominioUsername   "+ dominioUsername);
            System.out.println("str               "+ str);
            if(dominioUsername.equals(str)){
                msg = "Registrazione NON EFFETTUATA dominio e username precedentemente registrati";

                model.addAttribute("msg", msg);
                return "registrazione_effettuata";
            }


            //se stai valutando la stringa con solo dominio e username devi gestire
            //l'errore di str.indexOf(" ")-1



            String stringaDaConfrontare=str.substring(0,str.indexOf(" ")-1);
            String indice = Base64.getEncoder().encodeToString(encodedhash);


            System.out.println("stringaDaConfrontare: " + stringaDaConfrontare);
            System.out.println("indice: " + indice);

            if(stringaDaConfrontare.equals(indice)){

                //restituisci un messaggio con scritto che esiste già un account con le seguenti credenziali


                msg = "Registrazione NON effettuata";

                model.addAttribute("msg", msg);
                return "registrazione_effettuata";

            }


        }
        /*fine 02/09*/




        File file= new File (this.getClass().getResource("/credenziali.txt").toURI()   );
        FileWriter fw;

        if (file.exists())
        {

            fw = new FileWriter(file,true);
            PrintWriter out = new PrintWriter(fw);

            String uguale = "=";

            //modifica 26/08
            String spazio = " ";
            //Base64 Encoded

            String encoded = Base64.getEncoder().encodeToString(encrypted);
            System.out.println("La mia stringa encoded è: "+encoded );

            //String KeyCifraturaCompleta = KeyCifratura+uguale+encoded;

            //modifica 26/08
            String indice = Base64.getEncoder().encodeToString(encodedhash);
            String KeyCifraturaCompleta = indice+uguale+spazio+encoded;
            System.out.println("La KeyCifraturaCompleta:  "+KeyCifraturaCompleta);

            out.append(KeyCifraturaCompleta);
            out.println("\n");

            //in tale fase salviamo in sha256 anche la concatenazione di dominio e di username
            //in tal modo possiamo controllare in fase di salvataggio se tali credenziali siano state già salvate

            byte[] dominioUsernameHash = hashing256(dominio);
            String dominioUsername = Base64.getEncoder().encodeToString(dominioUsernameHash);
            //in modo da non far andare in errore String stringaDaConfrontare=str.substring(0,str.indexOf(" ")-1);
            dominioUsername=dominioUsername+spazio;
            out.append(dominioUsername);
            out.println("\n");

            out.close();
            fw.close();
        }




        msg = "Registrazione effettuata";

        model.addAttribute("msg", msg);
        return "registrazione_effettuata";
    }










    @RequestMapping(value = "/esegui_registrazione", method = RequestMethod.GET)
    public String registrazione(@RequestParam("dominio")String dominio, @RequestParam("username")String username,@RequestParam("password")String password, String msg, Model model,HttpServletRequest request) throws IOException, URISyntaxException, NoSuchAlgorithmException  {




        //inserisci qui la funzione random per la creazione del numero casuale
        //creare funzione random
        Random random = new Random();

        //generazione di un numero casuale tra 0 e 4999
        int k = random.nextInt(4999);

        String number =Integer.toString(k);
        //generazione di una stringa per il richiamo di un identificativo della gif
        String mtfa = "mtfa_00";
        number=mtfa.concat(number);

        msg = number;
        model.addAttribute("msg", msg);

        model.addAttribute("messaggio", "prova");

        return "registration_page";

    }



    public static byte[] encrypt(String plainText, String key) throws Exception {
        byte[] clean = plainText.getBytes();

        // Generating IV.
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Hashing key.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(key.getBytes("UTF-8"));





        byte[] keyBytes = new byte[16];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

        return encryptedIVAndText;
    }



    public byte[] hashing256 (String daCodificare) throws NoSuchAlgorithmException, UnsupportedEncodingException{


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(daCodificare.getBytes("UTF-8"));



        for(int i=0; i<encodedhash.length; i++){

            System.out.println(encodedhash[i]);
        }

        return encodedhash;
    }





    public String  codificaStringa(byte [] encodedhash) throws IOException, NoSuchAlgorithmException{


        char stringa8192[] = new char[8192] ;
        String keyCifrata = "";


        //Leggo l'array di 8192 caratteri generato precedentemente randomicamente
        InputStream is = getClass().getResourceAsStream("/stringaCodificata.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String str;
        while((str = reader.readLine())!=null){
            //devo mettere str dentro un array di 8192 caratteri così mi posso prendere la posizione che voglio
            stringa8192 =str.toCharArray();
        }
        reader.close();


        //3) scorriamo l'array8192 e l'array encodedhash
        for(int i=0, j=0; i< encodedhash.length && j<stringa8192.length  ; i=i+1,j=j+255) {

            //devi fare il modulo della encodedhash[i] perchè potrebbe essere anche negativo

            int indice = Math.abs(encodedhash[i]);



            //tale elemento va cocatenato con i valori generati man mano per creare il pin generale
            char elementoKeyCifrata =stringa8192[j+indice];

            keyCifrata = keyCifrata.concat(String.valueOf(elementoKeyCifrata));

        }

        return keyCifrata;

    }
    public static String decrypt(byte[] encryptedIvTextBytes, String key) throws Exception {
        int ivSize = 16;
        int keySize = 16;

        // Extract IV.
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        // Hash key.
        byte[] keyBytes = new byte[keySize];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes());
        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

        return new String(decrypted);
    }
}
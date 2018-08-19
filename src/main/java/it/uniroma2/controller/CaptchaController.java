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


@Controller
public class CaptchaController {





    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public String authentication( ) {
        System.out.println("Sto indirizzando all autenticazione");

        //TO DO

        //pulisci la sessione




        return "authentication_page";
    }






    @RequestMapping(value = "/verificaCredenzialiAccesso", method = RequestMethod.GET)
    public String verificaCredenzialiAccesso(@RequestParam("captchaPin")String captchaPin, HttpServletRequest request ) throws Exception {

        HttpSession session = request.getSession();
        Properties properties = new Properties();
        String passwordCodificataNelFile = null;



        String username = (String) session.getAttribute("username");
        String dominio = (String) session.getAttribute("dominio");

        //la tua password �:

        //mi rigenero lo sha 256(dominio+username+captchaPin)

        dominio = dominio.concat(username);
        dominio = dominio.concat(captchaPin);
        String dominioUsernamePassword = dominio;

        byte[] encodedhash = hashing256(dominioUsernamePassword);



        String KeyCifratura = codificaStringa(encodedhash);
        System.out.println("La KEYcifratura in fase di autenticazione �: " + KeyCifratura);


        //leggo il file per scoprire le credenziali di accesso
        InputStream is = getClass().getResourceAsStream("/credenziali.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        //da gestire questo reader
        String str;
        while((str = reader.readLine())!=null){

            //analizza i primi 32 caratteri
            //se corrispondono alla KEYCIFRATURA allora pendi la parte successiva senza ' '=' '
            //e la decifri per ottenere la password

            if(str.isEmpty()){break;}
            System.out.println("str        :" + str);
            String stringaDaConfrontare=str.substring(0,32);

            System.out.println("stringaDaConfrontare        :" + stringaDaConfrontare);
            System.out.println("KeyCifratura        :" + KeyCifratura);

            if(stringaDaConfrontare.equals(KeyCifratura)){
                passwordCodificataNelFile = str.substring(33,str.length());
                byte[] decoded = Base64.getDecoder().decode(passwordCodificataNelFile);
                String password = decrypt(decoded  , KeyCifratura);
                System.out.println("Il valore della password criptata nel file � :" + password);
            }

        }



        return "redirect:https://www.google.com/";
    }














    @RequestMapping(value = "/autenticazione", method = RequestMethod.GET)
    public String autenticazione( @RequestParam("Dominio")String dominio, @RequestParam("Username")String username, String msg, Model model, HttpServletRequest request ) throws IOException, URISyntaxException, NoSuchAlgorithmException  {
        System.out.println("Redirecting Result To The Final Page");

        //salva in sessione il dominio e l'username e porta alla pagina del captcha

        HttpSession session = request.getSession();

        session.setAttribute("dominio", dominio);
        session.setAttribute("username", username);




        return "pagina_captcha_autenticazione";

    }





    @RequestMapping(value = "/registrazione", method = RequestMethod.GET)
    public String indirizzaPaginaRegistrazione( ) {
        System.out.println("Sto indirizzando alla registrazione");
        return "registration_page";
    }





    @RequestMapping(value = "/creaPasswordCifrata", method = RequestMethod.GET)
    public String creaPasswordCifrata(@RequestParam("captchaPin")String captchaPin, String msg, Model model,HttpServletRequest request) throws Exception  {

        HttpSession session = request.getSession();
        String password = (String) session.getAttribute("password");
        String dominio = (String) session.getAttribute("dominio");
        String username = (String) session.getAttribute("username");

        //Fai lo sha 256 di dominio+username+pin

        //1) crea la stringa da concatenare
        dominio = dominio.concat(username);
        dominio = dominio.concat(captchaPin);
        String dominioUsernamePassword = dominio;

        byte[] encodedhash = hashing256(dominioUsernamePassword);

        String KeyCifratura = codificaStringa(encodedhash);

        System.out.println("La chiave di cifratura � : "+KeyCifratura);

        byte[] encrypted = encrypt(password, KeyCifratura);

        //tale password cifrata deve essere salvata nel file di testo codificaStringone(hash256)=passwordCriptata

        File file= new File (this.getClass().getResource("/credenziali.txt").toURI()   );
        FileWriter fw;

        if (file.exists())
        {

            fw = new FileWriter(file,true);
            PrintWriter out = new PrintWriter(fw);

            String uguale = "=";

            //Base64 Encoded
            String encoded = Base64.getEncoder().encodeToString(encrypted);

            System.out.println("La mia stringa encoded �: "+encoded );

            String KeyCifraturaCompleta = KeyCifratura+uguale+encoded;

            out.append(KeyCifraturaCompleta);
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


        HttpSession session = request.getSession();

        session.setAttribute("dominio", dominio);
        session.setAttribute("username", username);
        session.setAttribute("password", password);


        return "pagina_captcha";

    }

    //String dominio = "google";


  /*  InputStream is = getClass().getResourceAsStream("/credenziali.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

   String autenticazione =  captchaPinCodificato;
   System.out.println(autenticazione);
   String str;
   while((str = reader.readLine())!=null){

	   if(str.equals(autenticazione)){
		   msg = "credenziali precedentemente registrate";
		   model.addAttribute("msg", msg);
		   System.out.println("Credenziali gi� presenti");
		   reader.close();
		   return "pagina_errori_credenziali";
	   }

   }*/

    //salva la password nel file di testo



	/*

	   msg = "La password generata �: " + passwordCodificata;

   //File file= new File (this.getClass().getResource("/credenziali.txt").toURI());
   File file= new File (this.getClass().getResource("/credenziali.txt").toURI()   );
   FileWriter fw;

   if (file.exists())
   {
   	//salva all'interno del file di testo solamente il PIN
      fw = new FileWriter(file,true);
      PrintWriter out = new PrintWriter(fw);
      out.println("\n");
		out.append(passwordCodificata);
		out.close();
      fw.close();
   }
   else
   {
   	//TODO
      file.createNewFile();
      fw = new FileWriter(file);
   }

   //restituisci a video la password composta dal PIN + DOMINIO

   //creo la password PIN + DOMINIO


   model.addAttribute("msg", msg);

	return "pagina_captcha";
}




//passiamo i parametri alla final Page per poterli visualizzare
//@RequestMapping(value = "/final_page", method = RequestMethod.GET)
//public String finalPage() {
 //     System.out.println("Showing The Redirected Page");


 //     return "final";
 // }

*/

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
            //devo mettere str dentro un array di 8192 caratteri cos� mi posso prendere la posizione che voglio
            stringa8192 =str.toCharArray();
        }
        reader.close();


        //3) scorriamo l'array8192 e l'array encodedhash
        for(int i=0, j=0; i< encodedhash.length && j<stringa8192.length  ; i=i+1,j=j+255) {

            //devi fare il modulo della encodedhash[i] perch� potrebbe essere anche negativo

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
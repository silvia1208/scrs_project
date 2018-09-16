/**
 * Funzione che controlla se il pin è composto da 6 cifre
 * @param codice
 */
function check_codice(codice){
    if(codice.length==6){
       check_authentication(codice)
    }else{
        alert('Il codice deve essere composto da 6 cifre')
    }
}

/**
 * Funzione per effettuare la chiamata per l'autenticazione
 * @param codice
 */
function check_authentication(codice){
    var domain = document.getElementById("domain").value;
    var username = document.getElementById("user").value;

    //Imposta nei cookie i valori inseriti dall'utente per dominio e username
    setCookie("domain",domain,3);
    setCookie("username",username,3);

    $.ajax({
        type : "POST",
        url : "verificaCredenzialiAccesso",
        data : "Dominio=" + domain + "&Username=" + username+"&captchaPin="+codice,
        dataType: 'json',
        success : function(response) {
            if(response.status =='correct'){
                document.getElementById("error").style.display="none";
                document.getElementById("gestore-captcha").style.display="none";
                document.getElementById("success-div").style.display="block";
                document.getElementById('success-info').innerHTML = '<br> La password per accedere al dominio richiesto &egrave;:  <span class="boldText">' + response.password + '</span>';
            }else{
                document.getElementById("error").style.display="block";
                document.getElementById('error').innerHTML = '<br> I dati inseriti non sono presenti nel sistema';
            }

        },
        error : function(e) {
            alert('Error: ' + e);
        }
    });
}

/**
 * Effettua la chiamata per la registrazione dei dati inseriti dall'utente
 */
function registrati(codice){
    var domain = document.getElementById("domain").value;
    var username = document.getElementById("user").value;
    var password = document.getElementById("password").value;
    $.ajax({
        type : "POST",
        url : "creaPasswordCifrata",
        data : "dominio=" + domain + "&username=" + username+"&password="+password+"&captchaPin="+codice,
        dataType: 'json',
        success : function(response) {
            if(response.status =='correct'){
                  window.location.href="http://localhost:8080/scrsproject/authentication"
            }else{
                document.getElementById("error").style.display="block";
                document.getElementById('error').innerHTML = response.status;
            }

        },
        error : function(e) {
            alert('Error: ' + e);
        }
    });
}

/**
 * Imposta il valore del parametro all'interno dei cookie
 */
function setCookie(name,value,days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

/**
 * Estrae il valore del parametro richiesto dai cookie
 * @param name
 * @returns {*}
 */
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

/**
 * Controlla la configurazione dei div visualizzati nella pagina
 */
function check_configuration() {
    document.getElementById("gestore-captcha").style.display="block";
    document.getElementById("success-div").style.display="none";
}
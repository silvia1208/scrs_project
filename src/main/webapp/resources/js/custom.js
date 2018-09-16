
/*Call ajax for  go back*/
function check_codice(codice){
    alert(codice.length)
    if(codice.length==6){
       check_authentication(codice)
    }else{
        alert('Il codice deve essere composto da 6 cifre')
    }
}

function check_authentication(codice){
    var domain = document.getElementById("domain").value;
    var username = document.getElementById("user").value;
 //   var password = document.getElementById("password").value;

    setCookie("domain",domain,3);
    setCookie("username",username,3);

    alert('Chiamata per autenticazione: '+domain+username+codice)
    $.ajax({
        type : "POST",
        url : "verificaCredenzialiAccesso",
        data : "Dominio=" + domain + "&Username=" + username+"&captchaPin="+codice,
        dataType: 'json',
        success : function(response) {
            alert(response.status);
            if(response.status =='correct'){
             //   window.location.href="http://www.google.it"
                document.getElementById("error").style.display="block";
                document.getElementById('error').innerHTML = '<br>' + response.password;
            }else{
                document.getElementById("error").style.display="block";
                document.getElementById('error').innerHTML = '<br> Si &egrave; verificato un problema in fase di autenticazione';
                alert('Si è verificato un problema in fase di autenticazione')
            }

        },
        error : function(e) {
            alert('Error: ' + e);
        }
    });
}

function registrati(codice){
    var domain = document.getElementById("domain").value;
    var username = document.getElementById("user").value;
    var password = document.getElementById("password").value;
    alert('Chiamata per autenticazione: '+domain+username+codice)
    $.ajax({
        type : "POST",
        url : "creaPasswordCifrata",
        data : "dominio=" + domain + "&username=" + username+"&password="+password+"&captchaPin="+codice,
        dataType: 'json',
        success : function(response) {
            alert(response.status);
            if(response.status =='correct'){
                 alert("registrazione a buon fine")
                  window.location.href="http://localhost:8080/authentication"
             //   document.getElementById("error").style.display="block";
             //   document.getElementById('error').innerHTML = '<br>' + response.password;
            }else{
                document.getElementById("error").style.display="block";
                document.getElementById('error').innerHTML = '<br> Si &egrave; verificato un problema in fase di autenticazione';
                alert('Si è verificato un problema in fase di autenticazione')
            }

        },
        error : function(e) {
            alert('Error: ' + e);
        }
    });
}

function setCookie(name,value,days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}
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
function go_second_step(){
    document.getElementById("first-step").style.display="none";
    document.getElementById("second-step").style.display="block";
}

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
    var password = document.getElementById("password").value;

    alert('Chiamata per autenticazione: '+domain+username+codice+password)
    $.ajax({
        type : "POST",
        url : "verificaCredenzialiAccesso",
        data : "Dominio=" + domain + "&Username=" + username+"&captchaPin="+codice+"&Password="+password,
        dataType: 'json',
        success : function(response) {
            alert(response.status);
            if(response.status =='correct'){
                window.location.href="http://www.google.it"
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

}
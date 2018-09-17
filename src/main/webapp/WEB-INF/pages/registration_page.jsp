<html lang="en" dir="ltr"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"  rel="stylesheet" type="text/css" >
    <link href="${pageContext.request.contextPath}/resources/css/signin.css" rel="stylesheet" type="text/css" media="all">
    <link href="${pageContext.request.contextPath}/resources/css/custom.css" rel="stylesheet" type="text/css" media="all">
    <script src="${pageContext.request.contextPath}/resources/js/raphael.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/popper.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/custom.js"> </script>




    <script type="text/javascript">
        var valori = ${XXXVALORI}; //matrice delle lettere contenute dentro l'immagine
        var codice = ""; //codice selezionato dall'utente
        var captcha; //oggetto per la libreria raphael
        var dim; //dimensione del contenitore dell'immagine
        var pos; //posizione iniziale del svg
        var circle; //posizione dei cerchi in base alle lettere selezionate
        var vCode; //posizione dei codici
        var posCircle; //Numero di cerchi inseriti


        $(document).ready(function () {
            start();
        });

        function start() {

            //Inizializza oggetto per la gestione del captcha
            captcha = Raphael(document.getElementById('pointer_div'), 320, 320);

            //Imposta le dimensioni di una cella dividendo la grandezza del captcha per il numero di celle
            dim = (320.0 / 6.0);
            pos = 0;

            //inizializza le variabili
            circle = new Array(64);
            for (var i = 0; i < 64; ++i)
                circle[i] = new Array(2);	//x e y
            posCircle = 0;
            vCode = new Array(64);
            vCode[0] = "";
            message_color = "gray";
            // status = 0;
        }

        /**
         * Funzione per gestire gli eventi all'interno dell'immagine
         * @param event
         * @param quale
         */
        function point_it(event, quale) {
            var pos_x = 0;
            var pos_y = 0;
            var pointer_div = document.getElementById('pointer_div');

            //calcola la posizione del click in funzione delle dimensioni del div
            pos_x = (event.offsetX ? (event.offsetX) : event.layerX) - pointer_div.offsetLeft;
            pos_y = (event.offsetY ? (event.offsetY) : event.layerY) - pointer_div.offsetTop;

            var x = Math.floor((pos_x+dim/4) / dim);
            var y = Math.floor((pos_y) / dim);
            circle[posCircle][0] = x;
            circle[posCircle][1] = y;

            //imposta nel vcode il valore selezionato
            vCode[posCircle] = valori[x][y].toString();

            //se il codice non ha ancora aggiunto la lunghezza richiesta si disegna il cerchio intorno alla lettera
            if(codice.length<6){
                codice = codice + valori[x][y].toString();
                posCircle++;

                var cerchio = captcha.circle(0, 0, 0).attr({stroke: "blue", "stroke-width": 0});
                cerchio.animate({
                    "20%": {cx: pos_x, cy: pos_y, r: 0, "stroke-width": 0},
                    "100%": {cx: x*dim+dim/2, cy: y*dim+dim/2, r: dim / 2.5, "stroke-width": 4, easing: ">"}
                }, 100);
            }

        }

        /**
         * Funzione che elimina l'ultimo carattere inserito
         */
        function delete_last_number() {
            captcha.clear();
            posCircle--;
            if (posCircle <= 0)
            {
                posCircle = 0;
            }
            codice = "";
            for (var i = 0; i < posCircle; ++i)
            {
                var cerchio = captcha.circle(0, 0, 0).attr({stroke: "blue", "stroke-width": 0});
                cerchio.animate({
                    "90%": {cx: circle[i][0], cy: circle[i][1], r: 0, "stroke-width": 0},
                    "100%": {cx: circle[i][0]*dim+dim/2, cy: circle[i][1]*dim+dim/2, r: dim / 2.2, "stroke-width": 4, easing: ">"}
                }, 100);
                codice += vCode[i];
            }
        }
    </script>
    <title>SCRS - Registrazione</title>
</head>
<body>
<div class="container">

    <div class="row">
        <div class="col-md-2">
            &nbsp;
        </div>

        <div class="col-md-8">
            <div class="form-signin">
                <h2 class="form-signin-heading">Registrazione</h2>
                <div id="error" class="alert alert-danger" role="alert">
                </div>
                <label for="domain" class="sr-only">Dominio</label>
                <input  class="form-control" id="domain" name="Dominio" type="text" value="" placeholder="Dominio" autofocus required/>
                <label for="user" class="sr-only">Username</label>
                <input class="form-control" id="user" name="Username" type="text" value="" placeholder="Username" required />
                <label for="password" class="sr-only">Password</label>
                <input class="form-control" id="password" name="Password" type="password" value="" placeholder="Password"  required/>
                <button type="button" class="btn btn-lg btn-primary btn-block" onclick="delete_last_number()">
                    Elimina ultimo carattere <span class="glyphicon glyphicon-trash"></span>
                </button>
                <table align="center">
                    <tr>
                        <td>
                            <div id="pointer_div" onclick="point_it(event, 0)" style="margin-right:auto; margin-left:auto; background-position: center; width: 320px; height: 320px; background-image: url(${pageContext.request.contextPath}/resources/img/captcha/${capthcha_image}.gif)"> </div>
                        </td>
                    </tr>
                </table>
                <button  onclick="registrati(codice)" class="btn btn-lg btn-primary btn-block" type="submit">
                    Registrati <span class="glyphicon glyphicon-pencil"></span>
                </button>


            </div>
        </div>
        <div class="col-md-2">
            &nbsp;
        </div>

    </div>

</div>
</body>
</html>


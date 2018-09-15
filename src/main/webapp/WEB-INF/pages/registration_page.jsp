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
        var valori = ${XXXVALORI};
        var codice = "";
        var captcha;
        var dim;
        var pos;
        var status;
        var vCerchi;
        var vCodici;
        var posCerchi;

        function trim(str) {
            var newstr;
            newstr = str.replace(/^\s*/, "").replace(/\s*$/, "");
            newstr = newstr.replace(/\s{2,}/, " ");
            return newstr;
        }

        $(document).ready(function () {
            start();
        });

        function start() {
            captcha = Raphael(document.getElementById('pointer_div'), 320, 320);

            dim = (320.0 / 6.0);
            pos = 0;

            vCerchi = new Array(64);
            for (var i = 0; i < 64; ++i)
                vCerchi[i] = new Array(2);	//x e y
            posCerchi = 0;
            vCodici = new Array(64);
            vCodici[0] = "";
            message_color = "gray";
            status = 0;
        }
        ;

        function Set_Cookie(name, value, expires, path, domain, secure) {
            var today = new Date();
            today.setTime(today.getTime());
            if (expires) {
                expires = expires * 1000 * 60 * 60 * 24;
            }
            var expires_date = new Date(today.getTime() + (expires));

            document.cookie = name + "=" + escape(value) +
                ((expires) ? ";expires=" + expires_date.toGMTString() : "") +
                ((path) ? ";path=" + path : "") +
                ((domain) ? ";domain=" + domain : "") +
                ((secure) ? ";secure" : "");
        }

        function Delete_Cookie(name, path, domain) {
            if (Get_Cookie(name))
                document.cookie = name + "=" +
                    ((path) ? ";path=" + path : "") +
                    ((domain) ? ";domain=" + domain : "") +
                    ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
        }

        function Get_Cookie(check_name) {
            var a_all_cookies = document.cookie.split(';');
            var a_temp_cookie = '';
            var cookie_name = '';
            var cookie_value = '';
            var b_cookie_found = false;

            for (i = 0; i < a_all_cookies.length; i++) {
                a_temp_cookie = a_all_cookies[i].split('=');
                cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');
                if (cookie_name === check_name) {
                    b_cookie_found = true;
                    if (a_temp_cookie.length > 1) {
                        cookie_value = unescape(a_temp_cookie[1].replace(/^\s+|\s+$/g, ''));
                    }
                    return cookie_value;
                    break;
                }
                a_temp_cookie = null;
                cookie_name = '';
            }
            if (!b_cookie_found) {
                return null;
            }
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
            pos_x = (event.offsetX ? (event.offsetX) : event.layerX) - pointer_div.offsetLeft;
            pos_y = (event.offsetY ? (event.offsetY) : event.layerY) - pointer_div.offsetTop;

            var x = Math.floor((pos_x+dim/4) / dim);
            var y = Math.floor((pos_y) / dim);
            vCerchi[posCerchi][0] = x;
            vCerchi[posCerchi][1] = y;


            vCodici[posCerchi] = valori[x][y].toString();
            if(codice.length<6){
                codice = codice + valori[x][y].toString();
                posCerchi++;

                var cerchio = captcha.circle(0, 0, 0).attr({stroke: "blue", "stroke-width": 0});
                cerchio.animate({
                    "20%": {cx: pos_x, cy: pos_y, r: 0, "stroke-width": 0},
                    "100%": {cx: x*dim+dim/2, cy: y*dim+dim/2, r: dim / 2.5, "stroke-width": 4, easing: ">"}
                }, 100);
            }

        }

        function delete_last_number() {
            captcha.clear();
            posCerchi--;
            if (posCerchi <= 0)
            {
                posCerchi = 0;
            }
            codice = "";
            for (var i = 0; i < posCerchi; ++i)
            {
                var cerchio = captcha.circle(0, 0, 0).attr({stroke: "blue", "stroke-width": 0});
                cerchio.animate({
                    "90%": {cx: vCerchi[i][0], cy: vCerchi[i][1], r: 0, "stroke-width": 0},
                    "100%": {cx: vCerchi[i][0]*dim+dim/2, cy: vCerchi[i][1]*dim+dim/2, r: dim / 2.2, "stroke-width": 4, easing: ">"}
                }, 100);
                codice += vCodici[i];
            }
        }
    </script>
</head>
<body>
<div class="container">

    <div class="row">
        <div class="col-md-2">
            &nbsp;
        </div>

        <div class="col-md-8">
            <div id="error" class="alert alert-danger" role="alert">
            </div>
            <div class="form-signin">
                <h2 class="form-signin-heading">Registrazione</h2>
                <label for="domain" class="sr-only">Dominio</label>
                <input  class="form-control" id="domain" name="Dominio" type="text" value="" placeholder="Dominio" required autofocus />
                <label for="user" class="sr-only">Username</label>
                <input class="form-control" id="user" name="Username" type="text" value="" required placeholder="Username" />
                <label for="password" class="sr-only">Password</label>
                <input class="form-control" id="password" name="Password" type="password" value="" required placeholder="Password" />
                <button type="button" class="btn btn-default" onclick="delete_last_number()">
                    <span class="glyphicon glyphicon-trash"></span> Delete
                </button>
                <table align="center">
                    <tr>
                        <td>
                            <div id="pointer_div" onclick="point_it(event, 0)" style="margin-right:auto; margin-left:auto; background-position: center; width: 320px; height: 320px; background-image: url(/resources/img/captcha/${capthcha_image}.gif)"> </div>
                        </td>
                    </tr>
                </table>
                <button  onclick="registrati(codice)" class="btn btn-lg btn-primary btn-block" type="submit">Registrati</button>


            </div>
        </div>
        <div class="col-md-2">
            &nbsp;
        </div>

    </div>

</div>
</body>
</html>

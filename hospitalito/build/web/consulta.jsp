<%-- 
    Document   : consulta
    Created on : 22 oct 2025, 19:24:08
    Author     : rocio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>        
        <%

            String mes = request.getParameter("mes");
            String modalidad = request.getParameter("modalidad");
            String mesN = "";
            String modal = "";
            
            switch (mes) {
                case "1": 
                    mesN = "Enero";
                break;
                case "2": 
                    mesN = "Febrero";
                break;
                case "3": 
                    mesN = "Marzo";
                break;
                case "4": 
                    mesN = "Abril";
                break;
                case "5": 
                    mesN = "Mayo";
                break;
                case "6": 
                    mesN = "Junio";
                break;
                case "7": 
                    mesN = "Julio";
                break;
                case "8": 
                    mesN = "Agosto";
                break;
                case "9": 
                    mesN = "Septiembre";
                break;
                case "10": 
                    mesN = "Octubre";
                break;
                case "11": 
                    mesN = "Noviembre";
                break;
                case "12": 
                    mesN = "Diciembre";
                break;
                default: mesN = "";
            }
            
            switch (modalidad) {
                case "ingreso": 
                    modal = "ingresados";
                break;
                case "egreso": 
                    modal = "egresados";
                break;
                default: modal = "";
            }
            
            paquetito.consulta c = new paquetito.consulta();

            if (c.existeControlador()) {
                out.println("<header class=header>");
                out.println("<h1>Hospital-ITO</h1>");
                out.println("<h2>Pacientes " + modal + " en el mes de " + mesN + " de 2025</h2>");
                out.println("</header>");
                out.println(c.consultaPacientes(mes, modalidad));
            } else {
                out.println("No existe el controlador");
            }
        %>
    </body>
    
    <style>
        .header {
            background: #4a90e2;
            color: white;
            text-align: center;
            padding: 15px;
            font-family:'Segoe UI',Arial,sans-serif;
        }
    </style>
</html>

<%-- 
    Document   : consultaDetalles
    Created on : 22 oct 2025, 20:41:32
    Author     : rocio
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            String numExpediente = request.getParameter("num_expediente");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            
            paquetito.consulta c = new paquetito.consulta();
            
            out.println(c.verDetalles(numExpediente, nombre, apellidos));
        %>
    </body>
</html>

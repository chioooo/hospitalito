/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquetito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rocio
 */
public class consulta {

    public boolean existeControlador() {
        boolean existe = false;

        try {
            // Se busca que el driver JDBC exista para establecer la conexión a la BD
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            existe = true;
        } catch (Exception ex) {
            existe = false;
            //Error por que el Driver no existe
            System.out.println("Fallo driver: " + ex);
        }

        return existe;
    }

    public String consultaPacientes(String mes, String modalidad) {
        String resultado = "";

        Connection conn = null;

        Statement stat = null;

        ResultSet rs = null;

        try {
            //Se conecta con el servidor de la BD
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/hospital_examen", "root", "");

        } catch (SQLException se) {
            //En caso de que el servidor este apagado o no se está ejecutando
            resultado += "Mensaje: " + se.getMessage();
            resultado += "Estado: " + se.getSQLState();
            resultado += "Error: " + se.getErrorCode();
        }

        try {
            stat = conn.createStatement();

            //Se conecta a la base de datos
            //rs=stat.executeQuery("use agenda;");
            // Se establece la consulta SQL
            String sql = "";
            if (modalidad.equalsIgnoreCase("ingreso")) {
                sql = "SELECT * FROM pacientes "
                        + "WHERE MONTH(fecha_ingreso) = " + mes;
            } else if (modalidad.equalsIgnoreCase("egreso")) {
                sql = "SELECT * FROM pacientes "
                        + "WHERE MONTH(fecha_egreso) = " + mes;
            }

            rs = stat.executeQuery(sql);
            
            resultado = "<style>"
                    + "body{font-family:'Segoe UI',Arial,sans-serif;background-color:#ffffff;color:#333;}"
                    + "table{border-collapse:collapse;width:80%;margin:20px auto;font-size:14px;background-color:#fff;border:1px solid #dcdcdc;border-radius:6px;overflow:hidden;}"
                    + "th{background-color:#f2f5f9;color:#1a1a1a;text-align:left;padding:10px;border:1px solid #dcdcdc;font-weight:600;}"
                    + "td{padding:10px;border:1px solid #e6e6e6;}"
                    + "tr:nth-child(even) td{background-color:#fafafa;}"
                    + ".menu-button {display: block;margin: 20px auto;padding: 10px 25px;background-color: #4a90e2;color: white;border: none;border-radius: 6px;font-size: 15px;cursor: pointer;font-weight: 500;}"
                    + ".ver-button {display: block;margin: 20px auto;padding: 10px 25px;background-color: #90e24a;color: white;border: none;border-radius: 6px;font-size: 15px;cursor: pointer;font-weight: 500;}"
                    + "</style>";

            resultado += "<table border=1>";
            resultado += "<th> No. Expediente </th>";
            resultado += "<th> Nombre </th>";
            resultado += "<th> Apellidos </th>";
            resultado += "<th> Dirección </th>";
            resultado += "<th> Teléfono </th>";
            resultado += "<th> Ciudad </th>";
            resultado += "<th> RFC </th>";
            resultado += "<th> CURP </th>";
            resultado += "<th> Fecha </th>";
            resultado += "<th> Acción </th>";

            //Se extraen los registros de la base de datos
            while (rs.next()) {
                //Se extrae cada campo de la tabla
                resultado += "<tr>";
                resultado += "<td> " + rs.getString("num_expediente") + "</td>";
                String num_expediente = rs.getString("num_expediente");
                resultado += "<td> " + rs.getString("nombre") + "</td>";
                String nombre = rs.getString("nombre");
                resultado += "<td> " + rs.getString("apellidos") + "</td>";
                String apellidos = rs.getString("apellidos");
                resultado += "<td> " + rs.getString("direccion") + "</td>";
                resultado += "<td> " + rs.getString("telefono") + "</td>";
                resultado += "<td> " + rs.getString("ciudad") + "</td>";
                resultado += "<td> " + rs.getString("rfc") + "</td>";
                resultado += "<td> " + rs.getString("curp") + "</td>";
                
                String fecha = "";
                if(modalidad.equals("ingreso")) {
                    fecha = rs.getString("fecha_ingreso");
                }else if(modalidad.equals("egreso")){
                    fecha = rs.getString("fecha_egreso");
                }
                
                resultado += "<td> " + fecha + "</td>";
                resultado += "<td>" + "<form action=consultaDetalles.jsp methop=POST" + "</td>";
                resultado += "<input type=hidden name=num_expediente value='" + num_expediente + "'>";
                resultado += "<input type=hidden name=nombre value='" + nombre + "'>";
                resultado += "<input type=hidden name=apellidos value='" + apellidos + "'>";
                resultado += "<input class=ver-button type=Submit value=Ver>";
                resultado += "</form>" + "</td>";

                resultado += "</tr>";
            }

            resultado += "</table>";
            
            resultado+="<br><form action=index.html><input class=menu-button type=Submit value=Menu></form>";
        } catch (SQLException se) {
            //En caso de que la consulta SQL no haya obtenido registros
            resultado += "LMensaje: " + se.getMessage();
            resultado += "LEstado: " + se.getSQLState();
            resultado += "LError: " + se.getErrorCode();
        }

        return resultado;
    }

    public String verDetalles(String numExpediente, String nombre, String apellidos) {
        String resultado = "";

        Connection conn = null;

        Statement stat = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            //Se conecta con el servidor de la BD
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/hospital_examen", "root", "");

        } catch (SQLException se) {
            //En caso de que el servidor este apagado o no se está ejecutando
            resultado += "Mensaje: " + se.getMessage();
            resultado += "Estado: " + se.getSQLState();
            resultado += "Error: " + se.getErrorCode();
        }

        try {
            String sql1 = "SELECT h.consultorio, h.piso_consultorio, h.medico, h.especialidad_medico "
                    + "FROM historial_clinico h "
                    + "JOIN pacientes p ON h.num_expediente = p.num_expediente "
                    + "WHERE p.num_expediente = '" + numExpediente + "'";

            Statement stat1 = conn.createStatement();
            rs1 = stat1.executeQuery(sql1);

            String sql2 = "SELECT fecha_consulta, observaciones "
                    + "FROM consultas "
                    + "WHERE num_expediente = '" + numExpediente + "' "
                    + "ORDER BY fecha_consulta DESC "
                    + "LIMIT 3";

            Statement stat2 = conn.createStatement();
            rs2 = stat2.executeQuery(sql2);
            
            resultado = "<style>"
                    + "body{font-family:'Segoe UI',Arial,sans-serif;background-color:#ffffff;color:#333;}"
                    + "table{border-collapse:collapse;width:80%;margin:20px auto;font-size:14px;background-color:#fff;border:1px solid #dcdcdc;border-radius:6px;overflow:hidden;}"
                    + "th{background-color:#f2f5f9;color:#1a1a1a;text-align:left;padding:10px;border:1px solid #dcdcdc;font-weight:600;}"
                    + "td{padding:10px;border:1px solid #e6e6e6;}"
                    + "tr:nth-child(even) td{background-color:#fafafa;}"
                    + ".menu-button {display: block;margin: 20px auto;padding: 10px 25px;background-color: #4a90e2;color: white;border: none;border-radius: 6px;font-size: 15px;cursor: pointer;font-weight: 500;transition: all 0.2s ease-in-out;}"
                    + ".header {background: #4a90e2;color: white;text-align: center;padding: 15px;font-family:'Segoe UI',Arial,sans-serif;"
                    + "</style>";

            resultado += "<header class=header>";
            resultado += "<h1>Hospital-ITO</h1>";
            resultado += "<h2>Historial clínico</h2>";
            resultado += "</header>";
            
            resultado += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse: collapse; width: 80%; text-align: left;\">";
            resultado += "<tr>";
            resultado += "<th style=\"width: 50%;\">No. Expediente</th>";
            resultado += "<th style=\"width: 50%;\">Nombre Completo</th>";
            resultado += "</tr>";
            resultado += "<tr>";
            resultado += "<td>" + numExpediente + "</td>";
            resultado += "<td>" + nombre + " " + apellidos + "</td>";
            resultado += "</tr>";
            resultado += "</table>";
            resultado += "<br>";

            resultado += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse: collapse; width: 80%; text-align: left;\">";
            resultado += "<tr>";
            resultado += "<th>Consultorio</th>";
            resultado += "<th>Médico</th>";
            resultado += "<th>Fecha de Consulta</th>";
            resultado += "<th>Observaciones</th>";
            resultado += "</tr>";

            if (rs1.next()) {
                String consultorio = rs1.getString("consultorio");
                String piso = rs1.getString("piso_consultorio");
                String medico = rs1.getString("medico");
                String especialidad = rs1.getString("especialidad_medico");

                int fila = 0;
                while (rs2.next()) {
                    String fecha = rs2.getString("fecha_consulta");
                    String obs = rs2.getString("observaciones");

                    resultado += "<tr>";
                    if (fila == 0) {
                        resultado += "<td>" + consultorio + "</td>";
                        resultado += "<td>" + medico + "</td>";
                    } else if (fila == 1) {
                        resultado += "<th>Piso</th>";
                        resultado += "<th>Area de especialidad</th>";
                    } else {
                        resultado += "<td>" + piso + "</td>";
                        resultado += "<td>" + especialidad + "</td>";
                    }

                    resultado += "<td>" + fecha + "</td>";
                    resultado += "<td>" + obs + "</td>";
                    resultado += "</tr>";

                    fila++;
                }
            }

            resultado += "</table>";
            
            resultado+="<br><form action=index.html><input class=menu-button type=Submit value=Menu></form>";
 
        } catch (SQLException se) {
            //En caso de que la consulta SQL no haya obtenido registros
            resultado += "LMensaje: " + se.getMessage();
            resultado += "LEstado: " + se.getSQLState();
            resultado += "LError: " + se.getErrorCode();
        }

        return resultado;
    }
}

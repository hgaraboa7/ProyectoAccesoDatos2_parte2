/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author hector.garaboacasas
 */
public class HistoricoDAO {

    public void enviar(Connection conn, String idcliente, String nomapecli, double total, String numfactura) throws SQLException {

        String consulta="insert into historicofacturadoporcliente (idcliente, nomapecli, importefacturado, observaciones) VALUES (?,?,?,?)";
        PreparedStatement sentencia=conn.prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        sentencia.setString(2, nomapecli);
        sentencia.setDouble(3, total);
        sentencia.setString(4, numfactura);
        sentencia.executeUpdate();
        
        
        
        
    }

    public void enviarinFacturas(Connection conn, String idcliente, String nomapecli) throws SQLException {
      String consulta="insert into historicofacturadoporcliente (idcliente, nomapecli, importefacturado, observaciones) VALUES (?,?,?,?)";
        PreparedStatement sentencia=conn.prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        sentencia.setString(2, nomapecli);
        sentencia.setDouble(3, 0.0);
        sentencia.setString(4, "");
        sentencia.executeUpdate();
        
        
    }
    
    
    
}

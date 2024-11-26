/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hector.garaboacasas
 */
public class DetalleDAO {

    public void detallar(Connection conn, String numFactura, DefaultTableModel modelotabla) throws SQLException {
           String consulta="select idproducto from productos where nomproducto like ? ";
           PreparedStatement sentencia = conn.prepareStatement(consulta);
           String insercion="INSERT INTO detalle (numfactura, numdetalle, idproducto, cantidad, precio) VALUES  (?,?,?,?,?)";
           PreparedStatement sentenciaInsercion=conn.prepareStatement(insercion);
           ResultSet rs=null;
           for(int i=0;i<modelotabla.getRowCount();i++){
               sentencia.setString(1,String.valueOf(modelotabla.getValueAt(i, 0)) );
               rs=sentencia.executeQuery();
               if(rs.next()){
                   sentenciaInsercion.setString(1, numFactura);
                   sentenciaInsercion.setInt(2, i+1);
                   sentenciaInsercion.setString(3, rs.getString(1));
                   sentenciaInsercion.setInt(4,(int) modelotabla.getValueAt(i, 1));
                   sentenciaInsercion.setDouble(5,((double)modelotabla.getValueAt(i, 2)/(int) modelotabla.getValueAt(i, 1)));
                   sentenciaInsercion.executeUpdate();
                   
               }
           }
           
        
    }
//sin comprobar
    public double total(Connection conn, String numfactura) throws SQLException {
        double total=0.0;
        
        String consulta = "select cantidad, precio from detalle where numfactura like ?";
        PreparedStatement sentencia = conn.prepareStatement(consulta);
        String[]numeros=numfactura.split(":");
        
        for(int i=0;i<numeros.length;i++){
    
            sentencia.setString(1, numeros[i]);
            
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                total += rs.getInt(1) * rs.getDouble(2);
            }
        }
            return total; 
    }
    
    public void borrar(Connection conn, String numfactura) throws SQLException {
    String consulta = "DELETE FROM detalle WHERE numfactura = ?";
    PreparedStatement sentencia = conn.prepareStatement(consulta);
    String[] numeros = numfactura.split(":");
    
    for (int i = 0; i < numeros.length; i++) {
        sentencia.setString(1, numeros[i]);
        sentencia.executeUpdate(); 
    }
}

    
    
    
}

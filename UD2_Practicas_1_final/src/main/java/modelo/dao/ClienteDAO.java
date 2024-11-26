/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.vo.Cliente;

/**
 *
 * @author hector.garaboacasas
 */
public class ClienteDAO {

    public static Cliente buscar(Connection conn, String idCliente) throws SQLException {
    Cliente cliente=null;
    ResultSet rs=null;
    String consulta="{CALL BuscarCliente(?)}";
        CallableStatement sentencia=conn.prepareCall(consulta);
        sentencia.setString(1, idCliente);
        rs=sentencia.executeQuery();
        while(rs.next()){
         cliente=new Cliente(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
    }
        return cliente;
    }
    

public void insertar(Connection conn, String id, String nombre, String apellido, String direccion) throws SQLException {

        String consulta="insert into cliente(idcliente, nombrecli, apellidocli, dircli) VALUES(?,?,?,?)";
        PreparedStatement sentencia=conn.prepareStatement(consulta);
        sentencia.setString(1, id);
        sentencia.setString(2, nombre);
        sentencia.setString(3, apellido);
        sentencia.setString(4, direccion);
        sentencia.executeUpdate();
        
        
    }

    public void borrar(Connection conn, String idcliente) throws SQLException {
   
        
         String consulta = "DELETE FROM cliente WHERE idcliente = ?";

        PreparedStatement sentencia = conn.prepareStatement(consulta);

        sentencia.setString(1, idcliente);
        
        sentencia.executeUpdate(); 
    }
}
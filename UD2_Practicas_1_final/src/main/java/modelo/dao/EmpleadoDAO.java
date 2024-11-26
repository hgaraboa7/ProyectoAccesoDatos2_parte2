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
import javax.swing.DefaultComboBoxModel;
import modelo.vo.Empleado;

/**
 *
 * @author hector.garaboacasas
 */
public class EmpleadoDAO {

    public void cargarcombo(Connection conn, DefaultComboBoxModel modelocomboEmpleado) throws SQLException {       
        modelocomboEmpleado.removeAllElements();
        String consulta="select * from empleado";        
        Statement sentencia=conn.createStatement();
        ResultSet rs=sentencia.executeQuery(consulta);        
        while(rs.next()){            
            modelocomboEmpleado.addElement(new Empleado(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5)));
        }            
      }
//sin comprobnar
    public void incentivar(Connection conn, Double precio, Empleado emp) throws SQLException {
        String consulta="update empleado set incentivo=? where idempleado like ? ";
        PreparedStatement sentencia=conn.prepareStatement(consulta);
        sentencia.setDouble(1, (emp.getIncentivo()+precio*0.01));
        sentencia.setString(2, emp.getIdempleado());
        sentencia.executeUpdate();
    }

    public void aumentaroperativa(Connection conn, Empleado emp) throws SQLException {
        String consulta="update empleado set operativas=? where idempleado like ?";
        PreparedStatement sentencia=conn.prepareStatement(consulta);
        sentencia.setInt(1, emp.getOperativas()+1);
        sentencia.setString(2, emp.getIdempleado());
        sentencia.executeUpdate();
        
        
    }
    
}

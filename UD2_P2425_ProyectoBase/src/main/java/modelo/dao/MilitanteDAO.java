/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hector.garaboacasas
 */
public class MilitanteDAO {
   public boolean cargarMilitantesDePartido(Connection conn, DefaultTableModel modelotabla, double cantidadMinima, String ideologia) throws SQLException {
    boolean comp = false;

     modelotabla.setRowCount(0);

    String consulta = """
        SELECT m.Nombre, m.EsPolitico, m.Edad
        FROM Militante m
        JOIN Partido p ON m.IDPartido = p.IDPartido
        JOIN PartidoCasoCorrupcion pc ON p.IDPartido = pc.IDPartido
        JOIN CasoDeCorrupcion c ON pc.IDCaso = c.IDCaso
        WHERE p.Ideologia = ? AND c.CantidadFraude > ?
    """;
    PreparedStatement sentencia = conn.prepareStatement(consulta);
    sentencia.setString(1, ideologia);
    sentencia.setDouble(2, cantidadMinima);

    ResultSet rs = sentencia.executeQuery();

    while (rs.next()) {
        modelotabla.setRowCount(modelotabla.getRowCount() + 1); 

        modelotabla.setValueAt(rs.getString("Nombre"), modelotabla.getRowCount() - 1, 0);
        modelotabla.setValueAt(rs.getBoolean("EsPolitico"), modelotabla.getRowCount() - 1, 1);
        modelotabla.setValueAt(rs.getInt("Edad"), modelotabla.getRowCount() - 1, 2);

        comp = true;
    }

    return comp;
}

    
    
}

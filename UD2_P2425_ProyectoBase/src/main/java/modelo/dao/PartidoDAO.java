/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hector.garaboacasas
 */
public class PartidoDAO {
    
    
    
    
    
    public void alta(Connection conn, String nombre, String ideologia, Date fundacion) throws SQLException {
    String consulta = "INSERT INTO Partido (Nombre, Ideologia, Fundacion, NumeroMilitantes) VALUES (?, ?, ?, 0)";
    try (PreparedStatement sentencia = conn.prepareStatement(consulta)) {
        sentencia.setString(1, nombre);
        sentencia.setString(2, ideologia);
        sentencia.setDate(3, fundacion);
        sentencia.executeUpdate();
    }
}

    public void borrarEnCascada(Connection conn, int idPartido) throws SQLException {
     String consulta1 = "DELETE FROM PartidoCasoCorrupcion WHERE IDPartido = ?";
    PreparedStatement stmt1 = conn.prepareStatement(consulta1);
    stmt1.setInt(1, idPartido);
    stmt1.executeUpdate();

   
    String consulta2 = "DELETE FROM Militante WHERE IDPartido = ?";
    PreparedStatement stmt2 = conn.prepareStatement(consulta2);
    stmt2.setInt(1, idPartido);
    stmt2.executeUpdate();

  
    String consulta3 = "DELETE FROM Partido WHERE IDPartido = ?";
    PreparedStatement stmt3 = conn.prepareStatement(consulta3);
    stmt3.setInt(1, idPartido);
    stmt3.executeUpdate();
}


    public void modificar(Connection conn, int idPartido, String nombre, String ideologia, Date fundacion) throws SQLException {
    String consulta = "UPDATE Partido SET Nombre = ?, Ideologia = ?, Fundacion = ? WHERE IDPartido = ?";
    PreparedStatement sentencia = conn.prepareStatement(consulta);
    sentencia.setString(1, nombre);
    sentencia.setString(2, ideologia);
    sentencia.setDate(3, fundacion);
    sentencia.setInt(4, idPartido);
    sentencia.executeUpdate();
}


  public boolean cargarPartidos(Connection conn, DefaultTableModel modelotabla) throws SQLException {
    boolean comp = false;

     modelotabla.setRowCount(0);

     String consulta = "SELECT IDPartido, Nombre, Ideologia, Fundacion FROM Partido";
    PreparedStatement sentencia = conn.prepareStatement(consulta);

    String consulta2 = "SELECT COUNT(*) AS TotalMilitantes FROM Militante WHERE IDPartido = ?";
    PreparedStatement sentencia2 = conn.prepareStatement(consulta2);

    ResultSet rs = sentencia.executeQuery();
    while (rs.next()) {
        int idPartido = rs.getInt("IDPartido");
        String nombre = rs.getString("Nombre");
        String ideologia = rs.getString("Ideologia");
        Date fundacion = rs.getDate("Fundacion");
        int totalMilitantes = 0;

         sentencia2.setInt(1, idPartido);
        ResultSet rs2 = sentencia2.executeQuery();
        if (rs2.next()) {
            totalMilitantes = rs2.getInt("TotalMilitantes");
        }

        modelotabla.setRowCount(modelotabla.getRowCount() + 1);

        modelotabla.setValueAt(idPartido, modelotabla.getRowCount() - 1, 0);
        modelotabla.setValueAt(nombre, modelotabla.getRowCount() - 1, 1);
        modelotabla.setValueAt(ideologia, modelotabla.getRowCount() - 1, 2);
        modelotabla.setValueAt(fundacion, modelotabla.getRowCount() - 1, 3);
        modelotabla.setValueAt(totalMilitantes, modelotabla.getRowCount() - 1, 4);

        comp = true;
    }

    return comp;
}




//    public Partido buscar(Connection conn, int idPartido) throws SQLException {
//    String consulta = "SELECT IDPartido, Nombre, Ideologia, Fundacion, NumeroMilitantes FROM Partido WHERE IDPartido = ?";
//    PreparedStatement sentencia = conn.prepareStatement(consulta);
//    sentencia.setInt(1, idPartido);
//    ResultSet rs = sentencia.executeQuery();
//
//    if (rs.next()) {
//        Partido partido = new Partido();
//        partido.setIdPartido(rs.getInt("IDPartido"));
//        partido.setNombre(rs.getString("Nombre"));
//        partido.setIdeologia(rs.getString("Ideologia"));
//        partido.setFundacion(rs.getDate("Fundacion"));
//        partido.setNumeroMilitantes(rs.getInt("NumeroMilitantes"));
//        return partido;
//    }
//
//    return null; // Si no se encuentra el partido
//}
public boolean cargarPartidosPorFraudeYMilitantes(Connection conn, DefaultTableModel modelotabla, double cantidadMinima, int militantesMinimos) throws SQLException {
    boolean comp = false;

     modelotabla.setRowCount(0);

     String consulta = """
        SELECT p.IDPartido, p.Nombre, p.Ideologia, p.Fundacion
        FROM Partido p
        JOIN PartidoCasoCorrupcion pc ON p.IDPartido = pc.IDPartido
        JOIN CasoDeCorrupcion c ON pc.IDCaso = c.IDCaso
        GROUP BY p.IDPartido, p.Nombre, p.Ideologia, p.Fundacion
        HAVING SUM(pc.CantidadFraude) > ? AND COUNT((SELECT m.IDMilitante FROM Militante m WHERE m.IDPartido = p.IDPartido)) > ?
    """;
    PreparedStatement sentencia = conn.prepareStatement(consulta);
    sentencia.setDouble(1, cantidadMinima);
    sentencia.setInt(2, militantesMinimos);

    ResultSet rs = sentencia.executeQuery();

    while (rs.next()) {
        modelotabla.setRowCount(modelotabla.getRowCount() + 1); // Añadir nueva fila vacía

         modelotabla.setValueAt(rs.getInt("IDPartido"), modelotabla.getRowCount() - 1, 0);
        modelotabla.setValueAt(rs.getString("Nombre"), modelotabla.getRowCount() - 1, 1);
        modelotabla.setValueAt(rs.getString("Ideologia"), modelotabla.getRowCount() - 1, 2);
        modelotabla.setValueAt(rs.getDate("Fundacion"), modelotabla.getRowCount() - 1, 3);

        comp = true;
    }

    return comp;
}






}

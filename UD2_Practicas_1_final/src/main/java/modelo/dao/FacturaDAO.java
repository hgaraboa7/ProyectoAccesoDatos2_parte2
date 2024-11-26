/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import modelo.vo.Empleado;
import modelo.vo.Factura;

/**
 *
 * @author hector.garaboacasas
 */
public class FacturaDAO {

    public void generar(Connection conn, String numFactura, String idCliente, Empleado empleado, Date date, boolean cobrada, double iva) throws SQLException {
        String consulta = "Insert into factura(numfactura, idcliente, idempleado, fecha, cobrada, iva)values(?,?,?,?,?,?)";
        PreparedStatement sentencia = conn.prepareStatement(consulta);

        sentencia.setString(1, numFactura);
        sentencia.setString(2, idCliente);
        sentencia.setString(3, empleado.getIdempleado());
        sentencia.setDate(4, date);
        sentencia.setBoolean(5, cobrada);
        sentencia.setDouble(6, iva);

        sentencia.executeUpdate();

    }

    //sin probar
    public boolean extraerDatos(Connection conn, String idcliente, DefaultTableModel modelotabla, Date fechaInicio, Date fechaFin) throws SQLException {

        boolean comp = false;

        String consulta = "SELECT numfactura, fecha, cobrada FROM factura WHERE idcliente LIKE ? AND fecha BETWEEN ? AND ?";
        PreparedStatement sentencia = conn.prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        sentencia.setDate(2, fechaInicio);
        sentencia.setDate(3, fechaFin);

        String consulta2 = "select cantidad, precio from detalle where numfactura like ?";
        PreparedStatement sentencia2 = conn.prepareStatement(consulta2);

        ResultSet rs = sentencia.executeQuery();
        while (rs.next()) {
            double precio = 0.0;
            modelotabla.setRowCount(modelotabla.getRowCount() + 1);

            sentencia2.setString(1, rs.getString(1));
            ResultSet rs2 = sentencia2.executeQuery();
            while (rs2.next()) {
                precio += rs2.getInt(1) * rs2.getDouble(2);
            }
            //sin probar
            modelotabla.setValueAt(rs.getString(1), modelotabla.getRowCount() - 1, 0);
            modelotabla.setValueAt(rs.getDate(2), modelotabla.getRowCount() - 1, 1);
            modelotabla.setValueAt(precio, modelotabla.getRowCount() - 1, 2);
            modelotabla.setValueAt(rs.getInt(3), modelotabla.getRowCount() - 1, 3);

            comp = true;

        }

        return comp;

    }

    public String buscar(Connection conn, String idcliente) throws SQLException {

        String consulta = "select * from factura where idcliente like ?";
        String numfactura = null;

        PreparedStatement sentencia = conn.prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        ResultSet rs = null;
        rs = sentencia.executeQuery();

        while (rs.next()) {
            numfactura += rs.getString(1) + ":";
        }

        return numfactura;
    }

    public boolean cobrada(Connection conn, String idcliente) throws SQLException {
        boolean cobrada = false;
        String consulta = "select count(*) from  factura where cobrada=0 and idcliente like ?";
        PreparedStatement sentencia = conn.prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()) {
            if (rs.getInt(1) == 0) {
                return cobrada = true;
            }
        }

        return cobrada;
    }

    public void actualizarCobrada(Connection conn, DefaultTableModel modelotabla, String idcliente) throws SQLException {
        String consulta = "SELECT numfactura FROM factura WHERE idcliente LIKE ? ";
        PreparedStatement sentencia = conn.prepareStatement(consulta);
        sentencia.setString(1, idcliente);

        String consulta2 = "UPDATE factura SET cobrada = ? WHERE numfactura like ?";
        PreparedStatement sentencia2 = conn.prepareStatement(consulta2);

        for (int i = 0; i < modelotabla.getRowCount(); i++) {
            int cobrada = Integer.parseInt(modelotabla.getValueAt(i, 3).toString());

            String numFactura = modelotabla.getValueAt(i, 0).toString();

            if (cobrada == 1) {
                sentencia2.setInt(1, cobrada);
            }
            sentencia2.setString(2, numFactura);

            sentencia2.executeUpdate();
        }
    }

    public void borrar(Connection conn, String idcliente) throws SQLException {

        String consulta = "DELETE FROM factura WHERE idcliente = ?";

        PreparedStatement sentencia = conn.prepareStatement(consulta);

        sentencia.setString(1, idcliente);

        sentencia.executeUpdate();
    }

}

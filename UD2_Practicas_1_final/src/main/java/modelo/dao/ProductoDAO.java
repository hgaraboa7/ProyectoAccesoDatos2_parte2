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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.vo.Producto;

/**
 *
 * @author hector.garaboacasas
 */
public class ProductoDAO {

    public void cargarcombo(Connection conn, DefaultComboBoxModel modelocomboProducto) throws SQLException {

        String consulta = "select * from productos";

        Statement sentencia = conn.createStatement();

        ResultSet rs = sentencia.executeQuery(consulta);
        while (rs.next()) {

            modelocomboProducto.addElement(new Producto(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));

        }

    }

    public void cargartabla(DefaultTableModel modelotabla, Producto producto, int cantidad) {

        modelotabla.setRowCount(modelotabla.getRowCount() + 1);

        modelotabla.setValueAt(producto.getNomproducto(), modelotabla.getRowCount() - 1, 0);
        modelotabla.setValueAt(cantidad, modelotabla.getRowCount() - 1, 1);
        modelotabla.setValueAt(producto.getPrecio() * cantidad, modelotabla.getRowCount() - 1, 2);

    }

    public boolean comprobarTabla(DefaultTableModel modelotabla, Producto producto, int cantidad) {

       
        boolean comp = false;

        for (int i = 0; i < modelotabla.getRowCount(); i++) {

            String nombretabla = (String) modelotabla.getValueAt(i, 0);

            if (nombretabla == producto.getNomproducto()) {

                modelotabla.setValueAt(((int) modelotabla.getValueAt(i, 1) + cantidad), i, 1);

                modelotabla.setValueAt(((int) modelotabla.getValueAt(i, 1) * producto.getPrecio()), i, 2);

                return comp = true;

            }

        }

        return comp;

    }

    public void eliminarProducto(DefaultTableModel modelotabla, Producto producto, int cantidad) {
    for (int i = 0; i < modelotabla.getRowCount(); i++) {
        String nombretabla = (String) modelotabla.getValueAt(i, 0);

       
        if (nombretabla.equals(producto.getNomproducto())) {
            int cantidadActual = (int) modelotabla.getValueAt(i, 1) - cantidad;

             if (cantidadActual <= 0) {
                modelotabla.removeRow(i);
                i--; 
            } else {
                 modelotabla.setValueAt(cantidadActual, i, 1);
                modelotabla.setValueAt(cantidadActual * producto.getPrecio(), i, 2);
            }

            break;
        }
    }
}


    public void calcularTotal(DefaultTableModel modelotabla, JTextField txtTotal) {

        double precio = 0.0;

        for (int i = 0; i < modelotabla.getRowCount(); i++) {

            precio += (double) modelotabla.getValueAt(i, 2);

        }

        txtTotal.setText(String.valueOf(precio));

    }

    public int comprobarStock(Connection conn, DefaultTableModel modelotabla) throws SQLException {

       
        String consulta = "Select stock from productos where nomproducto like ?";
        PreparedStatement sentencia = conn.prepareStatement(consulta);
        int stock = 0;
        ResultSet rs;
        String nomproducto;
        int cantidadproducto;

        for (int i = 0; i < modelotabla.getRowCount(); i++) {
            nomproducto = String.valueOf(modelotabla.getValueAt(i, 0));
            cantidadproducto = (int) modelotabla.getValueAt(i, 1);
            sentencia.setString(1, nomproducto);
            rs = sentencia.executeQuery();
            if (rs.next()) {
                stock = rs.getInt(1);
                if (cantidadproducto <= stock) {
                    return stock;
                } else {
                    return stock = 0;
                }
            }

        }
        return stock;
    }


    public void actualizarStock(Connection conn, DefaultTableModel modelotabla, int stock) throws SQLException {
        String consulta = "UPDATE productos SET stock = ? WHERE nomproducto like ?";
        PreparedStatement sentencia = conn.prepareStatement(consulta);

        //no comprobadpo
//         String select="Select stock from productos where nomproducto like ?";
//         PreparedStatement sentenciaselect=conn.prepareStatement(select);
//         ResultSet rs2;
        String nomproducto;
        int cantidadproducto;

        for (int i = 0; i < modelotabla.getRowCount(); i++) {

            nomproducto = String.valueOf(modelotabla.getValueAt(i, 0));
            cantidadproducto = (int) modelotabla.getValueAt(i, 1);
            sentencia.setInt(1, (stock - cantidadproducto));
            sentencia.setString(2, nomproducto);
            sentencia.executeUpdate();

        }

    }

}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.DAOFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.dao.ClienteDAO;
import modelo.dao.DetalleDAO;
import modelo.dao.EmpleadoDAO;
import modelo.dao.FacturaDAO;
import modelo.dao.ProductoDAO;
import modelo.vo.Cliente;
import modelo.vo.Empleado;
import modelo.vo.Producto;
import vista.Ventana1_1;

/**
 *
 * @author Acceso a datos
 */

public class controlador1_1 {

    public static DAOFactory mySQLFactory;
    //declara los objetos DAO

    public static Ventana1_1 ventana = new Ventana1_1();

    static DefaultComboBoxModel modelocomboEmpleado = new DefaultComboBoxModel();

    static DefaultComboBoxModel modelocomboProducto = new DefaultComboBoxModel();

    static DefaultTableModel modelotabla = new DefaultTableModel();

    static ProductoDAO producto;

    static EmpleadoDAO empleado;

    static FacturaDAO factura;
    
    static ClienteDAO cliente;
    
    static DetalleDAO detalle;

   
   
            
             

    public static void iniciar() {
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);

        ventana.getCmbEmpleado().setModel(modelocomboEmpleado);

        ventana.getCmbProducto().setModel(modelocomboProducto);

        modelotabla = (DefaultTableModel) ventana.getTblProductos().getModel();

    }

    public static void iniciaFactory() {
        mySQLFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        //inicializa los objetos DAO

        producto = mySQLFactory.getProductoDAO();
        empleado = mySQLFactory.getEmpleadoDAO();
        cliente=mySQLFactory.getClienteDAO();
        factura=mySQLFactory.getFacturaDAO();
        detalle=mySQLFactory.getDetalleDAO();

    }

    public static void cerrarFactory() {
        try {
            mySQLFactory.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarcomboProducto() {
        Connection conn = null;
        try {
            conn = mySQLFactory.getConnection();
            producto.cargarcombo(conn, modelocomboProducto);
        } catch (SQLException ex1) {
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (Exception ex) {
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            mySQLFactory.releaseConnection(conn);
        }

    }


    public static void cargarcomboEmpleado() {

        Connection conn = null;

        try {
            conn = mySQLFactory.getConnection();

            empleado.cargarcombo(conn, modelocomboEmpleado);

        } catch (SQLException ex1) {
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (Exception ex) {
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            mySQLFactory.releaseConnection(conn);
        }

    }

    public static void listaañadirproducto() {
        
        if((int) ventana.getSpCantidad().getValue()==0){
            JOptionPane.showMessageDialog(null, "la cantidad debe ser superior a 0");
            return;
        }

        if (!producto.comprobarTabla(modelotabla, (Producto) ventana.getCmbProducto().getSelectedItem(), (int) ventana.getSpCantidad().getValue())) {

            producto.cargartabla(modelotabla, (Producto) ventana.getCmbProducto().getSelectedItem(),
                    (int) ventana.getSpCantidad().getValue());
        }

    }

    public static void listaretirarproducto() {
         if((int) ventana.getSpCantidad().getValue()==0){
          //  JOptionPane.showMessageDialog(null, "Faltan datos");
            return;
        }
         
        
        
            producto.eliminarProducto(modelotabla,(Producto) ventana.getCmbProducto().getSelectedItem(), (int) ventana.getSpCantidad().getValue());
        
    }

    public static void sumartotal() {
        
         if(((int) ventana.getSpCantidad().getValue())==0){
           
            return;
        }
        
        producto.calcularTotal(modelotabla, ventana.getTxtTotal());
        
        
    }

    
    
    public static void comprobarStock() {  
          Connection conn = null;          
          int stock;          
          Savepoint sp=null;   
           Date sqlDate;
        try {
            conn = mySQLFactory.getConnection();
            empleado.aumentaroperativa(conn,(Empleado)ventana.getCmbEmpleado().getSelectedItem());
            
            sp=conn.setSavepoint();
            if(modelotabla.getRowCount()==0){
            JOptionPane.showMessageDialog(null, "No hay productos que facturar");
            return;
            }
            if(ventana.getTxtNumFactura().getText().isEmpty()||
                       ventana.getTxtIdCliente().getText().isEmpty()||
                       ventana.getDcFecha().getDate()==null){
                JOptionPane.showMessageDialog(null, "Faltan datos de la factura");
            return;
            }
               
           sp=conn.setSavepoint();
           
            
           if(( stock=producto.comprobarStock(conn, modelotabla))!=0){
              
               producto.actualizarStock(conn, modelotabla, stock);
               empleado.incentivar(conn, Double.valueOf(ventana.getTxtTotal().getText()), (Empleado)ventana.getCmbEmpleado().getSelectedItem());
               //////////
               factura.generar(conn, ventana.getTxtNumFactura().getText(),
                       ventana.getTxtIdCliente().getText(),
                       (Empleado)ventana.getCmbEmpleado().getSelectedItem(),
                       sqlDate= new Date(ventana.getDcFecha().getDate().getTime()),
                       ventana.getCbCobrada().isSelected(),
                       10.00);
               detalle.detallar(conn, ventana.getTxtNumFactura().getText(), modelotabla);
                JOptionPane.showMessageDialog(null,"stock suficiente, factura realizada");
           }else{  
               JOptionPane.showMessageDialog(null,"stock insuficiente");
           }
       } catch (SQLException ex) {
            switch (ex.getErrorCode()) {
                case 1062 -> {
                    JOptionPane.showMessageDialog(null, "la factura ya existe, cambiar el nº de factura");
                }
                default -> {
                }
            }
            try {
                conn.rollback(sp);
            } catch (SQLException ex1) {
                Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }  catch (Exception ex2) {
            try {
                conn.rollback(sp);
            } catch (SQLException ex) {
                Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex2);
        } finally {
            } try {
                conn.commit();
            } catch (SQLException ex) {
                Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex);
            }
            mySQLFactory.releaseConnection(conn);
        }
        
         
         
       
     
    

  

    public static void buscarCliente() {
        
        Connection conn=null;
        Cliente cli=null;
        
        if(ventana.getTxtIdCliente().getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "falta id Cliente");
            return;
        }
        try {
            conn=mySQLFactory.getConnection();
            cli=cliente.buscar(conn, ventana.getTxtIdCliente().getText());
            if(cli!=null){
            System.out.println(cli.toString());
            ventana.getTxtIdCliente().setText(cli.toString());
            }else{
                JOptionPane.showMessageDialog(null, "no existe ese cliente");
            return;
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
  
  
    }

   
         
        
        
        
    
    
}

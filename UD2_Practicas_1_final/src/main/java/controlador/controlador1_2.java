/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.DAOFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.dao.ClienteDAO;
import modelo.dao.DetalleDAO;
import modelo.dao.EmpleadoDAO;
import modelo.dao.FacturaDAO;
import modelo.dao.HistoricoDAO;
import modelo.dao.ProductoDAO;
import modelo.vo.Cliente;
import modelo.vo.Factura;
import vista.Ventana1_2;

/**
 *
 * @author hector.garaboacasas
 */
public class controlador1_2 {

    public static DAOFactory mySQLFactory;
    public static Ventana1_2 ventana = new Ventana1_2();
    static ProductoDAO producto;
    static EmpleadoDAO empleado;
    static FacturaDAO factura;
    static ClienteDAO cliente;
    static DetalleDAO detalle;
    static HistoricoDAO historico;

    public static void iniciar() {
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);

    }

    public static void iniciaFactory() {
        mySQLFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        //inicializa los objetos DAO

        producto = mySQLFactory.getProductoDAO();
        empleado = mySQLFactory.getEmpleadoDAO();
        cliente = mySQLFactory.getClienteDAO();
        factura = mySQLFactory.getFacturaDAO();
        detalle = mySQLFactory.getDetalleDAO();
        historico = mySQLFactory.getHistoricoDAO();

    }

    public static void cerrarFactory() {
        try {
            mySQLFactory.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void buscarCliente() {
        Connection conn = null;
        Cliente cli = null;

        try {
            conn = mySQLFactory.getConnection();
            cli = cliente.buscar(conn, ventana.getTxtIdCliente().getText());
            if (cli != null) {
                ventana.getTxtNombreCli().setText(cli.getNombrecli());
                ventana.getTxtApellidoCli().setText(cli.getApellidocli());
                ventana.getTxtDirCli().setText(cli.getDircli());

            } else {

                return;
            }

        } catch (Exception ex) {
            Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                conn.commit();
            } catch (SQLException ex) {
                Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            mySQLFactory.releaseConnection(conn);

        }
    }

    public static void insertarCliente() {
        Connection conn = null;

        if (ventana.getTxtIdCliente().getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "falta id cliente");
            return;
        }

        try {
            conn = mySQLFactory.getConnection();
            cliente.insertar(conn,
                    ventana.getTxtIdCliente().getText(),
                    ventana.getTxtNombreCli().getText(),
                    ventana.getTxtApellidoCli().getText(),
                    ventana.getTxtDirCli().getText()
            );
            JOptionPane.showMessageDialog(null, "Insercion realizada con exito");

        } catch (SQLException ex) {
            switch (ex.getErrorCode()) {
                case 1062 -> {
                    JOptionPane.showMessageDialog(null, "el cliente ya existe");
                }
                default -> {
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                conn.commit();
            } catch (SQLException ex) {
                Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            mySQLFactory.releaseConnection(conn);

        }

    }

    public static void enviarCliente() {

        Connection conn = null;
        Cliente cli = null;
        String numfactura = null;
        double total = 0.0;
        if (ventana.getTxtIdCliente().getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "falta id cliente");
            return;
        }

        try {
            conn = mySQLFactory.getConnection();
            if ((cli = cliente.buscar(conn, ventana.getTxtIdCliente().getText())) == null) {
                JOptionPane.showMessageDialog(null, "no existe el cliente");
                return;
                
            } else if (factura.cobrada(conn, ventana.getTxtIdCliente().getText()) == true) {
                 try{
                    cliente.borrar(conn,ventana.getTxtIdCliente().getText());
                    
                    historico.enviarinFacturas(conn, cli.getIdcliente(),
                        cli.getNombrecli() + cli.getApellidocli());
                    }catch (SQLException ex) {
                numfactura = factura.buscar(conn, ventana.getTxtIdCliente().getText());
                System.out.println(numfactura.substring(4, numfactura.length() - 1));
                total = detalle.total(conn, numfactura);
                historico.enviar(conn, cli.getIdcliente(),
                        cli.getNombrecli() + cli.getApellidocli(),
                        total,
                        numfactura);
                //borrar//
                
               
                
                detalle.borrar(conn, numfactura );
                factura.borrar(conn,ventana.getTxtIdCliente().getText() );
                cliente.borrar(conn,ventana.getTxtIdCliente().getText());
                }
            }else{
                
                JOptionPane.showMessageDialog(null, "no tiene todas sus facturas pagadas, no se puede borrar");
                return;
            }

        } catch (SQLException ex) {
            switch (ex.getErrorCode()) {
                case 1062 -> {
                    JOptionPane.showMessageDialog(null, "el cliente ya existe en el historico");
                }
                default -> {
                    Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                conn.commit();
            } catch (SQLException ex) {
                Logger.getLogger(controlador1_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            mySQLFactory.releaseConnection(conn);

        }

    }

   

}

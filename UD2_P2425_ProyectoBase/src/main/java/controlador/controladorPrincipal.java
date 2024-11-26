/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.DAOFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.dao.MilitanteDAO;
import modelo.dao.PartidoDAO;
import vista.Principal;

/**
 *
 * @author Acceso a datos
 */


public class controladorPrincipal {
    
  public static DAOFactory mySQLFactory;
    public static Principal ventana = new Principal();

    static DefaultTableModel modelotabla = new DefaultTableModel();
    static DefaultTableModel modelotablaMilitantes = new DefaultTableModel();
    

    static PartidoDAO partido;
    static MilitanteDAO militante;
//    static CasoCorrupcionDAO casoCorrupcion;
//    static PartidoCasoCorrupcionDAO partidoCaso;

    public static void iniciar() {
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        modelotabla = (DefaultTableModel) ventana.getTblPartidos().getModel();
        modelotablaMilitantes = (DefaultTableModel) ventana.getTblMilitante().getModel();
        
        
    }

    public static void iniciaFactory() {
        mySQLFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

        partido = mySQLFactory.getPartidoDAO();
        militante = mySQLFactory.getMilitanteDAO();
//        casoCorrupcion = mySQLFactory.getCasoCorrupcionDAO();
//        partidoCaso = mySQLFactory.getPartidoCasoCorrupcionDAO();
    }

    public static void cerrarFactory() {
        try {
            mySQLFactory.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Insertar() {
   
         Connection conn = null;
    try {
        conn = mySQLFactory.getConnection();

        String nombre = ventana.getTxtNombre().getText();
        String ideologia = ventana.getTxtIdeologia().getText();
        Date fundacion = ventana.getDcFundacion().getDate() != null
                ? new java.sql.Date(ventana.getDcFundacion().getDate().getTime())
                : null;

        if (nombre.isEmpty() || ideologia.isEmpty() || fundacion == null) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.");
            return;
        }

        partido.alta(conn, nombre, ideologia, fundacion);
        JOptionPane.showMessageDialog(null, "Partido insertado correctamente.");
    } catch (SQLException e) {
        Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
    }   catch (Exception ex) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        mySQLFactory.releaseConnection(conn);
    }
        
    }
    public static void eliminarPartido() {
    Connection conn = null;
    try {
        conn = mySQLFactory.getConnection();
        int idPartido = Integer.parseInt(ventana.getTxtIdPartido().getText());

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este partido? Se eliminarán todos los militantes y casos relacionados.", 
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        partido.borrarEnCascada(conn, idPartido);
        JOptionPane.showMessageDialog(null, "Partido eliminado correctamente.");
    } catch (SQLException e) {
        Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
    } catch (Exception ex) {
          Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        mySQLFactory.releaseConnection(conn);
    }
}
    public static void modificarPartido() {
    Connection conn = null;
    try {
        conn = mySQLFactory.getConnection();
        int idPartido = Integer.parseInt(ventana.getTxtIdPartido().getText());
        String nombre = ventana.getTxtNombre().getText();
        String ideologia = ventana.getTxtIdeologia().getText();
        Date fundacion = ventana.getDcFundacion().getDate() != null
                ? new java.sql.Date(ventana.getDcFundacion().getDate().getTime())
                : null;

        if (nombre.isEmpty() || ideologia.isEmpty() || fundacion == null) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.");
            return;
        }

        partido.modificar(conn, idPartido, nombre, ideologia, fundacion);
        JOptionPane.showMessageDialog(null, "Partido modificado correctamente.");
    } catch (SQLException e) {
        Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
    } catch (Exception ex) {
          Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        mySQLFactory.releaseConnection(conn);
    }
}
    
  public static void cargarPartidosEnTabla() {
    Connection conn = null;
    try {
        conn = mySQLFactory.getConnection();
       
        if (!partido.cargarPartidos(conn, modelotabla)) {
            JOptionPane.showMessageDialog(null, "No se encontraron partidos para mostrar.");
        }
    } catch (SQLException e) {
        Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
    } catch (Exception ex) {
          Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        mySQLFactory.releaseConnection(conn);
    }
}

    
//    public static void buscarPartido() {
//    Connection conn = null;
//    try {
//        conn = mySQLFactory.getConnection();
//        int idPartido = Integer.parseInt(ventana.getTxtIdPartido().getText());
//
//        Partido partidoEncontrado = partido.buscar(conn, idPartido);
//        if (partidoEncontrado != null) {
//            ventana.getTxtNombre().setText(partidoEncontrado.getNombre());
//            ventana.getTxtIdeologia().setText(partidoEncontrado.getIdeologia());
//            ventana.getDcFundacion().setDate(partidoEncontrado.getFundacion());
//            JOptionPane.showMessageDialog(null, "Partido encontrado.");
//        } else {
//            JOptionPane.showMessageDialog(null, "No se encontró un partido con ese ID.");
//        }
//    } catch (SQLException e) {
//        Logger.getLogger(controlador1_1.class.getName()).log(Level.SEVERE, null, e);
//    } finally {
//        mySQLFactory.releaseConnection(conn);
//    }

public static void cargarPartidosPorFraudeYMilitantes() {
    Connection conn = null;
    try {
        conn = mySQLFactory.getConnection();

        double cantidadMinima = Double.parseDouble(ventana.getTxtCantidadMinima().getText());
        int militantesMinimos = Integer.parseInt(ventana.getTxtMilitantesMinimos().getText());

        boolean datosCargados = partido.cargarPartidosPorFraudeYMilitantes(conn, modelotabla, cantidadMinima, militantesMinimos);
        if (!datosCargados) {
            JOptionPane.showMessageDialog(null, "No se encontraron partidos que cumplan los criterios.");
        }
    } catch (SQLException e) {
        Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
    } catch (Exception ex) {
          Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        mySQLFactory.releaseConnection(conn);
    }
}

public static void cargarMilitantesDePartido() {
    Connection conn = null;
    try {
        conn = mySQLFactory.getConnection();

        double cantidadMinima = Double.parseDouble(ventana.getTxtCantidadMinimaBuscar().getText());
        String ideologia = ventana.getTxtIdeologiaBuscar().getText();

        boolean datosCargados = militante.cargarMilitantesDePartido(conn, modelotablaMilitantes, cantidadMinima, ideologia);
        if (!datosCargados) {
            JOptionPane.showMessageDialog(null, "No se encontraron militantes que cumplan los criterios.");
        }
    } catch (SQLException e) {
        Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
    } catch (Exception ex) {
          Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        mySQLFactory.releaseConnection(conn);
    }
}






    
    

}

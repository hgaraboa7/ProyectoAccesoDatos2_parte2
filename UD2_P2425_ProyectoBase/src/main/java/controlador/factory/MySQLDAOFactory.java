package controlador.factory;

import java.sql.Connection;
import java.sql.SQLException;
import controlador.pool.BasicConnectionPool;
import modelo.dao.MilitanteDAO;
import modelo.dao.PartidoDAO;

public class MySQLDAOFactory extends DAOFactory {

    final static String user = "root";
    final static String password = "root";
    final static String BD = "gestionpolitica"; //Indica aqui la BD 
    final static String IP = ""; //Indica aqui la IP 
    final static String url = "jdbc:mysql://" + IP + ":3306/" + BD;

    static BasicConnectionPool bcp;

    public MySQLDAOFactory() {

        try {
            bcp = BasicConnectionPool.create(url, user, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return bcp.getConnection();
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        return bcp.releaseConnection(connection);
    }

    @Override
    public int getSize() {
        return bcp.getSize();
    }
    //add getUser, getURL....

    @Override
    public void shutdown() throws SQLException {
        bcp.shutdown();
    }
    //implementamos los métodos abstractos

    @Override
    public PartidoDAO getPartidoDAO() {
    return new PartidoDAO ();
    }

    @Override
    public MilitanteDAO getMilitanteDAO() {
   return new MilitanteDAO ();
           }

}

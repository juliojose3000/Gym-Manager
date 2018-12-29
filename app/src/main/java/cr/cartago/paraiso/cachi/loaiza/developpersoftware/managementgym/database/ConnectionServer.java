package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;


import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionServer  extends AsyncTask<String, Void, Void> {

    private Connection connection;


    @Override
    protected Void doInBackground(String... params) {

        try {
            createConnection(params[0], params[1], params[2], params[3]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }


    public void createConnection(String user, String password, String database, String server) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //politica de connexion

        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String connectionUrl = null;

        Class.forName ("com.mysql.jdbc.Driver").newInstance ();

        connectionUrl = "jdbc:mysql://"+server+"/"+database;

        connection = DriverManager.getConnection (connectionUrl, user, password);

        setConnection(connection);

    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
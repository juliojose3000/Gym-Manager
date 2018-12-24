package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnectionWithDatabase {

    public CreateConnectionWithDatabase(){

    }

    public Connection createConnection(String user, String password, String database, String server) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //politica de connexion

        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String connectionUrl = null;

        Class.forName ("com.mysql.jdbc.Driver").newInstance ();

        connectionUrl = "jdbc:mysql://"+server+"/"+database;

        connection = DriverManager.getConnection (connectionUrl, user, password);

        return connection;

    }



}

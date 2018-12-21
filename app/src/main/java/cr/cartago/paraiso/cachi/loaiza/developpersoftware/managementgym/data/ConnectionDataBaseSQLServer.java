package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDataBaseSQLServer {

    private String dataBaseName = "AndroidAppJ";

    private CreateConnectionWithDatabase connectionDataBaseSQLServer;

    private Connection connection = null;

    private String notification;

    public ConnectionDataBaseSQLServer(){

        connectionDataBaseSQLServer = new CreateConnectionWithDatabase();

        try {
            connection = connectionDataBaseSQLServer.createConnection("julio@loaiza-server","123Loaiza", "gym_cachi","loaiza-server.mysql.database.azure.com:3306");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            notification = "No se pudo conectar con la base de datos del servidor de ubuntu, por lo que se conectará con la base de datos del servidor espejo";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if(connection==null) {

            return;

        }

        verifyUser();

    }


    public String verifyUserInBD(String username, String password){

            String tableName = "customer";

            String query = "SELECT* FROM "+tableName+" WHERE username="+username+" AND password="+password;

            try {
                //prepara la conexion;'
                Statement statement = connection.createStatement();
                //ejecuta el query
                ResultSet resultSet = statement.executeQuery(query);

                //pregunta si la consulta trajo resultados.
                if(!resultSet.next()){

                    return "No existe el usuario que intenta ingresar, favor revise los datos e intente de nuevo.";

                }else{
                    String query2 = "select name,username,password,incremental_key,r.id_rol,r.rol_name from dbo.Rol r,dbo.Users u where r.id_rol=u.id_rol and\n" +
                            "username="+username+" and password="+password;

                    ResultSet resultSet2 = statement.executeQuery(query2);


                    if(!resultSet2.next()) {

                        return "El usuario no existe en la base de datos";

                    }

                    //retorno el rol del usuario que intente loguearse
                    String rolName = resultSet2.getObject("rol_name").toString();

                    return rolName;

                }

            } catch (SQLException e) {

                return "Error al conectar con la base de datos. Verifique la coneccion.";

            }



    }

    public String verifyUser(){

        String customers="";

        String tableName = "customer";

        String query = "SELECT* FROM "+tableName;

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);



            //pregunta si la consulta trajo resultados.
            while(resultSet.next()){
                customers+= (resultSet.getString("customer_name"));
            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }

}

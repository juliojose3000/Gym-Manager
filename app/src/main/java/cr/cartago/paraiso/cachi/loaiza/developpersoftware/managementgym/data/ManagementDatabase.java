package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public final class ManagementDatabase{

    private CreateConnectionWithDatabase connectionDataBaseSQLServer;

    private Connection connection = null;

    private String notification;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    public static ArrayList<Customer> listAllCustomer;

    public static ArrayList<Customer> listCustomersOfToday;

    public static ArrayList<Customer> listCustomerForAddToday;


    public ManagementDatabase(){

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

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH)+1;

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        listAllCustomer = getAllCustomers();

        listCustomersOfToday = getAllCustomersOfToday(year+"-"+month+"-"+dayOfMonth);

        listCustomerForAddToday = customerForAddToday();

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

    public ArrayList<Customer> getAllCustomers(){

        ArrayList<Customer> customers = new ArrayList<>();

        String tableName = "customer";

        String query = "SELECT* FROM "+tableName;

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            Customer customer;

            //pregunta si la consulta trajo resultados.
            while(resultSet.next()){
                int customerId = resultSet.getInt("customer_id");
                String customerName = resultSet.getString("customer_name");
                String customerLastName = resultSet.getString("customer_lastname");
                String startDate = resultSet.getString("start_date");
                String customerNickname = resultSet.getString("customer_nickname");

                customer = new Customer();

                customer.setCustomerId(customerId);
                customer.setName(customerName);
                customer.setLastName(customerLastName);
                customer.setStarDate(Date.valueOf(startDate));
                customer.setNickname(customerNickname);

                customers.add(customer);


            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }

    //Este metodo carga solo los clientes que no han llegado hoy
    /*public ArrayList<Customer> addCustomerToday(String today){

        ArrayList<Customer> customers = new ArrayList<>();

        String tableName = "customer";

        String query = "SELECT* FROM "+tableName;

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            Customer customer;

            //pregunta si la consulta trajo resultados.
            while(resultSet.next()){
                int customerId = resultSet.getInt("customer_id");

                //Si el cliente ya llego no se carga en la lista general
                if(!thisCustomerArrivedToday(customerId, today)){
                    String customerName = resultSet.getString("customer_name");
                    String customerLastName = resultSet.getString("customer_lastname");
                    String startDate = resultSet.getString("start_date");
                    String customerNickname = resultSet.getString("customer_nickname");

                    customer = new Customer();

                    customer.setCustomerId(customerId);
                    customer.setName(customerName);
                    customer.setLastName(customerLastName);
                    customer.setStarDate(Date.valueOf(startDate));
                    customer.setNickname(customerNickname);

                    customers.add(customer);
                }

            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }*/
    public ArrayList<Customer> customerForAddToday(){

        ArrayList<Customer> listCustomersForAddToday = new ArrayList<>();

        for (Customer customer:
             listAllCustomer) {
            if(!contains(customer)){
                listCustomersForAddToday.add(customer);
            }
        }

        return listCustomersForAddToday;

    }

    private boolean contains(Customer customerToSearh){

        for (Customer customer:
             listCustomersOfToday) {
                if(customer.getCustomerId()==customerToSearh.getCustomerId()){
                    return true;
                }
        }
        return false;

    }

    public boolean insertCustomer(String customerName, String customerLastname, String customerNickname, String customerStartdate){

        String query = "INSERT INTO customer(customer_name,customer_lastname,start_date, customer_nickname) VALUES ('"+customerName+"','"+customerLastname+"','"+customerStartdate+"','"+customerNickname+"');";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            int rowsAffeted = statement.executeUpdate(query);

            if(rowsAffeted==1){//significa que hubo un cambio en la base de datos
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean insertCustomersOfToday(int customerId, String today){

        String query = "INSERT INTO customertoday(customer_id, date_arrived) values ("+customerId+",'"+today+"');";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            int rowsAffeted = statement.executeUpdate(query);

            if(rowsAffeted==1){//significa que hubo un cambio en la base de datos
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public ArrayList<Customer> getAllCustomersOfToday(String today){

        ArrayList<Customer> customers = new ArrayList<>();

        String tableName = "customertoday";

        String query = "SELECT* FROM "+tableName+" WHERE date_arrived='"+today+"'";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            Customer customer;

            //pregunta si la consulta trajo resultados.
            while(resultSet.next()){
                int customerId = resultSet.getInt("customer_id");

                String query2 = "SELECT* FROM customer WHERE customer_id = "+customerId+";";

                //prepara la conexion;'
                Statement statement2 = connection.createStatement();
                //ejecuta el query
                ResultSet resultSet2 = statement2.executeQuery(query2);

                if(resultSet2.next()){

                    String customerName = resultSet2.getString("customer_name");
                    String customerLastName = resultSet2.getString("customer_lastname");
                    String startDate = resultSet2.getString("start_date");
                    String customerNickname = resultSet2.getString("customer_nickname");

                    customer = new Customer();

                    customer.setCustomerId(customerId);
                    customer.setName(customerName);
                    customer.setLastName(customerLastName);
                    customer.setStarDate(Date.valueOf(startDate));
                    customer.setNickname(customerNickname);

                    customers.add(customer);

                }


            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }

    //Me dice si hoy a llegado el cliente con el id tal
    public boolean thisCustomerArrivedToday(int customerId, String today){

        String tableName = "customertoday";

        String query = "SELECT* FROM "+tableName+" WHERE customer_id="+customerId+" AND date_arrived='"+today+"'";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);


            //pregunta si la consulta trajo resultados.
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return false;

    }

}

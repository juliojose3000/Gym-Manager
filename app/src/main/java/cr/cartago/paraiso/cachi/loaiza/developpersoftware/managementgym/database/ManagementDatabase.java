package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;

public class ManagementDatabase{

    private static CreateConnectionWithDatabase createConnectionWithDatabase;

    public static Connection connection = null;

    private String notification;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    public static ArrayList<Customer> listAllCustomer;

    public static ArrayList<Customer> listCustomersOfToday;

    public static ArrayList<Customer> listCustomerForAddToday;

    public static ArrayList<Customer> listAllDefaulterCustomers;

    public static ArrayList<Customer> listAllCustomerWithCurrentPayment;

    public static ArrayList<Payment> listCustomersWithPaymentStillInForse;


    public ManagementDatabase(){

        createConnectionWithDatabase = new CreateConnectionWithDatabase();

        try {
            connection = createConnectionWithDatabase.createConnection("julio@loaiza-server","123Loaiza", "gym_cachi","loaiza-server.mysql.database.azure.com:3306");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            notification = "Hubo un problema al connectarse con la base de datos. Verifique su conexión a internet o contacte a su técnico";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

    public String getNotification() {
        return notification;
    }

    public void fillAllList(){

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH)+1;

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        listAllCustomer = getAllCustomers();

        listCustomersOfToday = getAllCustomersOfToday(year+"-"+month+"-"+dayOfMonth);

        listCustomerForAddToday = CustomerData.customerForAddToday();

        listAllDefaulterCustomers = getAllDefaulterCustomers();

        listAllCustomerWithCurrentPayment = getAllCustomersWithCurrentPayment(year+"-"+month+"-"+dayOfMonth);

        listCustomersWithPaymentStillInForse = getListCustomersWithPaymentStillInForse(year+"-"+month+"-"+dayOfMonth);

    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public boolean theConnectionIsClose() throws SQLException {
        return this.connection.isClosed();
    }

    public static void createNewConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        connection = createConnectionWithDatabase.createConnection("julio@loaiza-server","123Loaiza", "gym_cachi","loaiza-server.mysql.database.azure.com:3306");
    }

    public static void setConnection(Connection newNonnection){
        connection = newNonnection;
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

    public boolean verifyUser(String username, String password){

        String query = "SELECT EXISTS(SELECT * FROM partner WHERE partner_username='"+username+"' AND partner_password='"+password+"') exists_partner;";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //pregunta si la consulta trajo resultados.
            if(resultSet.next()){
                return resultSet.getInt("exists_partner")==1;
            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return false;

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

    public static boolean insertCustomer(String customerName, String customerLastname, String customerNickname, String customerStartdate){

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

    public static boolean insertCustomersOfToday(int customerId, String today){

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

                    customer = new Customer(customerName, customerLastName, customerNickname, Date.valueOf(startDate));

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

    public static boolean addPaymentFromCustomer(int customerId, String datePayment, String endDatePayment, String amuntTime) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        String query = "INSERT INTO customer_pay(customer_id, pay_date, pay_end, amount_time) VALUES ("+customerId+",'"+datePayment+"','"+endDatePayment+"','"+amuntTime+"');";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            int rowsAffeted = statement.executeUpdate(query);

            //connection.close();

            if(rowsAffeted==1){//significa que hubo un cambio en la base de datos
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public ArrayList<Customer> getAllDefaulterCustomers(){

        ArrayList<Customer> customers = new ArrayList<>();

        String query = "SELECT d.customer_id, customer_name, start_date, customer_lastname, customer_nickname, COUNT(*) days_to_pay\n" +
                "FROM defaulter_customer d, customer c \n" +
                "WHERE d.customer_id = c.customer_id     \n" +
                "GROUP BY customer_id;";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            Customer customer = new Customer();

            //pregunta si la consulta trajo resultados.
            while(resultSet.next()){
                int customerId = resultSet.getInt("customer_id");
                String customerName = resultSet.getString("customer_name");
                String customerLastName = resultSet.getString("customer_lastname");
                String startDate = resultSet.getString("start_date");
                String customerNickname = resultSet.getString("customer_nickname");
                int daysToPay = resultSet.getInt("days_to_pay");

                customer = new Customer();

                customer.setCustomerId(customerId);
                customer.setName(customerName);
                customer.setLastName(customerLastName);
                customer.setStarDate(Date.valueOf(startDate));
                customer.setNickname(customerNickname);
                customer.setDaysToPay(daysToPay);

                customers.add(customer);

            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }

    public static Payment lastCustomerPayment(int customerId){

        Payment lastCustomerPayment = new Payment();

        String query = "SELECT MAX(customer_pay_id) last_customer_payment_id from customer_pay WHERE customer_id="+customerId+";";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){

                int lastCustomerPaymentId = resultSet.getInt("last_customer_payment_id");

                if(lastCustomerPaymentId==0){

                    lastCustomerPayment.setPaymentId(lastCustomerPaymentId);
                    lastCustomerPayment.setCustomerId(customerId);
                    lastCustomerPayment.setPayDateStart("0000-00-00");
                    lastCustomerPayment.setPayDateEnd("0000-00-00");
                    lastCustomerPayment.setAmuntTime("");

                }else {

                    String query2 = "SELECT* FROM customer_pay WHERE customer_pay_id=" + lastCustomerPaymentId + ";";

                    //prepara la conexion;'
                    Statement statement2 = connection.createStatement();
                    //ejecuta el query
                    ResultSet resultSet2 = statement2.executeQuery(query2);

                    if (resultSet2.next()) {

                        String datePayment = resultSet2.getString("pay_date");
                        String endDatePayment = resultSet2.getString("pay_end");
                        String amuntTime = resultSet2.getString("amount_time");

                        lastCustomerPayment.setPaymentId(lastCustomerPaymentId);
                        lastCustomerPayment.setCustomerId(customerId);
                        lastCustomerPayment.setPayDateStart(datePayment);
                        lastCustomerPayment.setPayDateEnd(endDatePayment);
                        lastCustomerPayment.setAmuntTime(amuntTime);

                    }

                }

            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return lastCustomerPayment;

    }

    public static boolean addCustomerDefaulter(int customerId, String date){

        String query = "INSERT INTO defaulter_customer(customer_id, arrived_date) VALUES ("+customerId+",'"+date+"');";

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

    public ArrayList<Customer> getAllCustomersWithCurrentPayment(String today){

        ArrayList<Customer> customers = new ArrayList<>();

        String query = "SELECT* FROM customer_pay WHERE pay_end>='"+today+"';";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            Customer customer;

            //pregunta si la consulta trajo resultados.

            while(resultSet.next()){

                int customerId = resultSet.getInt("customer_id");

                customer = CustomerData.getCustomerById(customerId);

                customers.add(customer);

            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }

    public boolean updateTheDefaulterCustomer(int idCliente, String date){

        String query = "DELETE FROM defaulter_customer WHERE arrived_date>='"+date+"' AND customer_id="+idCliente+";";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            int rowsAffeted = statement.executeUpdate(query);

            if(rowsAffeted!=0){//significa que hubo un cambio en la base de datos
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static ArrayList<String> getListAllBillToPay(int idCliente){

        ArrayList<String> billsToPay = new ArrayList<>();

        String query = "SELECT* FROM defaulter_customer WHERE customer_id="+idCliente+";";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //pregunta si la consulta trajo resultados.

            while(resultSet.next()){

                String dateArrived = resultSet.getString("arrived_date");

                String day = CustomerData.getDayName(dateArrived);

                billsToPay.add(day+", "+CustomerData.getDateForShowUser(dateArrived));

            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return billsToPay;

    }

    public boolean cancelBillToPay(int idCliente, String date){

        String query = "DELETE FROM defaulter_customer WHERE customer_id="+idCliente+" AND arrived_date='"+date+"';";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            int rowsAffeted = statement.executeUpdate(query);

            if(rowsAffeted!=0){//significa que hubo un cambio en la base de datos
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean cancelAllBillToPay(int idCliente){

        String query = "DELETE FROM defaulter_customer WHERE customer_id="+idCliente+";";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            int rowsAffeted = statement.executeUpdate(query);

            if(rowsAffeted!=0){//significa que hubo un cambio en la base de datos
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public ArrayList<Customer> customerInSpecificDate(String date){

        ArrayList<Customer> customers = new ArrayList<>();

        String query = "SELECT* FROM customertoday WHERE date_arrived='"+date+"';";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            Customer customer;

            //pregunta si la consulta trajo resultados.
            while(resultSet.next()){

                int customerId = resultSet.getInt("customer_id");

                customers.add(CustomerData.getCustomerById(customerId));


            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return customers;

    }

    public ArrayList<Payment> getListCustomersWithPaymentStillInForse(String date){

        ArrayList<Payment> listCustomersWithPaymentStillInForse = new ArrayList<>();

        String query = "SELECT* FROM customer_pay WHERE pay_end>='"+date+"';";

        try {
            //prepara la conexion;'
            Statement statement = connection.createStatement();
            //ejecuta el query
            ResultSet resultSet = statement.executeQuery(query);

            //pregunta si la consulta trajo resultados.

            Payment payment;

            while(resultSet.next()){

                payment = new Payment();

                int paymentId  = resultSet.getInt("customer_pay_id");
                int customerId = resultSet.getInt("customer_id");
                String payDateStart = resultSet.getString("pay_date");
                String payDateEnd = resultSet.getString("pay_end");
                String amuntTime = resultSet.getString("amount_time");

                payment.setPaymentId(paymentId);
                payment.setCustomerId(customerId);
                payment.setPayDateStart(payDateStart);
                payment.setPayDateEnd(payDateEnd);
                payment.setAmuntTime(amuntTime);

                listCustomersWithPaymentStillInForse.add(payment);

            }

        } catch (SQLException e) {

            String msj =  "Error al conectar con la base de datos. Verifique la coneccion.";

        }

        return listCustomersWithPaymentStillInForse;

    }


}

package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

public class DBCustomerPay {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static String SERVER_URL = DBHelper.REST_API_PHP_URL+"customer_pay/";
    public static final String UI_UPDATE_BROADCAST = "com.example.jay.syncdemo.ui_update_broadcast";

    public static final String DB_NAME = "GymCachi";
    public static final String TABLE_NAME = "customer_pay";

    public static String URL_Read(){
        return DBHelper.REST_API_PHP_URL+"customer_pay/read.php";
    }

    public static String URL_Create(){
        return DBHelper.REST_API_PHP_URL+"customer_pay/create.php";
    }

    public static String URL_Customers_With_Current_Payment(){
        return DBHelper.REST_API_PHP_URL+"customer_pay/with_current_payment.php";
    }

    public static String URL_Delete_Payment(){
        return DBHelper.REST_API_PHP_URL+"customer_pay/delete.php";
    }
}

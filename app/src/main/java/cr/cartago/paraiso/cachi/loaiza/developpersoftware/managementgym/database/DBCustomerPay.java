package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

public class DBCustomerPay {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static final String SERVER_URL = DBHelper.REST_API_PHP_URL+"customer_pay/";
    public static final String UI_UPDATE_BROADCAST = "com.example.jay.syncdemo.ui_update_broadcast";

    public static final String DB_NAME = "GymCachi";
    public static final String TABLE_NAME = "customer_pay";

    public static String URL_Read(){
        return SERVER_URL+"read.php";
    }

}

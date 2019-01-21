package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

public class DBDefaulterCustomer {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static final String SERVER_URL = DBHelper.REST_API_PHP_URL+"defaulter_customer/";
    public static final String UI_UPDATE_BROADCAST = "com.example.jay.syncdemo.ui_update_broadcast";

    public static final String DB_NAME = "GymCachi";
    public static final String TABLE_NAME = "defaulter_customer";

    public static String URL_Read(){
        return SERVER_URL+"read.php";
    }

    public static String URL_Create(){
        return SERVER_URL+"create.php";
    }

}

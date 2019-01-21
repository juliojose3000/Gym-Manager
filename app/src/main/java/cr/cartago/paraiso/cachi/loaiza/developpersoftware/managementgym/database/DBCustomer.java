package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

/**
 * Created by Jay on 06-06-2017.
 */

public class DBCustomer {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static final String SERVER_URL = DBHelper.REST_API_PHP_URL+"customer/";
    public static final String UI_UPDATE_BROADCAST = "com.example.jay.syncdemo.ui_update_broadcast";

    public static final String DB_NAME = "GymCachi";
    public static final String TABLE_NAME = "customer";

    //colums
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_LASTNAME = "customer_lastname";
    public static final String START_DATE = "start_date";
    public static final String CUSTOMER_NICKNAME = "customer_nickname";

    public static String URL_Read(){
        return SERVER_URL+"read.php";
    }

    public static String URL_Create(){
        return SERVER_URL+"create.php";
    }


}

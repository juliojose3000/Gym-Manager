package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

public class DBCustomerToday {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static String SERVER_URL = DBHelper.REST_API_PHP_URL+"customertoday/";
    public static final String UI_UPDATE_BROADCAST = "com.example.jay.syncdemo.ui_update_broadcast";

    public static final String DB_NAME = "GymCachi";
    public static final String TABLE_NAME = "customertoday";

    public static String URL_In_Specific_Date(){
        return DBHelper.REST_API_PHP_URL+"customertoday/in_specific_date.php";
    }

    public static String URL_Create(){
        return DBHelper.REST_API_PHP_URL+"customertoday/create.php";
    }


}

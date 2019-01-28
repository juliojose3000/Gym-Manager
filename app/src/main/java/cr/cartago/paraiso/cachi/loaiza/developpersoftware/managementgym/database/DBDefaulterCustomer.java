package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

public class DBDefaulterCustomer {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

    public static String SERVER_URL = DBHelper.REST_API_PHP_URL+"defaulter_customer/";
    public static final String UI_UPDATE_BROADCAST = "com.example.jay.syncdemo.ui_update_broadcast";

    public static final String DB_NAME = "GymCachi";
    public static final String TABLE_NAME = "defaulter_customer";

    public static String URL_Create(){
        return DBHelper.REST_API_PHP_URL+"defaulter_customer/create.php";
    }

    public static String URL_Bill_To_Pay(){
        return DBHelper.REST_API_PHP_URL+"defaulter_customer/bill_to_pay.php";
    }

    public static String URL_All_Defaulters(){
        return DBHelper.REST_API_PHP_URL+"defaulter_customer/all_defaulters.php";
    }

    public static String URL_Cancel_Bill_To_Pay(){
        return DBHelper.REST_API_PHP_URL+"defaulter_customer/cancel_bill_to_pay.php";
    }

    public static String URL_All_Cancel_Bill_To_Pay(){
        return DBHelper.REST_API_PHP_URL+"defaulter_customer/cancel_all_bill_to_pay.php";
    }

    public static String URL_Delete_Days_Covered_By_Payment(){
        return DBHelper.REST_API_PHP_URL+"defaulter_customer/delete.php";
    }





}

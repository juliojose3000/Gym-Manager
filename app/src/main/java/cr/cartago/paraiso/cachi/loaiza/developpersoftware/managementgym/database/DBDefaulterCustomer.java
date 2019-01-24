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

    public static String URL_Bill_To_Pay(){
        return SERVER_URL+"bill_to_pay.php";
    }

    public static String URL_All_Defaulters(){
        return SERVER_URL+"all_defaulters.php";
    }

    public static String URL_Cancel_Bill_To_Pay(){
        return SERVER_URL+"cancel_bill_to_pay.php";
    }

    public static String URL_All_Cancel_Bill_To_Pay(){
        return SERVER_URL+"cancel_all_bill_to_pay.php";
    }

    public static String URL_Delete_Days_Covered_By_Payment(){
        return SERVER_URL+"delete.php";
    }





}

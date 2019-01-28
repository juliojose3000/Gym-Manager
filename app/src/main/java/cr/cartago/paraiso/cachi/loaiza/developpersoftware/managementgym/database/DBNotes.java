package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

public class DBNotes {

    public static String SERVER_URL = DBHelper.REST_API_PHP_URL+"note/";

    public static String URL_Read(){
        return SERVER_URL+"read.php";
    }

    public static String URL_Create(){
        return SERVER_URL+"create.php";
    }

}

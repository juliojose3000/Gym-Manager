package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Dates;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Note;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Partner;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;

/**
 * Created by Jay on 06-06-2017.
 */

public class DBHelper  {

    //public static String REST_API_PHP_URL = "http://192.168.1.6/proyectos_web/AppGym/CachiFitnessCenter%20REST%20API%20PHP/php_rest_cachi_fitness_center/api/";
    public static String REST_API_PHP_URL = "https://loaiza.000webhostapp.com/REST_API_PHP_CACHI_FITNESS_CENTER/REST_API/api/";
    //public static String REST_API_PHP_URL = "http://loaiza.hostingerapp.com/REST_API_PHP_CACHI_FITNESS_CENTER/REST_API/api/";

    public static ArrayList<Customer> CUSTOMERS;
    public static ArrayList<Customer> CUSTOMERS_TODAY;
    public static ArrayList<Customer> CUSTOMERS_FOR_ADD_TODAY;
    public static ArrayList<Customer> CUSTOMERS_WITH_CURRENT_PAYMENT;
    public static ArrayList<Customer> CUSTOMERS_DEFAULTERS;
    public static ArrayList<Payment>  CUSTOMER_PAYMENTS;
    public static ArrayList<Partner>  PARTNERS;
    public static ArrayList<Note>     NOTES;

    private Dates date;


    public DBHelper(){
        date = new Dates();

        try {
            getAllPartners();
            getAllCustomers();
            getAllCustomersWithCurrentPayment(date.getDateOfToday());
            getAllCustomersToday(date.getDateOfToday());
            getAllCustomersDefaulters();
            getCustomerForAddToday();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getAllPartners() throws JSONException {

        PARTNERS = new ArrayList<>();

        Map<String, String> params = new HashMap<>();

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(REST_API_PHP_URL+"partner/read.php", params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("partner_id");
            String name = jsonObject.getString("partner_name");
            String lastname = jsonObject.getString("partner_lastname");
            String username = jsonObject.getString("partner_username");
            String password = jsonObject.getString("partner_password");

            Partner partner = new Partner(id, name, lastname, username, password);

            PARTNERS.add(partner);

        }
    }

    public static void getAllCustomers() throws JSONException {

        CUSTOMERS = new ArrayList<>();

        JSONObject jsonObject;

        Customer customer;

        Map<String, String> params = new HashMap<>();

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBCustomer.URL_Read(), params);

        if(jsonArray==null){
            return;
        }

        for(int i=0; i<jsonArray.length(); i++)
        {
            jsonObject=jsonArray.getJSONObject(i);
            int customerId = jsonObject.getInt("customer_id");
            String customerName = jsonObject.getString("customer_name");
            String customerLastname = jsonObject.getString("customer_lastname");
            String startDate = jsonObject.getString("start_date");
            String customerNickname = jsonObject.getString("customer_nickname");
            String phoneNumber = jsonObject.getString("phone_number");

            customer = new Customer(customerId, customerName, customerLastname, customerNickname, startDate, phoneNumber);

            CUSTOMERS.add(customer);

        }
        CUSTOMERS = CustomerData.sortAlphabeticallyList(CUSTOMERS);
    }

    public void getAllCustomersToday(String date) throws JSONException {

        CUSTOMERS_TODAY = new ArrayList<>();

        Map<String, String> params = new HashMap<>();

        params.put("date_arrived",date);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBCustomerToday.URL_In_Specific_Date(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);

            int customerId = jsonObject.getInt("customer_id");

            CUSTOMERS_TODAY.add(CustomerData.getCustomerById(customerId));

        }
    }

    public void getAllCustomersWithCurrentPayment(String date) throws JSONException {

        CUSTOMER_PAYMENTS = new ArrayList<>();

        CUSTOMERS_WITH_CURRENT_PAYMENT = new ArrayList<>();

        Map<String, String> params = new HashMap<>();

        params.put("pay_end",date);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBCustomerPay.URL_Customers_With_Current_Payment(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            int customerPayId = jsonObject.getInt("customer_pay_id");
            int customerId = jsonObject.getInt("customer_id");
            String startPay = jsonObject.getString("pay_date");
            String endPay = jsonObject.getString("pay_end");
            String amountTime = jsonObject.getString("amount_time");

            CUSTOMER_PAYMENTS.add(new Payment(customerPayId, customerId, startPay, endPay, amountTime));


            CUSTOMERS_WITH_CURRENT_PAYMENT.add(CustomerData.getCustomerById(customerId));

        }

    }

    public static int insertCustomer(String name, String lastname, String dateStart, String nickname, String phoneNumber){

        int code = 0;

        Map<String, String> params = new HashMap<>();

        params.put("customer_name",name);
        params.put("customer_lastname",lastname);
        params.put("start_date",dateStart);
        params.put("customer_nickname",nickname);
        params.put("phone_number",phoneNumber);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            code = httpJsonParser.sendJson(DBCustomer.URL_Create(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static void insertCustomersOfToday(int customerId,String today){

        Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);
        params.put("date_arrived",today);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            httpJsonParser.sendJson(DBCustomerToday.URL_Create(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getAllCustomersDefaulters() throws JSONException {

        CUSTOMERS_DEFAULTERS = new ArrayList<>();

        Map<String, String> params = new HashMap<>();

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBDefaulterCustomer.URL_All_Defaulters(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);

            int customerId = jsonObject.getInt("customer_id");

            Customer customer = CustomerData.getCustomerById(customerId);

            customer.setDaysToPay(jsonObject.getInt("days_to_pay"));

            CUSTOMERS_DEFAULTERS.add(customer);

        }
    }

    public static void insertCustomerDefaulter(int customerId, String date){

        Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);
        params.put("arrived_date",date);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            httpJsonParser.sendJson(DBDefaulterCustomer.URL_Create(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getCustomerForAddToday(){

        CUSTOMERS_FOR_ADD_TODAY = new ArrayList<>();

        for (Customer customer:
                DBHelper.CUSTOMERS) {
            if(!CustomerData.existsCustomerInCustomerOfToday(customer)){
                CUSTOMERS_FOR_ADD_TODAY.add(customer);
            }
        }

        CUSTOMERS_FOR_ADD_TODAY = CustomerData.sortAlphabeticallyList(CUSTOMERS_FOR_ADD_TODAY);

    }

    public static void addPaymentFromCustomer(int customerId,String startDate,String endDate,String duracion){

        final Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);
        params.put("pay_date",startDate);
        params.put("pay_end",endDate);
        params.put("amount_time",duracion);

        HttpJsonParser httpJsonParser = new HttpJsonParser();
        try {
            httpJsonParser.sendJson(DBCustomerPay.URL_Create(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void deleteDaysForPayByCoveredPay(int customerId,String startDate){

        final Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);
        params.put("arrived_date",startDate);

        HttpJsonParser httpJsonParser = new HttpJsonParser();
        try {
            httpJsonParser.sendJson(DBDefaulterCustomer.URL_Delete_Days_Covered_By_Payment(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static ArrayList<String> getListAllBillToPay(int customerId) throws JSONException {

        ArrayList<String> listBillToPayOfCustomer = new ArrayList<>();

        Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBDefaulterCustomer.URL_Bill_To_Pay(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            String arrivedDate = jsonObject.getString("arrived_date");

            listBillToPayOfCustomer.add(Dates.getDateForShowUser(arrivedDate));

        }

        return listBillToPayOfCustomer;

    }

    public static void cancelBillToPay(int customerId, String date){

        Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);
        params.put("arrived_date",date);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            httpJsonParser.sendJson(DBDefaulterCustomer.URL_Cancel_Bill_To_Pay(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void cancelAllBillToPay(int customerId){

        Map<String, String> params = new HashMap<>();

        params.put("customer_id",""+customerId);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            httpJsonParser.sendJson(DBDefaulterCustomer.URL_All_Cancel_Bill_To_Pay(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Customer> customerInSpecificDate(String date) throws JSONException {

        ArrayList<Customer> listCustomer = new ArrayList<>();

        Map<String, String> params = new HashMap<>();

        params.put("date_arrived",date);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBCustomerToday.URL_In_Specific_Date(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);

            int customerId = jsonObject.getInt("customer_id");

            listCustomer.add(CustomerData.getCustomerById(customerId));

        }

        return listCustomer;

    }

    public static void getAllNotes() throws JSONException {

        NOTES = new ArrayList<>();

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBNotes.URL_Read(), new HashMap<String, String>());

        if(jsonArray==null){
            return;
        }

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String title = jsonObject.getString("title");
            String body = jsonObject.getString("body");

            Note note = new Note(id, title, body);

            NOTES.add(note);

        }

    }

    public static void deletePayment(String customerPayId){

        Map<String, String> params = new HashMap<>();

        params.put("customer_pay_id",customerPayId);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            httpJsonParser.sendJson(DBCustomerPay.URL_Delete_Payment(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
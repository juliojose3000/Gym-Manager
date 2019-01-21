package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Date;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.CustomerPay;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.CustomerToday;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.DefaulterCustomer;

/**
 * Created by Jay on 06-06-2017.
 */

public class DBHelper  {

    public static String REST_API_PHP_URL = "http://192.168.1.6/proyectos_web/AppGym/CachiFitnessCenter%20REST%20API%20PHP/php_rest_cachi_fitness_center/api/";

    public static ArrayList<Customer> CUSTOMERS;
    public static ArrayList<Customer> CUSTOMERS_TODAY;
    public static ArrayList<Customer> CUSTOMERS_FOR_ADD_TODAY;
    public static ArrayList<Customer> CUSTOMERS_WITH_CURRENT_PAYMENT;
    public static ArrayList<Customer> CUSTOMERS_DEFAULTERS;

    private Date date;


    public DBHelper(){
        CUSTOMERS = new ArrayList<>();
        CUSTOMERS_TODAY = new ArrayList<>();
        CUSTOMERS_FOR_ADD_TODAY = new ArrayList<>();
        CUSTOMERS_WITH_CURRENT_PAYMENT = new ArrayList<>();
        CUSTOMERS_DEFAULTERS = new ArrayList<>();

        date = new Date();

        try {
            getAllCustomers();
            getAllCustomersToday(date.getDateOfToday());
            getAllCustomersWithCurrentPayment(date.getDateOfToday());
            getAllCustomersDefaulters();
            getCustomerForAddToday();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllCustomers() throws JSONException {

        JSONObject jsonObject;

        Customer customer;

        Map<String, String> params = new HashMap<>();

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBCustomer.URL_Read(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            jsonObject=jsonArray.getJSONObject(i);
            int customerId = jsonObject.getInt("customer_id");
            String customerName = jsonObject.getString("customer_name");
            String customerLastname = jsonObject.getString("customer_lastname");
            String startDate = jsonObject.getString("start_date");
            String customerNickname = jsonObject.getString("customer_nickname");

            customer = new Customer(customerId, customerName, customerLastname, startDate, customerNickname);

            CUSTOMERS.add(customer);

        }
    }

    public void getAllCustomersToday(String date) throws JSONException {

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

        Map<String, String> params = new HashMap<>();

        params.put("date_arrived",date);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBCustomerPay.URL_Read(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            int customerId = jsonObject.getInt("customer_id");
            String customerName = jsonObject.getString("customer_name");
            String customerLastname = jsonObject.getString("customer_lastname");
            String startDate = jsonObject.getString("start_date");
            String customerNickname = jsonObject.getString("customer_nickname");

            Customer customer = new Customer(customerId, customerName, customerLastname, startDate, customerNickname);

            CUSTOMERS_WITH_CURRENT_PAYMENT.add(customer);

        }

    }

    public static void insertCustomer(String name, String lastname, String dateStart, String nickname){

        Map<String, String> params = new HashMap<>();

        params.put("customer_name",name);
        params.put("customer_lastname",lastname);
        params.put("start_date",dateStart);
        params.put("customer_nickname",nickname);

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        //httpJsonParser.makeHttpRequest(DBCustomer.URL_Create(), "POST", params);
        try {
            httpJsonParser.sendJson(DBCustomer.URL_Create(), params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public void getAllCustomersDefaulters() throws JSONException {

        Map<String, String> params = new HashMap<>();

        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray =  httpJsonParser.getJson(DBDefaulterCustomer.URL_Read(), params);

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);

            int customerId = jsonObject.getInt("customer_id");

            CUSTOMERS_DEFAULTERS.add(CustomerData.getCustomerById(customerId));

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

        for (Customer customer:
                DBHelper.CUSTOMERS) {
            if(!CustomerData.existsCustomerInCustomerOfToday(customer)){
                CUSTOMERS_FOR_ADD_TODAY.add(customer);
            }
        }

    }


}
package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;


import android.os.AsyncTask;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Partner;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;

public class CustomerData {

    public static int[] CUSTOMERS_HARD = {};

    public CustomerData(){


    }

    public static ArrayList<String> getNameAndLastNameFromListCustomer(ArrayList<Customer> listCustomers){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        Dates dates = new Dates();

        for (Customer customer:listCustomers) {

            String paymentAlert = "";

            String customerHard = "";

            //VERIFY IF THE CUSTOMER'S PAYMENT WILL BE FINISHED
            if(theCustomerHaveCurrentPayment(customer.getCustomerId())){

                Payment payment = getPaymentByIdCustomer(customer.getCustomerId());
                Date date1 = Date.valueOf(dates.getDateOfToday());

                Date date2 = Date.valueOf(payment.getPayDateEnd());

                long diffInMillies = Math.abs(date2.getTime() - date1.getTime());

                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if(payment.getAmuntTime().equals("un mes") && diff<=5){
                    paymentAlert = " ("+diff+")";
                }else if(payment.getAmuntTime().equals("una semana") && diff<=2){
                    paymentAlert = " ("+diff+")";
                }

            }

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName()+paymentAlert+customerHard);

        }

        return listNamesAndLastNames;

    }

    public static Payment getPaymentByIdCustomer(int customerId){

        for (Payment payment:
             DBHelper.CUSTOMER_PAYMENTS) {
            if(payment.getCustomerId()==customerId){
                return payment;
            }
        }
        return null;
    }

    public static ArrayList<String> getNameAndLastNameFromListCustomerInSpecificDay(ArrayList<Customer> listCustomers){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        if(listCustomers==null){

        }else{

            for (Customer customer:listCustomers) {

                listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

            }

        }

        return listNamesAndLastNames;

    }

    public static ArrayList<Customer> customerToSearch(String customerToSearch, ArrayList<Customer> customers){

        ArrayList<Customer> customersToSearch = new ArrayList<>();

        for (Customer currentCustomer:
                customers) {

            if(containsIgnoreCase(currentCustomer.getName(), customerToSearch)  || containsIgnoreCase(currentCustomer.getLastName(), customerToSearch)){
                customersToSearch.add(currentCustomer);
            }

        }

        return customersToSearch;

    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static boolean theCustomerHaveCurrentPayment(int customerId){

        for (Customer customer:
                DBHelper.CUSTOMERS_WITH_CURRENT_PAYMENT) {
            if(customer.getCustomerId()==customerId){
                return true;
            }
        }
        return false;

    }

    public static boolean existsCustomerInCustomerOfToday(Customer customerToSearh){

        for (Customer customer:
                DBHelper.CUSTOMERS_TODAY) {
            if(customer.getCustomerId()==customerToSearh.getCustomerId()){
                return true;
            }
        }
        return false;

    }

    public static boolean theCustomerIsDefaulter(int customerId){

        for (Customer customer:
                DBHelper.CUSTOMERS_DEFAULTERS) {
            if(customer.getCustomerId()==customerId){
                return true;
            }
        }
        return false;

    }

    public static void incrementDaysForPay(int customerId){

        for (Customer customer:
                DBHelper.CUSTOMERS) {
            if(customer.getCustomerId()==customerId){
                customer.setDaysToPay(customer.getDaysToPay()+1);
            }
        }

    }

    public static Customer getCustomerById(int id){

        Customer customer = null;

        for (Customer currentCustomer:
                DBHelper.CUSTOMERS) {
            if(currentCustomer.getCustomerId()==id){
                customer = currentCustomer;
                break;
            }
        }

        return customer;

    }

    public static Payment getDetailsCustomerPayment(int customerId){

        Payment payment = null;

        for (Payment currentPayment:
                DBHelper.CUSTOMER_PAYMENTS) {
            if(currentPayment.getCustomerId()==customerId){
                payment = currentPayment;
                break;
            }
        }

        return payment;
    }

    public static ArrayList<String> getAllDefaulters(){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:DBHelper.CUSTOMERS_DEFAULTERS) {

            String cantDias="";

            if(customer.getDaysToPay()==1){
                cantDias=" día";
            }else{
                cantDias=" días";
            }

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName()+": "+customer.getDaysToPay()+cantDias);

        }

        return listNamesAndLastNames;

    }

    public static void reduceBillToPayToCustomer(int customerId){

        for (Customer customer:DBHelper.CUSTOMERS_DEFAULTERS) {

            if(customer.getCustomerId()==customerId){

                if(customer.getDaysToPay()==1){

                    removeFromDefaulters(customerId);

                    break;

                }else{

                    customer.setDaysToPay(customer.getDaysToPay()-1);

                    break;

                }
            }

        }

    }

    public static void removeFromDefaulters(int customerId){

        for (int i = 0; i<DBHelper.CUSTOMERS_DEFAULTERS.size(); i++) {
            Customer customer = DBHelper.CUSTOMERS_DEFAULTERS.get(i);
            if(customer.getCustomerId()==customerId){
                DBHelper.CUSTOMERS_DEFAULTERS.remove(i);
            }
        }

    }

    public static boolean verifyPartner(String username, String password){

        if(username.equals("test") && password.equals("test")){
            DBHelper.REST_API_PHP_URL = "https://loaiza.000webhostapp.com/REST_API_PHP_CACHI_FITNESS_CENTER/REST_API_TEST/api/";
            try {
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        new DBHelper();
                        return null;
                    }
                }.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        if(!DBHelper.REST_API_PHP_URL.equals("https://loaiza.000webhostapp.com/REST_API_PHP_CACHI_FITNESS_CENTER/REST_API/api/")){
            DBHelper.REST_API_PHP_URL = "https://loaiza.000webhostapp.com/REST_API_PHP_CACHI_FITNESS_CENTER/REST_API/api/";
            try {
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        new DBHelper();
                        return null;
                    }
                }.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Partner partner:
                DBHelper.PARTNERS) {
            if(partner.getUsername().equals(username) && partner.getPassword().equals(password)){
                return true;
            }
        }
        return false;

    }

    public static ArrayList sortAlphabeticallyList(ArrayList<Customer> list){
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<Customer>() {
                @Override
                public int compare(final Customer object1, final Customer object2) {
                    String name1 = object1.getName()+" "+object1.getLastName();
                    String name2 = object2.getName()+" "+object2.getLastName();
                    return name1.compareTo(name2);
                }
            });
        }
        return list;
    }


}

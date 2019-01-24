package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;


import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Partner;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;

public class CustomerData {

    public CustomerData(){


    }




    public static ArrayList<String> getNameAndLastNameFromListCustomer(ArrayList<Customer> listCustomers){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:listCustomers) {

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

        }

        return listNamesAndLastNames;

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

        for (Partner partner:
                DBHelper.PARTNERS) {
            if(partner.getUsername().equals(username) && partner.getPassword().equals(password)){
                return true;
            }
        }
        return false;

    }


}

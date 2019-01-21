package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;


import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.CustomerPay;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.CustomerToday;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.DefaulterCustomer;

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

}

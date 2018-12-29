package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;

import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class CustomerData {


    public CustomerData(){


    }

    public ArrayList<Customer> customerForAddToday(){

        ArrayList<Customer> listCustomersForAddToday = new ArrayList<>();

        for (Customer customer:
                ManagementDatabase.listAllCustomer) {
            if(!existsCustomerInCustomerOfToday(customer)){
                listCustomersForAddToday.add(customer);
            }
        }

        return listCustomersForAddToday;

    }

    public static boolean existsCustomerInCustomerOfToday(Customer customerToSearh){

        for (Customer customer:
                ManagementDatabase.listCustomersOfToday) {
            if(customer.getCustomerId()==customerToSearh.getCustomerId()){
                return true;
            }
        }
        return false;

    }

    public static boolean theCustomerHaveCurrentPayment(int customerToSearh){

        for (Customer customer:
                ManagementDatabase.listAllCustomerWithCurrentPayment) {
            if(customer.getCustomerId()==customerToSearh){
                return true;
            }
        }
        return false;

    }

    public static Customer getCustomerById(int id){

        Customer customer = null;

        for (Customer currentCustomer:
                ManagementDatabase.listAllCustomer) {
            if(currentCustomer.getCustomerId()==id){
                customer = currentCustomer;
                break;
            }
        }

        return customer;

    }

    public static ArrayList<String> getNameAndLastNameFromListCustomer(ArrayList<Customer> listCustomers){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:listCustomers) {

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

        }

        return listNamesAndLastNames;

    }

    //this method obtains me all clients can arrive to the gym
    public ArrayList<Customer> getAllCustomerWithCurrentPayment(){

        return null;

    }




}

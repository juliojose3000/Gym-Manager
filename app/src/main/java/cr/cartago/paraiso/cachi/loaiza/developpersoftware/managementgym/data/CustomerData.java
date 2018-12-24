package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;

import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class CustomerData {

    public ArrayList<Customer> customerForAddToday(){

        ArrayList<Customer> listCustomersForAddToday = new ArrayList<>();

        for (Customer customer:
                ManagementDatabase.listAllCustomer) {
            if(!contains(customer)){
                listCustomersForAddToday.add(customer);
            }
        }

        return listCustomersForAddToday;

    }

    private boolean contains(Customer customerToSearh){

        for (Customer customer:
                ManagementDatabase.listCustomersOfToday) {
            if(customer.getCustomerId()==customerToSearh.getCustomerId()){
                return true;
            }
        }
        return false;

    }



}

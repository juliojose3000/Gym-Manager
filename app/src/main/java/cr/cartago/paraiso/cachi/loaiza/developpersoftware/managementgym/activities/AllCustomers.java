package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class AllCustomers extends Activity {

    private ManagementDatabase managementDatabase;

    ListView listViewCustomers;

    ArrayList<Customer> listCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customers);

        listViewCustomers = findViewById(R.id.listview_all_customers);

        //managementDatabase = new ManagementDatabase();// Creo una nueva conexion

        listCustomers = ManagementDatabase.listAllCustomer;

        fillLisViewCustomers();
    }


    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                getNameAndLastNameFromListCustomer() );

        listViewCustomers.setAdapter(arrayAdapter);
        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }


    private ArrayList<String> getNameAndLastNameFromListCustomer(){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:listCustomers) {

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

        }

        return listNamesAndLastNames;

    }


}

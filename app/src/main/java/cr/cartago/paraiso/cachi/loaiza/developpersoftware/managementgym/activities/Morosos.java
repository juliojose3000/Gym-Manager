package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class Morosos extends Activity {

    private ListView listView_defaulterCustomers;

    private ArrayList<Customer> customers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_morosos);

        listView_defaulterCustomers = findViewById(R.id.listview_defaulterCustomers);

        listView_defaulterCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            }

        });

        customers = ManagementDatabase.listDefaulterCustomers;

        fillLisViewCustomers();
    }



    public void fillLisViewCustomers() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                CustomerData.getNameAndLastNameFromListCustomer(customers) );

        listView_defaulterCustomers.setAdapter(arrayAdapter);
        listView_defaulterCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }




}

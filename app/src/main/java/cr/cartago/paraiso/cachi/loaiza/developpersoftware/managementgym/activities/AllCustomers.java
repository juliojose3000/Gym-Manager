package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class AllCustomers extends Activity {

    private ListView listViewCustomers;

    private Customer customerSelected;

    private EditText editText_customerToSearch;

    private ArrayList<Customer> listCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_customers);

        editText_customerToSearch = findViewById(R.id.editText_customerToSearch2);

        listViewCustomers = findViewById(R.id.listview_all_customers);

        listCustomers = DBHelper.CUSTOMERS;

        fillLisViewCustomers();

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customerSelected = DBHelper.CUSTOMERS.get(position);

            }

        });


    }


    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                CustomerData.getNameAndLastNameFromListCustomer(listCustomers));

        listViewCustomers.setAdapter(arrayAdapter);
        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }


    public void seeDetailsCustomer(View v){

        if(customerSelected==null){
            Toast.makeText(this,"Seleccione un cliente",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(AllCustomers.this, SeeDetailsCustomer.class);

        i.putExtra("customer_id", customerSelected.getCustomerId());

        startActivity(i);

    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void customerToSearch2(View v){

        String nameCustomerToSearch = editText_customerToSearch.getText().toString();

        listCustomers = CustomerData.customerToSearch(nameCustomerToSearch, DBHelper.CUSTOMERS);

        fillLisViewCustomers();

        hideKeyboard();

    }



}

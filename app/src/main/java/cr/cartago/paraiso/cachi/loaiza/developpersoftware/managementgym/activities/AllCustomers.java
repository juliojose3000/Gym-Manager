package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
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

    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<Customer> customersDefaulters;

    private ArrayList<Customer> customerWithCurrentPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_customers);

        editText_customerToSearch = findViewById(R.id.editText_customerToSearch2);

        listViewCustomers = findViewById(R.id.listview_all_customers);

        listCustomers = DBHelper.CUSTOMERS;

        customersDefaulters = DBHelper.CUSTOMERS_DEFAULTERS;

        customerWithCurrentPayment = DBHelper.CUSTOMERS_WITH_CURRENT_PAYMENT;

        createAdapter();

        listViewCustomers.setAdapter(arrayAdapter);

        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //fillLisViewCustomers();

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customerSelected = listCustomers.get(position);
            }

        });

    }

    public void createAdapter(){
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, CustomerData.getNameAndLastNameFromListCustomer(listCustomers)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);

                row.setBackgroundColor (Color.TRANSPARENT); // default coloe

                for (Customer customer:
                        customersDefaulters) {

                    String item = listCustomers.get(position).getName()+" "+listCustomers.get(position).getLastName();

                    if(item.equals(customer.getName()+" "+customer.getLastName()))
                    {
                        row.setBackgroundColor (Color.argb(100,255,0,0)); // some color
                    }
                }

                for (Customer customer:
                        customerWithCurrentPayment) {

                    String item = listCustomers.get(position).getName()+" "+listCustomers.get(position).getLastName();

                    if(item.equals(customer.getName()+" "+customer.getLastName()))
                    {
                        row.setBackgroundColor (Color.argb(100,0,255,0)); // some color
                    }
                }


                return row;
            }
        };
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

        createAdapter();

        listViewCustomers.setAdapter(arrayAdapter);

        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        hideKeyboard();

    }


    public void addPayment(View v){

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        if(customerSelected==null){
            Toast.makeText(this,"Seleccione un cliente",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(AllCustomers.this, AddPayment.class);

        i.putExtra("customer_name", customerSelected.getName()+" "+customerSelected.getLastName());

        i.putExtra("customer_id", customerSelected.getCustomerId());

        startActivity(i);

        onRestart();

    }


    public boolean verifyInternetAccess(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("customer_id",customerSelected.getCustomerId());

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int customerId= savedInstanceState.getInt("customer_id");
        customerSelected = CustomerData.getCustomerById(customerId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        customerToSearch2(null);
        createAdapter();
    }
}

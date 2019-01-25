package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.MainActivity;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class Pesas extends Activity {

    private ListView listViewCustomers;

    private Customer customerCurrentSelected;

    private ArrayList<Customer> customersDefaulters;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pesas);

        listViewCustomers = findViewById(R.id.listview_come_customers);

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customerCurrentSelected = DBHelper.CUSTOMERS_TODAY.get(position);

            }

        });

        customersDefaulters = DBHelper.CUSTOMERS_DEFAULTERS;

        createAdapter();

        listViewCustomers.setAdapter(arrayAdapter);

        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    public void createAdapter(){
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, CustomerData.getNameAndLastNameFromListCustomer(DBHelper.CUSTOMERS_TODAY)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);

                row.setBackgroundColor (Color.TRANSPARENT); // default coloe

                for (Customer customer:
                        customersDefaulters) {
                    if(getItem(position).equals(customer.getName()+" "+customer.getLastName()))
                    {
                        row.setBackgroundColor (Color.argb(100,255,0,0)); // some color
                    }
                }
                return row;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.customer_menu, menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        Intent i = null;
        if(id == R.id.item_add_customer){
            if(!verifyInternetAccess()){
                Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
                return false;
            }
            i = new Intent(Pesas.this, AddCustomer.class);
        }else if(id == R.id.item_all_customers) {
            i = new Intent(Pesas.this, AllCustomers.class);
        }else if(id == R.id.item_all_defaulter){
            i = new Intent(Pesas.this, Morosos.class);
        }else if(id == R.id.item_customers_to_arrived_to_specific_date){
            if(!verifyInternetAccess()){
                Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
                return false;
            }
            i = new Intent(Pesas.this, CustomersArrivedInSpecificDay.class);
        }
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }


    public void addCustomersToday(View v){

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(Pesas.this, AddCustomersToday.class);

        startActivity(i);

    }


    public void addPayment(View v){

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        if(customerCurrentSelected==null){
            Toast.makeText(this,"Seleccione un cliente",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(Pesas.this, AddPayment.class);

        i.putExtra("customer_name", customerCurrentSelected.getName()+" "+customerCurrentSelected.getLastName());

        i.putExtra("customer_id", customerCurrentSelected.getCustomerId());

        startActivity(i);

    }

    public void seeDetailsCustomer(View v){

        if(customerCurrentSelected==null){
            Toast.makeText(this,"Seleccione un cliente",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(Pesas.this, SeeDetailsCustomer.class);

        i.putExtra("customer_id", customerCurrentSelected.getCustomerId());

        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Pesas.this, MainActivity.class);
        startActivity(i);
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

}

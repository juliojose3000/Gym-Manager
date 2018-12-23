package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.MainActivity;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class Pesas extends Activity {



    ListView listViewCustomers;

    ArrayList<Customer> listCustomers;

    ManagementDatabase managementDatabase;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pesas);

        listViewCustomers = findViewById(R.id.listview_come_customers);

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }

        });

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH)+1;

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        listCustomers = new ArrayList<>();

        listCustomers = ManagementDatabase.listCustomersOfToday;

        fillLisViewCustomers();

    }

    @Override
    protected void onResume() {
        super.onResume();

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


    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.customer_menu, menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == R.id.item_add_customer){
            Intent i = new Intent(Pesas.this, AddCustomer.class);
            startActivity(i);
        }else if(id == R.id.item_all_customers) {
            Intent i = new Intent(Pesas.this, AllCustomers.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }


    public void seeAllCustomers(View v){

        Intent i = new Intent(Pesas.this, AddCustomersToday.class);

        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Pesas.this, MainActivity.class);
        startActivity(i);
    }











}

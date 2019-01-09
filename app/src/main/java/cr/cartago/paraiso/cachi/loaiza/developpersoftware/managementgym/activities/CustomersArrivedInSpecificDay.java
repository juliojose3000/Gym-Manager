package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class CustomersArrivedInSpecificDay extends Activity {

    private EditText dateArrivedCustomers;

    private DatePickerDialog datePickerDialog;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    private ManagementDatabase managementDatabase;

    private ListView listViewCustomerInSpecificDay;

    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<Customer> customersInDate;

    private ArrayList<String> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customers_arrived_in_specific_day);

        dateArrivedCustomers = findViewById(R.id.editText_date_arrived);

        listViewCustomerInSpecificDay = findViewById(R.id.listview_customer_in_date);

        customers = CustomerData.getNameAndLastNameFromListCustomerInSpecificDay(customersInDate);

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                customers);

        listViewCustomerInSpecificDay.setAdapter(arrayAdapter);
        listViewCustomerInSpecificDay.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewCustomerInSpecificDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }

        });

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        dateArrivedCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = new DatePickerDialog(CustomersArrivedInSpecificDay.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                onClickInSpecificDate(year, month, dayOfMonth);

                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();
            }

        });

    }

    private void onClickInSpecificDate(int year, int month, int dayOfMonth){

        if(!verifyInternetAccess()){
            Toast.makeText(CustomersArrivedInSpecificDay.this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }
        managementDatabase = new ManagementDatabase();

        dateArrivedCustomers.setText(year + "-" + (month+1) + "-" + dayOfMonth);

        customersInDate = managementDatabase.customerInSpecificDate(year + "-" + (month+1) + "-" + dayOfMonth);

        customers = CustomerData.getNameAndLastNameFromListCustomerInSpecificDay(customersInDate);

        if(customers.size()==0){
            Toast.makeText(this, "Nadie llegó en esta fecha",Toast.LENGTH_LONG).show();
        }

        fillLisViewCustomers();

    }

    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                customers );

        listViewCustomerInSpecificDay.setAdapter(arrayAdapter);
        listViewCustomerInSpecificDay.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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

package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class AddCustomer extends Activity {

    private EditText customerName;

    private EditText customerLastname;

    private EditText customerStartdate;

    private EditText customerNickname;

    private DatePickerDialog datePickerDialog;

    private Button buttonAccept;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    private ThreadConnectionDB threadConnectionDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);

        customerName = findViewById(R.id.editText_customer_name);

        customerLastname = findViewById(R.id.editText_customer_lastname);

        customerNickname = findViewById(R.id.editText_customer_nickname);

        customerStartdate = findViewById(R.id.editText_customer_startdate);

        buttonAccept = findViewById(R.id.button_accept_mainActivity);

        customerStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);

                month = calendar.get(Calendar.MONTH);

                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddCustomer.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                customerStartdate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                            }
                        },year,month,dayOfMonth);
                        datePickerDialog.show();
            }

        });

        threadConnectionDB = new ThreadConnectionDB();

        threadConnectionDB.start();

    }


    public void cancel(View v){

        customerName.setText("");

        customerLastname.setText("");

        customerStartdate.setText("");

        customerNickname.setText("");

    }

    public void addCustomer(View v){


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

    public class ThreadConnectionDB extends Thread{
        public void run()
        {
        }
    }


}

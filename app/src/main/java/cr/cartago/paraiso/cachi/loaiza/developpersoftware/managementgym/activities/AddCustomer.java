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
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
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

    private ManagementDatabase managementDatabase;

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

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        final String customerName = this.customerName.getText().toString();

        final String customerLastname = this.customerLastname.getText().toString();

        final String customerStartdate = this.customerStartdate.getText().toString();

        final String customerNickname = this.customerNickname.getText().toString();

        if(customerName.equals("") || customerLastname.equals("") || customerStartdate.equals("")){
            Toast.makeText(this, "Solo el campo 'Conocido como' no es indispensable. Complete los demás.", Toast.LENGTH_LONG).show();
            return;
        }

        //if the connection have be closed, it create a new connection
        try {
            if(threadConnectionDB.isAlive()){
                while(threadConnectionDB.isAlive()){}
            }
            if(managementDatabase.theConnectionIsClose()){

                threadConnectionDB = new ThreadConnectionDB();

                threadConnectionDB.start();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while(threadConnectionDB.isAlive()){}

        boolean isInserted = managementDatabase.insertCustomer(customerName, customerLastname, customerNickname, customerStartdate);

        String notification;

        if(isInserted){

            new Thread() {
                public void run() {
                    ManagementDatabase.listAllCustomer.add(new Customer(customerName, customerLastname, customerNickname, Date.valueOf(customerStartdate)));
                    ManagementDatabase.listCustomerForAddToday.add(new Customer(customerName, customerLastname, customerNickname, Date.valueOf(customerStartdate)));
                }
            }.start();



            notification = "El cliente se guardó correctamente";
            cancel(null);
        }else{
            notification = "Hubo un problema al guardar el cliente. Contacte a su técnico";
        }

        Toast.makeText(this, notification, Toast.LENGTH_LONG).show();

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
            managementDatabase = new ManagementDatabase();
        }
    }


}

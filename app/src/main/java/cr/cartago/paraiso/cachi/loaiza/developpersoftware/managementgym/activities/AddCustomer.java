package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class AddCustomer extends Activity {

    private EditText customerName;

    private EditText customerLastname;

    private EditText customerStartdate;

    private EditText customerNickname;

    private EditText customerPhone;

    private DatePickerDialog datePickerDialog;

    private Button buttonAccept;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    private int codeResponse = 200;


    String name;

    String lastname;

    String startdate;

    String nickname;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);

        customerName = findViewById(R.id.editText_customer_name);

        customerLastname = findViewById(R.id.editText_customer_lastname);

        customerNickname = findViewById(R.id.editText_customer_nickname);

        customerStartdate = findViewById(R.id.editText_customer_startdate);

        customerPhone = findViewById(R.id.editText_customerPhone);

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
    }


    public void cancel(View v){

        customerName.setText("");

        customerLastname.setText("");

        customerStartdate.setText("");

        customerNickname.setText("");

        customerPhone.setText("");

    }

    public void addCustomer(View v){

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        name = stripAccents(this.customerName.getText().toString());

        lastname = stripAccents(this.customerLastname.getText().toString());

        startdate = this.customerStartdate.getText().toString();

        nickname = stripAccents(this.customerNickname.getText().toString());

        phoneNumber = customerPhone.getText().toString();

        if(name.equals("") || lastname.equals("") || startdate.equals("") || phoneNumber.equals("")){
            Toast.makeText(this, "Solo el campo 'Conocido como' no es indispensable. Complete los demás.", Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... params) {
                codeResponse = DBHelper.insertCustomer(params[0], params[1], params[2],params[3], params[4]);
                return null;
            }
        }.execute(name, lastname, startdate, nickname, phoneNumber);

        if(codeResponse==200){

            int customerId = DBHelper.CUSTOMERS.size()+1;

            Customer customer = new Customer(customerId,name, lastname, nickname, startdate, "87349999");

            DBHelper.CUSTOMERS.add(customer);
            DBHelper.CUSTOMERS_FOR_ADD_TODAY.add(customer);

            DBHelper.CUSTOMERS = CustomerData.sortAlphabeticallyList(DBHelper.CUSTOMERS);
            DBHelper.CUSTOMERS_FOR_ADD_TODAY = CustomerData.sortAlphabeticallyList(DBHelper.CUSTOMERS_FOR_ADD_TODAY);

            cancel(null);
            Toast.makeText(this,"Se ha registrado el cliente exitosamente",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Hubo un problema al guardar el cliente. Intente de nuevo",Toast.LENGTH_LONG).show();
        }



    }

    public String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
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

package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Date;

public class AddPayment extends Activity {

    private TextView textView_customerName;

    private TextView textView_paymentDetails;

    private String paymentDetails;

    private String customerName;

    private int customerId;

    private int year, month, dayOfMonth;

    private int endYear, endMonth, endDayOfMonth;

    private String duracion;

    private Calendar calendar;

    private Button dayButton, weekButton, monthButton;

    private ThreadConnectionDB threadConnectionDB;

    private ThreadFillAllList threadFillAllList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_payment);

        dayButton = findViewById(R.id.button_day_payment);

        weekButton = findViewById(R.id.button_week_payment);

        monthButton = findViewById(R.id.button_month_payment);

        Bundle extras = getIntent().getExtras();

        customerName = extras.getString("customer_name");

        customerId = extras.getInt("customer_id");

        calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH)+1;

        textView_customerName = findViewById(R.id.textView_payment_customer);

        textView_paymentDetails = findViewById(R.id.textView_payment_details);

        textView_customerName.setText("Agregar un pago a "+customerName);

        threadConnectionDB = new ThreadConnectionDB();

        threadFillAllList = new ThreadFillAllList();

        threadConnectionDB.start();

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

    public void day(View v){

        duracion = "un día";

        Calendar calendar = Calendar.getInstance();

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        endYear = calendar.get(Calendar.YEAR);

        endMonth = calendar.get(Calendar.MONTH)+1;

        endDayOfMonth = dayOfMonth;

        String fechaPago = Date.getDateForShowUser(year+"-"+month+"-"+dayOfMonth);

        String dayName = Date.getDayName(year+"-"+month+"-"+dayOfMonth);

        paymentDetails = "Cliente: "+customerName+"\nFecha de pago: "+dayName+", "+fechaPago+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

        setColorButtonPressed(v);

    }

    public void week(View v){

        duracion = "una semana";

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        month = calendar.get(Calendar.MONTH)+1;

        year = calendar.get(Calendar.YEAR);

        calendar.add(Calendar.DAY_OF_YEAR, 6);

        endDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        endYear = calendar.get(Calendar.YEAR);

        endMonth = calendar.get(Calendar.MONTH)+1;

        String startDate = Date.getDateForShowUser(year+"-"+month+"-"+dayOfMonth);

        String startDayName =Date.getDayName(year+"-"+month+"-"+dayOfMonth);

        String endDate = Date.getDateForShowUser(year+"-"+endMonth+"-"+endDayOfMonth);

        String endDayName = Date.getDayName(year+"-"+endMonth+"-"+endDayOfMonth);

        paymentDetails = "Cliente: "+customerName+"\nCubre desde el: "+startDayName+", "+startDate+
                "\nhasta el: "+endDayName+", "+endDate+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

        setColorButtonPressed(v);

    }

    public void month(View v){

        duracion = "un mes";

        Calendar calendar = Calendar.getInstance();

        //calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        endYear = calendar.get(Calendar.YEAR);

        endMonth = calendar.get(Calendar.MONTH)+1;

        endDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        dayOfMonth  = 1;

        String startDate = Date.getDateForShowUser(year+"-"+month+"-"+dayOfMonth);

        String startDayName =Date.getDayName(year+"-"+month+"-"+dayOfMonth);

        String endDate = Date.getDateForShowUser(year+"-"+endMonth+"-"+endDayOfMonth);

        String endDayName = Date.getDayName(year+"-"+endMonth+"-"+endDayOfMonth);

        paymentDetails = "Cliente: "+customerName+"\nCubre desde el: "+startDayName+", "+startDate+
                "\nhasta el: "+endDayName+", "+endDate+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

        setColorButtonPressed(v);

    }

    private void setColorButtonPressed(View v){
        /*dayButton.setBackgroundResource(android.R.drawable.btn);
        weekButton.setBackgroundResource(android.R.drawable.btn_default);
        monthButton.setBackgroundResource(android.R.drawable.btn_default);*/
        //v.setBackgroundColor(Color.BLUE);
        dayButton.getBackground().clearColorFilter();
        weekButton.getBackground().clearColorFilter();
        monthButton.getBackground().clearColorFilter();
        v.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
    }

    public void cancel(View v){
        Intent i = new Intent(AddPayment.this, Pesas.class);
        startActivity(i);
    }

    public void accept(View v){



    }


    public class ThreadConnectionDB extends Thread{
        public void run()
        {

        }
    }


    public class ThreadFillAllList extends Thread{
        public void run()
        {

        }
    }


    @Override
    public void onBackPressed(){

        Intent i = new Intent(AddPayment.this, Pesas.class);
        startActivity(i);
    }

    private void disabilityButtons(){
        monthButton.setEnabled(false);
        dayButton.setEnabled(false);
        weekButton.setEnabled(false);
    }


}

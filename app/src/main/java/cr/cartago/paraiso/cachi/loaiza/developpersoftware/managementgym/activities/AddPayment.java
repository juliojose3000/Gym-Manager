package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;

public class AddPayment extends Activity {

    private TextView textView_customerName;

    private TextView textView_paymentDetails;

    private String paymentDetails;

    private String customerName;

    private int year, month, dayOfMonth;

    private int endYear, endMonth, endDayOfMonth;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_payment);

        Bundle extras = getIntent().getExtras();

        customerName = extras.getString("customer_name");

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH)+1;

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        textView_customerName = findViewById(R.id.textView_payment_customer);

        textView_paymentDetails = findViewById(R.id.textView_payment_details);

        textView_customerName.setText("Agregar un pago a "+customerName);

    }


    public void day(View v){

        paymentDetails = "Cliente: "+customerName+"\nFecha de pago: "+dayOfMonth+"-"+month+"-"+year+"\nDuración: un día";

        textView_paymentDetails.setText(paymentDetails);

    }

    public void week(View v){

        setDetails(7);

    }

    public void month(View v){

        setDetails(30);

    }

    private void setDetails(int noOfDays){

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);

        endYear = calendar.get(Calendar.YEAR);

        endMonth = calendar.get(Calendar.MONTH)+1;

        endDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        paymentDetails = "Cliente: "+customerName+"\nFecha de pago: "+dayOfMonth+"-"+month+"-"+year+"\nCubre hasta el: "+endDayOfMonth+"-"+endMonth+"-"+endYear+"\nDuración: una semana";

        textView_paymentDetails.setText(paymentDetails);

    }

    public void cancel(View v){
        Intent i = new Intent(AddPayment.this, Pesas.class);
        startActivity(i);
    }



}

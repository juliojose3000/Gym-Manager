package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ConnectionServer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;

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

    private ManagementDatabase managementDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_payment);

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

        managementDatabase = new ManagementDatabase();

    }


    public void day(View v){

        duracion = "un día";

        Calendar calendar = Calendar.getInstance();

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        paymentDetails = "Cliente: "+customerName+"\nFecha de pago: "+dayOfMonth+"-"+month+"-"+year+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

    }

    public void week(View v){

        duracion = "una semana";

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        endYear = calendar.get(Calendar.YEAR);

        endMonth = calendar.get(Calendar.MONTH)+1;

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)+1;

        endDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)+7;

        paymentDetails = "Cliente: "+customerName+"\nCubre desde el: "+dayOfMonth+"-"+month+"-"+year+"\nhasta el: "+endDayOfMonth+"-"+endMonth+"-"+endYear+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

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

        paymentDetails = "Cliente: "+customerName+"\nCubre desde el: "+dayOfMonth+"-"+month+"-"+year+"\nhasta el: "+endDayOfMonth+"-"+endMonth+"-"+endYear+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

    }


    public void cancel(View v){
        Intent i = new Intent(AddPayment.this, Pesas.class);
        startActivity(i);
    }

    public void accept(View v){

        boolean isInserted = false;

        if(!CustomerData.theCustomerHaveCurrentPayment(customerId)){
            try {
                isInserted = managementDatabase.addPaymentFromCustomer(customerId, year+"-"+month+"-"+dayOfMonth, endYear+"-"+endMonth+"-"+endDayOfMonth, duracion);
                ManagementDatabase.listAllCustomerWithCurrentPayment.add(CustomerData.getCustomerById(customerId));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            String notification;

            if(isInserted){
                notification = "Se ha registrado el pago correctamente";
            }else{
                notification = "Hubo un proble al registrar el pago. Contacte a su técnico";
            }
            Toast.makeText(this, notification, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, customerName+" tiene un pago todavía vigente", Toast.LENGTH_LONG).show();
        }






    }



}
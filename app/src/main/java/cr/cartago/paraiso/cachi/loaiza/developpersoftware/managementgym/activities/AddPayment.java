package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.sql.Date;
import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Dates;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;


public class AddPayment extends Activity {

    private TextView textView_customerName;

    private TextView textView_paymentDetails;

    private String paymentDetails;

    private String customerName;

    private int customerId;

    private String duracion;

    private Button dayButton, weekButton, monthButton;

    private Dates date;

    private String startDate;

    private String endDate;

    private ArrayList<String> listBillToPay;

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

        date = new Dates();

        textView_customerName = findViewById(R.id.textView_payment_customer);

        textView_paymentDetails = findViewById(R.id.textView_payment_details);

        textView_customerName.setText("Agregar un pago a "+customerName);

        listBillToPay = new ArrayList<>();

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    listBillToPay = DBHelper.getListAllBillToPay(customerId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();

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

        duracion = "un dia";

        startDate = date.getDateOfToday();

        endDate = startDate;

        paymentDetails = "Cliente: "+customerName+"\nCubre hoy: "+Dates.getDateForShowUser(startDate)+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

        setColorButtonPressed(v);

    }

    public void week(View v){

        duracion = "una semana";

        startDate = date.getDateOfToday();

        endDate = date.getDateForDB(date.getDateInAWeek());

        paymentDetails = "Cliente: "+customerName+"\nCubre desde el: "+Dates.getDateForShowUser(startDate)+
                "\nhasta el: "+date.getDateInAWeek()+"\nDuración: "+duracion;

        textView_paymentDetails.setText(paymentDetails);

        setColorButtonPressed(v);

    }

    public void month(View v){

        duracion = "un mes";

        startDate = date.getDateOfToday();

        endDate = date.getDateForDB(date.getDateInAMonth());

        paymentDetails = "Cliente: "+customerName+"\nCubre desde el: "+Dates.getDateForShowUser(date.getDateOfToday())+
                "\nhasta el: "+date.getDateInAMonth()+"\nDuración: "+duracion;

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

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        if(paymentDetails==null){
            Toast.makeText(this,"Seleccione la cantidad de tiempo", Toast.LENGTH_LONG).show();
            return;
        }

        boolean theCustomerHaveACurrentPayment = !CustomerData.theCustomerHaveCurrentPayment(customerId);

        if(theCustomerHaveACurrentPayment){//el cliente no tiene un pago vigente

            doPay();

        }else{//the customer have a current payment
            //Toast.makeText(this, customerName+" tiene un pago todavía vigente", Toast.LENGTH_LONG).show();
            AlertDialog diaBox = AskOption();
            diaBox.show();


        }

    }

    private void doPay(){

        new Thread(){
            public void run(){

                //int nextId = DBHelper.LIST_PAYMENTS.get(DBHelper.LIST_PAYMENTS.size()-1).getPaymentId()+1;
                DBHelper.addPaymentFromCustomer(customerId, startDate, endDate, duracion);
                DBHelper.LIST_PAYMENTS.add(new Payment(customerId, startDate, endDate, duracion));
                DBHelper.CUSTOMERS_WITH_CURRENT_PAYMENT.add(CustomerData.getCustomerById(customerId));
            }
        }.start();


        if(CustomerData.theCustomerIsDefaulter(customerId)){//If the customer is defaulter, so delete the days covered by the payment

            isHeNoLongerADefaulter();

            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    DBHelper.deleteDaysForPayByCoveredPay(customerId, startDate);
                    try {
                        DBHelper.getAllCustomersDefaulters();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        }

        disabilityButtons();
        Toast.makeText(this, "Se agregó el pago correctamente", Toast.LENGTH_LONG).show();
    }


    private void updatePayment(){

        /*new Thread(){
            public void run(){

                Customer customer = CustomerData.getCustomerById(customerId);

                Payment currentPayment = CustomerData.getPaymentByIdCustomer(customer.getCustomerId());

                DBHelper.deletePayment(""+currentPayment.getPaymentId());

            }
        }.start();*/


        new Thread(){

            public void run(){

                int nextId = DBHelper.LIST_PAYMENTS.get(DBHelper.LIST_PAYMENTS.size()-1).getPaymentId()+1;

                DBHelper.addPaymentFromCustomer(customerId, startDate, endDate, duracion);

                DBHelper.LIST_PAYMENTS.add(new Payment(nextId,customerId, startDate, endDate, duracion));
            }
        }.start();


        if(CustomerData.theCustomerIsDefaulter(customerId)){//If the customer is defaulter, so delete the days covered by the payment

            isHeNoLongerADefaulter();

            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    DBHelper.deleteDaysForPayByCoveredPay(customerId, startDate);
                    try {
                        DBHelper.getAllCustomersDefaulters();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        }


    }

    private AlertDialog AskOption()
    {

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Pago vigente")
                .setMessage("El cliente "+customerName+" ya tiene un pago asignado. ¿Seguro que quieres agregar un nuevo pago?")


                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        updatePayment();

                        doPay();

                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public void isHeNoLongerADefaulter(){ //Ya no es un moroso?

        if(listBillToPay.size()==0){
            return;
        }

        Date oldBillToPay = Date.valueOf(Dates.getDateForDB(listBillToPay.get(0)));
        Date firstDayDateToPay = Date.valueOf(startDate);

        if (oldBillToPay.compareTo(firstDayDateToPay) > 0) {
            CustomerData.removeFromDefaulters(customerId);
        } else if (oldBillToPay.compareTo(firstDayDateToPay) < 0) {
            System.out.println("Date1 is before Date2");
        } else if (oldBillToPay.compareTo(firstDayDateToPay) == 0) {
            CustomerData.removeFromDefaulters(customerId);
        } else {
            System.out.println("How to get here?");
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


    public class AddPaymentFromCustomer extends Thread{
        public void run()
        {
        }
    }

    private class Test extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            DBHelper.addPaymentFromCustomer(customerId, startDate, endDate, duracion);
            return null;
        }
    }


}

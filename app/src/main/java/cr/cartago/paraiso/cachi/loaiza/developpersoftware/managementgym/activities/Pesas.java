package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.MainActivity;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Dates;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Message;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.services.SendMessageService;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.services.YourJobService;

public class Pesas extends Activity {

    private ListView listViewCustomers;

    private Customer customerCurrentSelected;

    private ArrayList<Customer> customersDefaulters;

    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<String> listCustomersToday;

    private SendMessageService sendMessageService;

    private String customerToSentMessage;

    private int WARNING_MESSAGE = 0;

    private int EXPIRATION_MESSAGE = 1;

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

        listCustomersToday = CustomerData.getNameAndLastNameFromListCustomer(DBHelper.CUSTOMERS_TODAY);

        //--------------------------------------------//
        createAdapter();
        //-----------------------------------------------------//

        listViewCustomers.setAdapter(arrayAdapter);

        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        runOnUiThread(new Runnable(){//this method allow to show the toast message about sent messages

            @Override
            public void run(){
                sendMessage();
            }
        });

    }

    public void createAdapter(){
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listCustomersToday) {
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
        }else if(id == R.id.item_customers_to_sent_message) {

            i = new Intent(Pesas.this, CustomersToSentMessage.class);
            sentMessagesTo();
            i.putExtra("customers", this.customerToSentMessage);

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

    public void sendMessage(){

        if(!isNeededSendMenssage()){return;}

        Dates dates = new Dates();

        for (final Customer customer:DBHelper.CUSTOMERS) {

            //VERIFY IF THE CUSTOMER'S PAYMENT WILL BE FINISHED
            if(CustomerData.theCustomerHaveCurrentPayment(customer.getCustomerId())){

                Payment payment = CustomerData.getPaymentByIdCustomer(customer.getCustomerId());

                Date date1 = Date.valueOf(dates.getDateOfToday());

                Date date2 = Date.valueOf(payment.getPayDateEnd());

                long diffInMillies = Math.abs(date2.getTime() - date1.getTime());

                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                final String endPayment = Dates.getDateForShowUser(date2.toString());

                boolean itHaveSentMessage = false;

                if(payment.getAmuntTime().equals("un mes")){

                    Message message = new Message(customer.getPhoneNumber(), customer.getName(), endPayment);

                    if(diff==3){
                        message.sendMessage(Pesas.this, WARNING_MESSAGE);
                    }else if(diff==0){
                        message.sendMessage(Pesas.this, EXPIRATION_MESSAGE);
                    }

                    itHaveSentMessage = true;

                }

                if(itHaveSentMessage){
                    Toast.makeText(Pesas.this, "Se han enviado mensajes de advertencia", Toast.LENGTH_LONG).show();
                }

            }

        }

    }//end sendMessage

    public void sentMessagesTo(){

        Dates dates = new Dates();

        String customersSentMennsage = "";

        for (final Customer customer:DBHelper.CUSTOMERS) {

            //VERIFY IF THE CUSTOMER'S PAYMENT WILL BE FINISHED
            if(CustomerData.theCustomerHaveCurrentPayment(customer.getCustomerId())){

                Payment payment = CustomerData.getPaymentByIdCustomer(customer.getCustomerId());

                Date date1 = Date.valueOf(dates.getDateOfToday());

                Date date2 = Date.valueOf(payment.getPayDateEnd());

                long diffInMillies = Math.abs(date2.getTime() - date1.getTime());

                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if(payment.getAmuntTime().equals("un mes") && diff==3){

                    customersSentMennsage+=customer.getName()+" "+customer.getLastName()+" (Advertencia)\n\n";

                }

                if(payment.getAmuntTime().equals("un mes") && diff==0){

                    customersSentMennsage+=customer.getName()+" "+customer.getLastName()+" (Caducidad)\n\n";

                }

            }

        }
        this.customerToSentMessage = customersSentMennsage;

    }//end sendMessage


    private void displayNotification(String customersSentMennsage){

        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification.Builder(getApplicationContext()).
                setContentTitle(customersSentMennsage).setContentText(customersSentMennsage).
                setContentTitle("Se ha enviado un mensaje a: ").setSmallIcon(R.drawable.ic_launcher_foreground).build();


        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0,notify);

    }

    //this method will execute only once a day for send a message to customers that will caducate the payment
    private boolean isNeededSendMenssage(){

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFERENCES5",0);
        int lastDay = settings.getInt("day",0);

        if(lastDay != currentDay){

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();

            return true;

        }

        return false;

    }


}

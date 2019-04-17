package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Dates;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Message;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;

public class SendMessageService extends Service {

    public SendMessageService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendMessage();

        return START_STICKY;
    }

    private void customersToSendMessage(){
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

                if(payment.getAmuntTime().equals("un mes") && diff==3){
                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Message message = new Message(customer.getPhoneNumber(), customer.getName(), endPayment);
                            message.sendMessage(SendMessageService.this);
                            return null;
                        }
                    }.execute();

                }

            }

        }
    }


    public void sendMessage(){

        new Thread(new Runnable(){
            public void run() {
                while(true)
                {
                    try {
                        customersToSendMessage();
                        Thread.sleep(21600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }

            }
        }).start();


    }//end sendMessage

    //this method will execute only once a day for send a message to customers that will caducate the payment
    private boolean isNeededSendMenssage(){

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFS",0);
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

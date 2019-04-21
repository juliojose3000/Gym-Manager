package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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

public class YourJobService extends JobService {

    private static final int JOB_ID = 1;
    public static final long ONE_MINUTE_INTERVAL = 10 * 1000L;
    public static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L; // 1 Day
    public static final long ONE_WEEK_INTERVAL = 7 * 24 * 60 * 60 * 1000L; // 1 Week

    public static Context contexto;

    public static void schedule(Context context, long intervalMillis) {
        contexto = context;
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, YourJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(intervalMillis);
        jobScheduler.schedule(builder.build());
    }

    public static void cancel(Context context) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        customersToSendMessage();
        if (false) {
            // To finish a periodic JobService,
            // you must cancel it, so it will not be scheduled more.
            YourJobService.cancel(this);
        }

        // false when it is synchronous.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void customersToSendMessage(){
        //if(!isNeededSendMenssage()){return;}

        Dates dates = new Dates();

        for (final Customer customer: DBHelper.CUSTOMERS) {

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
                            message.sendMessage(contexto);
                            return null;
                        }
                    }.execute();

                }

            }

        }
    }

    //this method will execute only once a day for send a message to customers that will caducate the payment
    private boolean isNeededSendMenssage(){

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFERENCES",0);
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
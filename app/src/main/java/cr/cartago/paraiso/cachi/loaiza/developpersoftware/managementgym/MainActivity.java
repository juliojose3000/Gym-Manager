package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class MainActivity extends Activity {

    private EditText editText_password;

    private EditText editText_username;

    private Thread thread;

    private boolean theConnectionWasSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText_password = findViewById(R.id.password);

        editText_username = findViewById(R.id.username);

        if(verifyInternetAccess()){
            thread = new Thread(){
                public void run(){
                    new DBHelper();
                }
            };
            thread.start();
            theConnectionWasSuccessful = true;
        }else{
            Toast.makeText(this,"Verifique su conexión a internet", Toast.LENGTH_LONG).show();
            theConnectionWasSuccessful = false;
        }

    }


    public void accept(View v){

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        if(!theConnectionWasSuccessful){
            thread = new Thread(){
                public void run(){
                    new DBHelper();
                }
            };
            thread.start();

        }

        while (thread.isAlive()){}

        ArrayList<Customer> list = DBHelper.CUSTOMERS;

        String username = editText_username.getText().toString();

        String password = editText_password.getText().toString();

        if(CustomerData.verifyPartner(username, password)){
            //TEST MODE: MODE IN WHICH I CAN DO MANY TYPES OF TEST WITHOUT INTERFERE WITH THE MAIN DATABASE
            if(username.equals("test") && password.equals("test")){
                Toast.makeText(this,"Modo TEST activado", Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(MainActivity.this, Pesas.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"Usuario o contraseña incorrectos, intente de nuevo", Toast.LENGTH_LONG).show();
        }

    }

    public void cancel(View v){

        editText_username.setText("");

        editText_password.setText("");

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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


  /*  private class Testing extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {

            Map<String, String> params = new HashMap<>();

            HttpJsonParser httpJsonParser = new HttpJsonParser();

            JSONObject json = httpJsonParser.makeHttpRequest(DBCustomer.URL_Read(), "GET", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray JAStuff = json.getJSONArray(TAG_STUFF);

                    // CHECK THE NUMBER OF RECORDS
                    int intStuff = JAStuff.length();

                    if (intStuff != 0) {

                        for (int i = 0; i < JAStuff.length(); i++) {
                            JSONObject JOStuff = JAStuff.getJSONObject(i);
                            Log.e("ALL THE STUFF", JOStuff.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
*/



}

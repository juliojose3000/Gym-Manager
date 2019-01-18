package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBCustomer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.HttpJsonParser;

public class MainActivity extends Activity {

    private EditText editText_password;

    private EditText editText_username;

    private Testing testing;

    String TAG_SUCCESS = "success";

    String TAG_STUFF = "stuff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText_password = findViewById(R.id.password);

        editText_username = findViewById(R.id.username);

        testing = new Testing();

        testing.execute();

    }


    public void accept(View v){


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




    private class Testing extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {

            Map<String, String> params = new HashMap<>();

            HttpJsonParser httpJsonParser = new HttpJsonParser();

            JSONObject json = httpJsonParser.makeHttpRequest(DBCustomer.read(), "GET", params);

            try {
                /* Checking for SUCCESS TAG */
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray JAStuff = json.getJSONArray(TAG_STUFF);

                    /** CHECK THE NUMBER OF RECORDS **/
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




}

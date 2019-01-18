package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;

public class CustomerBillToPay extends Activity {

    private ArrayList<String> listBillToPayOfCustomer;

    private ListView listView_billToPay;


    private int customerId;

    private String customerName;

    private TextView title;

    private String dateBillToPay;

    private int pos;

    private ArrayAdapter<String> arrayAdapter;

    private ThreadConnectionDB threadConnectionDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bill_to_pay);

        Bundle extras = getIntent().getExtras();

        customerId = extras.getInt("customer_id");

        customerName = extras.getString("customer_name");

        title = findViewById(R.id.textView_title_customer_bill_to_pay);

        listView_billToPay = findViewById(R.id.listview_bill_to_pay);

        title.setText("Días que ha llegado "+customerName+" sin pagar:");

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                listBillToPayOfCustomer );

        listView_billToPay.setAdapter(arrayAdapter);
        listView_billToPay.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView_billToPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dateBillToPay = listBillToPayOfCustomer.get(position);

                pos = position;

            }

        });

        threadConnectionDB = new ThreadConnectionDB();

        threadConnectionDB.start();

    }

    public void cancelBillToPay(View v){


    }

    public void cancelAllBillToPay(View v){


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CustomerBillToPay.this, Morosos.class);
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

    public class ThreadConnectionDB extends Thread{
        public void run()
        {

        }
    }

}

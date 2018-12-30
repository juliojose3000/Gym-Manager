package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.MainActivity;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;

public class CustomerBillToPay extends Activity {

    private ArrayList<String> listBillToPayOfCustomer;

    private ListView listView_billToPay;

    private ManagementDatabase managementDatabase;

    private int customerId;

    private String customerName;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bill_to_pay);

        Bundle extras = getIntent().getExtras();

        customerId = extras.getInt("customer_id");

        customerName = extras.getString("customer_name");

        managementDatabase = new ManagementDatabase();

        title = findViewById(R.id.textView_title_customer_bill_to_pay);

        listView_billToPay = findViewById(R.id.listview_bill_to_pay);

        title.setText("Días que ha llegado "+customerName+" sin pagar:");

        listBillToPayOfCustomer = managementDatabase.getListAllBillToPay(customerId);

        fillLisViewCustomers();
    }


    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                listBillToPayOfCustomer );

        listView_billToPay.setAdapter(arrayAdapter);

    }

}

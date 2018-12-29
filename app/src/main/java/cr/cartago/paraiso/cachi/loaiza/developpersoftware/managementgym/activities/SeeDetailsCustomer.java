package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class SeeDetailsCustomer extends Activity {

    private int customerId;

    private CustomerData customerData;

    private Customer customer;

    private String customerDetails;

    private TextView textView_customerDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details_customer);

        Bundle extras = getIntent().getExtras();

        customerId = extras.getInt("customer_id");

        customerData = new CustomerData();

        customer = customerData.getCustomerById(customerId);

        customerDetails = "Nombre del cliente: "+customer.getName()+" "+customer.getLastName()
                +"\n\nConocido como: "+customer.getNickname()
                +"\n\nInició el: "+customer.getStarDate()
                +"\n\nEstado: al día";

        textView_customerDetails = findViewById(R.id.textView_customer_details);

        textView_customerDetails.setText(customerDetails);


    }
}

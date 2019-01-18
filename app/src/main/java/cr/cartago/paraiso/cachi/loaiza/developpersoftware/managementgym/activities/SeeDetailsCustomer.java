package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class SeeDetailsCustomer extends Activity {

    private int customerId;

    private Customer customer;

    private String customerDetails;

    private TextView textView_customerDetails;

    private boolean isDefaulter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details_customer);

        Bundle extras = getIntent().getExtras();

        customerId = extras.getInt("customer_id");

        customer = CustomerData.getCustomerById(customerId);

        isDefaulter = CustomerData.theCustomerIsDefaulter(customerId);

        String customerStatus;

        if(isDefaulter){
            customerStatus = "El cliente está moroso";
        }else{
            customerStatus = "El cliente está al día";
        }

        if(CustomerData.customerHaveCurrentPayment(customerId)){



        }






        customerDetails = "Nombre del cliente: "+customer.getName()+" "+customer.getLastName();

        if(!customer.getNickname().equals("")){
            customerDetails+="\n\nConocido(a) como: "+customer.getNickname();
        }
        customerDetails+="\n\nInició el: "+CustomerData.getDateForShowUser(customer.getStarDate().toString())
                +"\n\n"+customerStatus;

        textView_customerDetails = findViewById(R.id.textView_customer_details);

        textView_customerDetails.setText(customerDetails);


    }
}

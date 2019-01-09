package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Payment;

public class SeeDetailsCustomer extends Activity {

    private int customerId;

    private Customer customer;

    private String customerDetails;

    private TextView textView_customerDetails;

    private boolean isDefaulter;

    private Payment payment;


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
            payment = CustomerData.getDetailsCustomerPayment(customerId);
            customerStatus+="\n\nTiene un pago vigente de "+payment.getAmuntTime()+" que cubre del: "+payment.getPayDateStart()+
                    "\nal: "+payment.getPayDateEnd();
        }






        customerDetails = "Nombre del cliente: "+customer.getName()+" "+customer.getLastName()
                +"\n\nConocido como: "+customer.getNickname()
                +"\n\nInició el: "+customer.getStarDate()
                +"\n\n"+customerStatus;

        textView_customerDetails = findViewById(R.id.textView_customer_details);

        textView_customerDetails.setText(customerDetails);


    }
}

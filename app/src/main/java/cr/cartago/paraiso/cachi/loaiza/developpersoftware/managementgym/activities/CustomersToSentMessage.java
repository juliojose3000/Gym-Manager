package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;

public class CustomersToSentMessage extends Activity {

    private String customerToSentMessage;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_to_sent_message);

        Bundle extras = getIntent().getExtras();

        customerToSentMessage = extras.getString("customers");

        textView = findViewById(R.id.textView_customersToSentMessage);

        textView.setText(customerToSentMessage);
    }
}

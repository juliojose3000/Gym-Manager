package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;

public class MainActivity extends Activity {

    EditText editText_password;

    EditText editText_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_password = findViewById(R.id.password);

        editText_username = findViewById(R.id.username);

    }


    public void accept(View v){
        Intent i = new Intent(MainActivity.this, Pesas.class);
        startActivity(i);
    }

}

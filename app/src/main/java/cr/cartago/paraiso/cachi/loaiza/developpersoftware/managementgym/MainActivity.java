package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.ManagementDatabase;

public class MainActivity extends Activity {

    EditText editText_password;

    EditText editText_username;

    ManagementDatabase managementDatabase;

    boolean aux = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText_password = findViewById(R.id.password);

        editText_username = findViewById(R.id.username);

        new Thread(new Runnable() {
            public void run() {
                managementDatabase = new ManagementDatabase();
                aux = true;
            }
        }).start();


    }


    public void accept(View v){
        while(!aux){

        }
        Intent i = new Intent(MainActivity.this, Pesas.class);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}

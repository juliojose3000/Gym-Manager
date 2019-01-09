package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;

public class MainActivity extends Activity {

    private EditText editText_password;

    private EditText editText_username;

    private ManagementDatabase managementDatabase;

    private boolean aux = false;

    private Button buttonAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText_password = findViewById(R.id.password);

        editText_username = findViewById(R.id.username);

        buttonAccept = findViewById(R.id.button_accept_mainActivity);

        this.runOnUiThread(new Runnable() {

            public void run() {

                managementDatabase = new ManagementDatabase();

                if(managementDatabase.getNotification()!=null){
                    aux = false;
                    Toast.makeText(MainActivity.this,managementDatabase.getNotification(), Toast.LENGTH_LONG).show();

                }else {

                    new Thread(new Runnable() {
                        public void run() {
                            managementDatabase.fillAllList();
                            aux = true;
                        }
                    }).start();

                }
            }
        });


    }


    public void accept(View v){

        if(!aux){
            managementDatabase = new ManagementDatabase();
        }
        
        if(managementDatabase.getNotification()!=null){

            Toast.makeText(this,managementDatabase.getNotification(), Toast.LENGTH_LONG).show();

            return;

        }else {

            new Thread(new Runnable() {
                public void run() {
                    managementDatabase.fillAllList();
                    aux = true;
                }
            }).start();

        }

        if(managementDatabase.getNotification()!=null){
            return;
        }

        while(!aux){

        }
        if(managementDatabase.verifyUser(editText_username.getText().toString(), editText_password.getText().toString())){
            Intent i = new Intent(MainActivity.this, Pesas.class);
            startActivity(i);
        }else{
            Toast.makeText(this, "El usuario o contraseña están incorrectos, intente de nuevo", Toast.LENGTH_LONG).show();
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


}

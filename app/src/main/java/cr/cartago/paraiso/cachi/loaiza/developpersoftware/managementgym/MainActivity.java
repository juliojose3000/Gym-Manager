package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities.Pesas;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;

public class MainActivity extends Activity {

    EditText editText_password;

    EditText editText_username;

    public static ManagementDatabase managementDatabase;

    boolean aux = false;

    boolean pivot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText_password = findViewById(R.id.password);

        editText_username = findViewById(R.id.username);

        managementDatabase = new ManagementDatabase();

        if(managementDatabase.getNotification()!=null){

            Toast.makeText(this,managementDatabase.getNotification(), Toast.LENGTH_LONG).show();

            pivot = true;

        }else {

            new Thread(new Runnable() {
                public void run() {
                    managementDatabase.fillAllList();
                    aux = true;
                }
            }).start();

        }


    }


    public void accept(View v){

        if(pivot){

            managementDatabase = new ManagementDatabase();

            if(managementDatabase.getNotification()!=null){

                Toast.makeText(this,managementDatabase.getNotification(), Toast.LENGTH_LONG).show();

                pivot = true;

            }else {

                new Thread(new Runnable() {
                    public void run() {
                        managementDatabase.fillAllList();
                        aux = true;
                    }
                }).start();

            }

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

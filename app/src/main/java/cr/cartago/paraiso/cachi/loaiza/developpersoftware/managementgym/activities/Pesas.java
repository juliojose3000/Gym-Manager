package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.ConnectionDataBaseSQLServer;

public class Pesas extends Activity {

    private ConnectionDataBaseSQLServer connectionDataBaseSQLServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesas);

    }

    public void connectWithDatabase(View v){

        connectionDataBaseSQLServer = new ConnectionDataBaseSQLServer();

    }



}

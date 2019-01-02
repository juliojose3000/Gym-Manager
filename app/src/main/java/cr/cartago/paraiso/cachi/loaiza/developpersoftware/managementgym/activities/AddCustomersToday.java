package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class AddCustomersToday extends Activity {

    private ListView listViewCustomers;

    private ArrayList<Customer> listCustomersForAddToday;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    private boolean[] itemsChecked;

    private ManagementDatabase managementDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customers_today);

        listViewCustomers = findViewById(R.id.listview_customers);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH)+1;

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        listCustomersForAddToday = ManagementDatabase.listCustomerForAddToday;

        itemsChecked = new boolean[listCustomersForAddToday.size()];

        fillLisViewCustomers();

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!itemsChecked[position]){
                    itemsChecked[position] = true;
                }else{
                    itemsChecked[position] = false;
                }


            }

        });

        managementDatabase = new ManagementDatabase();

    }


    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                getNameAndLastNameFromListCustomer() );

        listViewCustomers.setAdapter(arrayAdapter);
        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    private ArrayList<String> getNameAndLastNameFromListCustomer(){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:listCustomersForAddToday) {

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

        }

        return listNamesAndLastNames;

    }


    /*public void accept(View v){

        //Esta linea tan satanica lo que hace es, obtener todos los items seleccionados, luego solo dejo la posiciones de los items y los
        //separo para almacenarlos en un arreglo.
        String[] numbers = listViewCustomers.getCheckedItemPositions().toString().replaceAll("\\D+","").split("(?!^)");

        if(numbers[0].equals("")){//Significa que no ha seleccionado ningun cliente
            Toast.makeText(this, "Seleccione al menos un cliente", Toast.LENGTH_LONG).show();
            return;
        }

        String today = year+"-"+month+"-"+dayOfMonth;

        for(int i = 0; i<numbers.length; i++){
            //obtengo el cliente de la posicion seleccionada en la lista y obtengo su id
            int customerId = listCustomers.get(Integer.parseInt(numbers[i])).getCustomerId();

            managementDatabase.insertCustomersOfToday(customerId, today);

        }

        Toast.makeText(this, "Se han agregado los clientes que han llegado hoy", Toast.LENGTH_LONG).show();

        Intent i = new Intent(AddCustomersToday.this, Pesas.class);
        startActivity(i);

    }*/


    public void accept(View v){

        if(!verifyInternetAccess()){
            Toast.makeText(this,"Verifique su conexión a internet e intente de nuevo",Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Integer> customersId = new ArrayList<>();

        if(areAllFalse()){
            Toast.makeText(this, "Seleccione al menos un cliente", Toast.LENGTH_LONG).show();
            return;
        }

        for(int i = 0; i<listCustomersForAddToday.size(); i++){

            if(itemsChecked[i]){
                String today = year+"-"+month+"-"+dayOfMonth;
                //obtengo el cliente de la posicion seleccionada en la lista y obtengo su id
                int customerId = listCustomersForAddToday.get(i).getCustomerId();
                customersId.add(customerId);

                managementDatabase.insertCustomersOfToday(customerId, today);

                if(!CustomerData.theCustomerHaveCurrentPayment(customerId)){
                    managementDatabase.addCustomerDefaulter(customerId, today);//si el cliente ha llegado y no tiene un pago vigente que lo cubra, se agrega a morosos
                }
            }
        }

        for(int i = 0; i<customersId.size(); i++){

            ManagementDatabase.listCustomerForAddToday.remove(CustomerData.getCustomerById(customersId.get(i)));

        }

        managementDatabase.fillAllList();

        Toast.makeText(this, "Se han agregado los clientes que han llegado hoy", Toast.LENGTH_LONG).show();

        Intent i = new Intent(AddCustomersToday.this, Pesas.class);
        startActivity(i);

    }



    public boolean areAllFalse()
    {
        for(boolean b : itemsChecked) if(b) return false;
        return true;
    }

    public boolean verifyInternetAccess(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;

    }


}

package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Dates;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class AddCustomersToday extends Activity {

    private ListView listViewCustomers;

    private ArrayList<Customer> listCustomersForAddToday;

    private boolean[] itemsChecked;

    private EditText editText_customerToSearch;

    private ArrayList<Integer> customersId;

    private Dates date;

    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customers_today);

        editText_customerToSearch = findViewById(R.id.editText_customerToSearch);

        listViewCustomers = findViewById(R.id.listview_customers);

        date = new Dates();

        listCustomersForAddToday = DBHelper.CUSTOMERS_FOR_ADD_TODAY;

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

    }

    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                CustomerData.getNameAndLastNameFromListCustomer(listCustomersForAddToday));

        listViewCustomers.setAdapter(arrayAdapter);
        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

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

        if(areAllFalse()){
            Toast.makeText(this, "Seleccione al menos un cliente", Toast.LENGTH_LONG).show();
            return;
        }

        customersId = new ArrayList<>();

        for(int i = 0; i<listCustomersForAddToday.size(); i++){

            if(itemsChecked[i]){

                //obtengo el cliente de la posicion seleccionada en la lista y obtengo su id
                customerId = listCustomersForAddToday.get(i).getCustomerId();
                customersId.add(customerId);

                /*NOTE: WHEN INSERT THE CUSTOMERS THAT ARRIVED TODAY IN THE GYM WITH A THREAD,
                * ONLY INSERT ONE CUSTOMER THE AMOUNT TIMES THAT ADD CUSTOMERS, BUT AN ASYNC TASK
                * CAN THE WORK CORRECTLY*/

                /*new Thread(){
                    public void run(){
                        DBHelper.insertCustomersOfToday(customerId, date.getDateOfToday());
                    }
                }.start();*/


                Test test = new Test();
                test.execute(""+customerId, date.getDateOfToday());


                if(!CustomerData.theCustomerHaveCurrentPayment(customerId)){// the customer doesn't have a current payment

                    if(CustomerData.theCustomerIsDefaulter(customerId)){//if the customer already is defaulter, only increases the days to pay
                        CustomerData.incrementDaysForPay(customerId);
                    }else{//on the other hand, if the customer doesn't is defaulter, it to add in the defaulters list

                        DBHelper.CUSTOMERS_DEFAULTERS.add(CustomerData.getCustomerById(customerId));

                        CustomerData.incrementDaysForPay(customerId);
                    }

                    /*new Thread(){
                        public void run(){
                            DBHelper.insertCustomerDefaulter(customerId, date.getDateOfToday());//si el cliente ha llegado y no tiene un pago vigente que lo cubra, se agrega a morosos
                        }
                    }.start();*/

                    new AsyncTask<String, Void, Void>(){

                        @Override
                        protected Void doInBackground(String... strings) {
                            DBHelper.insertCustomerDefaulter(Integer.parseInt(strings[0]), strings[1]);//si el cliente ha llegado y no tiene un pago vigente que lo cubra, se agrega a morosos
                            return null;
                        }
                    }.execute(""+customerId, date.getDateOfToday());

                }
            }
        }


        for(int i = 0; i<customersId.size(); i++){

            DBHelper.CUSTOMERS_FOR_ADD_TODAY.remove(CustomerData.getCustomerById(customersId.get(i)));

            DBHelper.CUSTOMERS_TODAY.add(CustomerData.getCustomerById(customersId.get(i)));

        }

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

    public void customerToSearch(View v){

        String nameCustomerToSearch = editText_customerToSearch.getText().toString();

        listCustomersForAddToday = CustomerData.customerToSearch(nameCustomerToSearch, DBHelper.CUSTOMERS_FOR_ADD_TODAY);

        if(listCustomersForAddToday.size()==0){
            Toast.makeText(this,"No se encontraron resultados similares",Toast.LENGTH_LONG).show();
        }

        itemsChecked = new boolean[listCustomersForAddToday.size()];

        fillLisViewCustomers();

        hideKeyboard();

    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public class ThreadConnectionDB extends Thread{
        public void run()
        {
            DBHelper.insertCustomersOfToday(customerId, date.getDateOfToday());
        }
    }

    private class Test extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            DBHelper.insertCustomersOfToday(Integer.parseInt(params[0]), params[1]);
            return null;
        }
    }


}

package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.CustomerData;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.Date;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database.DBHelper;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.CustomerToday;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.DefaulterCustomer;

public class AddCustomersToday extends Activity {

    private ListView listViewCustomers;

    private ArrayList<Customer> listCustomersForAddToday;

    private boolean[] itemsChecked;

    private EditText editText_customerToSearch;

    private ArrayList<Integer> customersId;

    private ThreadConnectionDB threadConnectionDB;

    private Date date;

    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customers_today);

        editText_customerToSearch = findViewById(R.id.editText_customerToSearch);

        listViewCustomers = findViewById(R.id.listview_customers);

        date = new Date();

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

        threadConnectionDB = new ThreadConnectionDB();

        threadConnectionDB.start();

    }

    public void fillLisViewCustomers() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                getNameAndLastNameFromListCustomer(listCustomersForAddToday));

        listViewCustomers.setAdapter(arrayAdapter);
        listViewCustomers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    private ArrayList<String> getNameAndLastNameFromListCustomer(ArrayList<Customer> listCustomersForAddToday){

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper.insertCustomersOfToday(customerId, date.getDateOfToday());
                    }
                }).start();

                if(!CustomerData.theCustomerHaveCurrentPayment(customerId)){// the customer doesn't have a current payment

                    if(CustomerData.theCustomerIsDefaulter(customerId)){//if the customer already is defaulter, only increases the days to pay
                        CustomerData.incrementDaysForPay(customerId);
                    }else{//on the other hand, if the customer doesn't is defaulter, it to add in the defaulters list

                        DBHelper.CUSTOMERS_DEFAULTERS.add(CustomerData.getCustomerById(customerId));

                        CustomerData.incrementDaysForPay(customerId);
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DBHelper.insertCustomerDefaulter(customerId, date.getDateOfToday());//si el cliente ha llegado y no tiene un pago vigente que lo cubra, se agrega a morosos
                        }
                    }).start();


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

        }
    }


}

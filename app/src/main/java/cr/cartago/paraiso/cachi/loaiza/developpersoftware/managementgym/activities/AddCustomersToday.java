package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.R;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data.ManagementDatabase;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;
import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.CustomerToday;

public class AddCustomersToday extends Activity {

    private ManagementDatabase managementDatabase;

    ListView listViewCustomers;

    ArrayList<Customer> listCustomers;

    ArrayList<CustomerToday> listCustomersToday;

    private int year, month, dayOfMonth;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customers_today);

        listViewCustomers = findViewById(R.id.listview_customers);

        managementDatabase = new ManagementDatabase();// Creo una nueva conexion

        listCustomers = managementDatabase.addCustomerToday();

        fillLisViewCustomers();

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            }

        });

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);



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

        for (Customer customer:listCustomers) {

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

        }

        return listNamesAndLastNames;

    }


    public void accept(View v){

        //Esta linea tan satanica lo que hace es, obtener todos los items seleccionados, luego solo dejo la posiciones de los items y los
        //separo para almacenarlos en un arreglo.
        String[] numbers = listViewCustomers.getCheckedItemPositions().toString().replaceAll("\\D+","").split("(?!^)");

        String today = year+"-"+month+"-"+dayOfMonth;

        for(int i = 0; i<numbers.length; i++){
            //obtengo el cliente de la posicion seleccionada en la lista y obtengo su id
            int customerId = listCustomers.get(Integer.parseInt(numbers[i])).getCustomerId();

            managementDatabase.insertCustomersOfToday(customerId, today);

        }

        Toast.makeText(this, "Se han agregado los clientes que han llegado hoy", Toast.LENGTH_LONG).show();

        Intent i = new Intent(AddCustomersToday.this, Pesas.class);
        startActivity(i);

    }


}

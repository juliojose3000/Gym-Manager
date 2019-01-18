package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models.Customer;

public class CustomerData {

    public CustomerData(){


    }

    public static ArrayList<Customer> customerForAddToday(){

        ArrayList<Customer> listCustomersForAddToday = new ArrayList<>();

        for (Customer customer:
                ManagementDatabase.listAllCustomer) {
            if(!existsCustomerInCustomerOfToday(customer)){
                listCustomersForAddToday.add(customer);
            }
        }

        return listCustomersForAddToday;

    }

    public static boolean existsCustomerInCustomerOfToday(Customer customerToSearh){

        for (Customer customer:
                ManagementDatabase.listCustomersOfToday) {
            if(customer.getCustomerId()==customerToSearh.getCustomerId()){
                return true;
            }
        }
        return false;

    }

    public static boolean theCustomerHaveCurrentPayment(int customerId){

        for (Customer customer:
                ManagementDatabase.listAllCustomerWithCurrentPayment) {
            if(customer.getCustomerId()==customerId){
                return true;
            }
        }
        return false;

    }

    public static boolean theCustomerIsDefaulter(int customerId){

        for (Customer customer:
                ManagementDatabase.listAllDefaulterCustomers) {
            if(customer.getCustomerId()==customerId){
                return true;
            }
        }
        return false;

    }

    public static Customer getCustomerById(int id){

        Customer customer = null;

        for (Customer currentCustomer:
                ManagementDatabase.listAllCustomer) {
            if(currentCustomer.getCustomerId()==id){
                customer = currentCustomer;
                break;
            }
        }

        return customer;

    }

    public static ArrayList<String> getNameAndLastNameFromListCustomer(ArrayList<Customer> listCustomers){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:listCustomers) {

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

        }

        return listNamesAndLastNames;

    }

    public static ArrayList<String> getNameAndLastNameFromListCustomerInSpecificDay(ArrayList<Customer> listCustomers){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        if(listCustomers==null){

        }else{

            for (Customer customer:listCustomers) {

                listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName());

            }

        }

        return listNamesAndLastNames;

    }

    public static ArrayList<String> getAllDefaulters(){

        ArrayList<String> listNamesAndLastNames = new ArrayList<>();

        for (Customer customer:ManagementDatabase.listAllDefaulterCustomers) {

            String cantDias="";

            if(customer.getDaysToPay()==1){
                cantDias=" día";
            }else{
                cantDias=" días";
            }

            listNamesAndLastNames.add(customer.getName()+" "+customer.getLastName()+": "+customer.getDaysToPay()+cantDias);

        }

        return listNamesAndLastNames;

    }

    public static void reduceBillToPayToCustomer(int customerId){

        for (Customer customer:ManagementDatabase.listAllDefaulterCustomers) {

            if(customer.getCustomerId()==customerId){

                if(customer.getDaysToPay()==1){

                    removeFromDefaulters(customerId);

                    break;

                }else{

                    customer.setDaysToPay(customer.getDaysToPay()-1);

                    break;

                }
            }

        }

    }

    private static void removeFromDefaulters(int customerId){

        for (int i = 0; i<ManagementDatabase.listAllDefaulterCustomers.size(); i++) {
            Customer customer = ManagementDatabase.listAllDefaulterCustomers.get(i);
            if(customer.getCustomerId()==customerId){
                ManagementDatabase.listAllDefaulterCustomers.remove(i);
            }
        }

    }

    public static Payment getDetailsCustomerPayment(int customerId){

        Payment payment = null;

        for (Payment currentPayment:
                ManagementDatabase.listCustomersWithPaymentStillInForse) {
            if(currentPayment.getCustomerId()==customerId){
                payment = currentPayment;
                break;
            }
        }

        return payment;


    }

    public static boolean customerHaveCurrentPayment(int customerId){

        for (Payment currentPayment:
                ManagementDatabase.listCustomersWithPaymentStillInForse) {
            if(currentPayment.getCustomerId()==customerId){
                return true;
            }
        }

        return false;


    }

    public static ArrayList<Customer> customerToSearch(String customerToSearch, ArrayList<Customer> customers){

        ArrayList<Customer> customersToSearch = new ArrayList<>();

        for (Customer currentCustomer:
                customers) {

            if(containsIgnoreCase(currentCustomer.getName(), customerToSearch)  || containsIgnoreCase(currentCustomer.getLastName(), customerToSearch)){
                customersToSearch.add(currentCustomer);
            }

        }

        return customersToSearch;

    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static String getDateForShowUser(String date){

        String[] dateParts = date.split("-");

        String day = dateParts[2];

        String month = getMonthByNum(Integer.parseInt(dateParts[1])-1);

        String year = dateParts[0];

        return day+" "+month+" "+year;

    }

    public static String getMonthByNum(int i){
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dec"};
        return months[i];
    }

    public static String getDateForDB(String date){

        if(date.contains(",")){
            String[] newDate = date.split(",");
            date = newDate[1].substring(1);
        }

        String[] dateParts = date.split(" ");

        String day = dateParts[0];

        String month = getNumByMonth(dateParts[1]);

        String year = dateParts[2];

        return year+"-"+month+"-"+day;
    }

    private static String getNumByMonth(String month){

        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dec"};

        for(int i = 0; i< months.length; i++){
            if(months[i].equals(month)){
                return ""+(++i);
            }
        }

        return "";
    }

    public static void incrementDaysForPay(int customerId){

        for (Customer customer:
             ManagementDatabase.listAllDefaulterCustomers) {
            if(customer.getCustomerId()==customerId){
                customer.setDaysToPay(customer.getDaysToPay()+1);
            }
        }

    }

    public static String getDayName(String inputDate){

        String[] dayNames = {"Lunes","Martes","Miércoles","Jueves", "Viernes", "Sábado", "Domingo"};

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

        if(day.equals("Monday")){
            return dayNames[0];
        }else if(day.equals("Tuesday")){
            return dayNames[1];
        }else if(day.equals("Wednesday")){
            return dayNames[2];
        }else if(day.equals("Thursday")){
            return dayNames[3];
        }else if(day.equals("Friday")){
            return dayNames[4];
        }else if(day.equals("Saturday")){
            return dayNames[5];
        }else if(day.equals("Sunday")){
            return dayNames[6];
        }

        return "";
    }


}

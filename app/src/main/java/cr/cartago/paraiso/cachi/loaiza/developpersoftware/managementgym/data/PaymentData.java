package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;


public class PaymentData {

    private ManagementDatabase managementDatabase;


    public PaymentData(){

        managementDatabase = new ManagementDatabase();

    }

    public void getAllDefaulters(){

    }

    public void addDefaulter(int customerId, String date){

        if (!CustomerData.theCustomerHaveCurrentPayment(customerId)) {

            managementDatabase.addCustomerDefaulter(customerId, date);

        }

    }

}

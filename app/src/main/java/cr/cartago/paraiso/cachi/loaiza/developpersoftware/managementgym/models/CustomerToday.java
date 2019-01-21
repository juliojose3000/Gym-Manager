package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models;

public class CustomerToday {

    private int customerTodayId;
    private int customerId;
    private String dateArrived;

    public CustomerToday(int customerTodayId, int customerId, String dateArrived) {
        this.customerTodayId = customerTodayId;
        this.customerId = customerId;
        this.dateArrived = dateArrived;
    }

    public CustomerToday(int customerId, String dateArrived) {
        this.customerId = customerId;
        this.dateArrived = dateArrived;
    }

    public int getCustomerTodayId() {
        return customerTodayId;
    }

    public void setCustomerTodayId(int customerTodayId) {
        this.customerTodayId = customerTodayId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(String dateArrived) {
        this.dateArrived = dateArrived;
    }
}

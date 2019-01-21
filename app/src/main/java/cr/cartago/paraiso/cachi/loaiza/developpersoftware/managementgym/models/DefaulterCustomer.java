package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models;

public class DefaulterCustomer {

    private int defaulterCustomerId;
    private int customerId;
    private String dateArrived;

    public DefaulterCustomer(int defaulterCustomerId, int customerId, String dateArrived) {
        this.defaulterCustomerId = defaulterCustomerId;
        this.customerId = customerId;
        this.dateArrived = dateArrived;
    }

    public DefaulterCustomer(int customerId, String dateArrived) {
        this.customerId = customerId;
        this.dateArrived = dateArrived;
    }

    public int getDefaulterCustomerId() {
        return defaulterCustomerId;
    }

    public void setDefaulterCustomerId(int defaulterCustomerId) {
        this.defaulterCustomerId = defaulterCustomerId;
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

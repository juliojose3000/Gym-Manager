package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models;

public class Payment {

    private int paymentId;

    private int customerId;

    private String payDateStart;

    private String payDateEnd;

    private String amuntTime;

    public Payment() {

    }

    public Payment(int customerId, String payDateStart, String payDateEnd, String amuntTime) {
        this.customerId = customerId;
        this.payDateStart = payDateStart;
        this.payDateEnd = payDateEnd;
        this.amuntTime = amuntTime;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPayDateStart() {
        return payDateStart;
    }

    public void setPayDateStart(String payDateStart) {
        this.payDateStart = payDateStart;
    }

    public String getPayDateEnd() {
        return payDateEnd;
    }

    public void setPayDateEnd(String payDateEnd) {
        this.payDateEnd = payDateEnd;
    }

    public String getAmuntTime() {
        return amuntTime;
    }

    public void setAmuntTime(String amuntTime) {
        this.amuntTime = amuntTime;
    }
}

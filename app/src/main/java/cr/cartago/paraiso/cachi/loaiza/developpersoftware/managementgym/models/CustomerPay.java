package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models;

public class CustomerPay {

    private int customerPayId;
    private int customerId;
    private String payDate;
    private String payEnd;
    private String amountTime;

    public int getCustomerPayId() {
        return customerPayId;
    }

    public void setCustomerPayId(int customerPayId) {
        this.customerPayId = customerPayId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayEnd() {
        return payEnd;
    }

    public void setPayEnd(String payEnd) {
        this.payEnd = payEnd;
    }

    public String getAmountTime() {
        return amountTime;
    }

    public void setAmountTime(String amountTime) {
        this.amountTime = amountTime;
    }
}

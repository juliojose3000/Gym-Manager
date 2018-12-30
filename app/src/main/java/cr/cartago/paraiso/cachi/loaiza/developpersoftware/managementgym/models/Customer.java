package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models;

import java.sql.Date;

public class Customer {

    private int customerId;

    private String name;

    private String lastName;

    private String nickname;

    private Date starDate;

    private int daysToPay;

    public Customer(String name, String lastName, String nickname, Date starDate) {
        this.name = name;
        this.lastName = lastName;
        this.nickname = nickname;
        this.starDate = starDate;
    }

    public Customer() {
    }

    public int getDaysToPay() {
        return daysToPay;
    }

    public void setDaysToPay(int daysToPay) {
        this.daysToPay = daysToPay;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getStarDate() {
        return starDate;
    }

    public void setStarDate(Date starDate) {
        this.starDate = starDate;
    }

    public String getNickname() {
        if(nickname==null){
            return "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

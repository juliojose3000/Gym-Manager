package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.models;

import java.sql.Date;

public class Customer {

    private int customerId;

    private String name;

    private String lastName;

    private String nickname;

    private String starDate;

    private int daysToPay;

    private String phoneNumber;


    public Customer(int customerId, String name, String lastName, String nickname, String starDate, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.lastName = lastName;
        this.nickname = nickname;
        this.starDate = starDate;
        this.phoneNumber = phoneNumber;
    }



    public Customer() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getStarDate() {
        return starDate;
    }

    public void setStarDate(String starDate) {
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

package com.example.callcenter.modes;

enum CallStatus{

}

public class Call {
    private String Number;
    private String Name;
    private String Date;

    public Call() {
    }

    public Call(String number, String name, String date) {
        this.Number = number;
        this.Name = name;
        this.Date = date;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }
}

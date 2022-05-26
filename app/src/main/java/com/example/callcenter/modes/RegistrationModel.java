package com.example.callcenter.modes;

public class RegistrationModel {

    private String Name;
    private String Login;
    private String Password;

    public RegistrationModel() {
    }

    public RegistrationModel(String name, String login, String password) {
        Name = name;
        Login = login;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

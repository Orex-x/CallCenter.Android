package com.example.callcenter.modes;

public class User {
    public int Id;
    public String Name;

    public String Token;
    public String Login;
    public String Password;

    public User() {
    }

    public User(int id, String name, String token, String login, String password) {
        Id = id;
        Name = name;
        Token = token;
        Login = login;
        Password = password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
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

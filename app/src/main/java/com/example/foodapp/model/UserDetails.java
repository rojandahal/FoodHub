package com.example.foodapp.model;

public class UserDetails {
    String first_name;
    String last_name;
    String phone;
    String password;

    public UserDetails() {
    }

    public UserDetails(String first_name, String last_name, String phone, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

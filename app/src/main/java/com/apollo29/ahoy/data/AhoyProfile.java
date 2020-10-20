package com.apollo29.ahoy.data;

import com.google.gson.Gson;

import java.util.Objects;

public class AhoyProfile {

    private final String firstname;
    private final String lastname;
    private final String address;
    private final String birthday;
    private final String mobile;
    private final String email;

    public AhoyProfile(String firstname, String lastname, String address, String birthday, String mobile, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.birthday = birthday;
        this.mobile = mobile;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AhoyProfile that = (AhoyProfile) o;
        return Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(address, that.address) &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, address, birthday, mobile, email);
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static AhoyProfile fromJson(String json){
        return new Gson().fromJson(json, AhoyProfile.class);
    }
}

package com.apollo29.ahoy.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AhoyProfile {

    @SerializedName("firstname")
    @Expose
    public String firstname;

    @SerializedName("lastname")
    @Expose
    public String lastname;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("birthday")
    @Expose
    public Long birthday;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("email")
    @Expose
    public String email;

    public AhoyProfile(){

    }

    public AhoyProfile(String firstname, String lastname, String address, Long birthday, String mobile, String email) {
        super();
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

    public Long getBirthday() {
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

    @Override
    public String toString() {
        return "AhoyProfile{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static String toJson(AhoyProfile profile){
        return new Gson().toJson(profile, AhoyProfile.class);
    }

    public static AhoyProfile fromJson(String json){
        return new Gson().fromJson(json, AhoyProfile.class);
    }
}

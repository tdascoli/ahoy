package com.apollo29.ahoy.comm.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Credentials implements Serializable {

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("password")
    @Expose
    private String password;

    /**
     * No args constructor for use in serialization
     *
     */
    public Credentials() {
    }

    /**
     * Credentials for Backend
     * @param deviceId deviceId
     * @param password password
     */
    public Credentials(String deviceId, String password) {
        super();
        this.deviceId = deviceId;
        this.password = password;
    }

    public static Credentials of(String deviceId, String password) {
        return new Credentials(deviceId, password);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPassword() {
        return password;
    }

}

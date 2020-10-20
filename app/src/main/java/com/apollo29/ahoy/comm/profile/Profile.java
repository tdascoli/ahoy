package com.apollo29.ahoy.comm.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Serializable {

    @SerializedName("uid")
    @Expose
    public int uid;

    @SerializedName("device_id")
    @Expose
    public String deviceId;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("create_date")
    @Expose
    public int create;

    @SerializedName("active")
    @Expose
    public int active;

    @SerializedName("trial")
    @Expose
    public int trial;

    /**
     * No args constructor for use in serialization
     *
     */
    public Profile(){
    }

    public Profile(int uid, String deviceId, String password, int create, int active, int trial) {
        super();
        this.uid = uid;
        this.deviceId = deviceId;
        this.password = password;
        this.create = create;
        this.active = active;
        this.trial = trial;
    }

    public static Profile of(String deviceId, String password){
        return new Profile(0, deviceId, password, 0, 1, 1);
    }
}

package com.apollo29.ahoy.comm.queue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Queue implements Serializable {

    @SerializedName("uid")
    @Expose
    public int uid;

    @SerializedName("event_uid")
    @Expose
    public int eventUid;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("birthday")
    @Expose
    private int birthday;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("timestamp")
    @Expose
    private int timestamp;

    /**
     * No args constructor for use in serialization
     *
     */
    public Queue(){
    }

    public Queue(int uid, int eventUid, String firstname, String lastname, String address, int birthday, String mobile, String email, int timestamp) {
        super();
        this.uid = uid;
        this.eventUid = eventUid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.birthday = birthday;
        this.mobile = mobile;
        this.email = email;
        this.timestamp = timestamp;
    }
}

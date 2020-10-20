package com.apollo29.ahoy.comm.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {

    @SerializedName("uid")
    @Expose
    public Integer uid;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("profile_id")
    @Expose
    public Integer profileId;

    @SerializedName("secret")
    @Expose
    public String secret;

    @SerializedName("date")
    @Expose
    public int date;

    @SerializedName("active")
    @Expose
    public int active;

    public Event(){
    }

    public Event(int uid, String title, int profileId, String secret, int date, int active) {
        super();
        this.uid = uid;
        this.title = title;
        this.profileId = profileId;
        this.secret = secret;
        this.date = date;
        this.active = active;
    }

    public static Event of(String title, int profileId, String secret, int date){
        return new Event(0, title, profileId, secret, date, 1);
    }

    public static Event empty(){
        return new Event();
    }

    public boolean isEmpty(){
        return uid==null;
    }
}

package com.apollo29.ahoy.comm.event;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "event",
        indices = {@Index("uid"), @Index("date")})
public class Event implements Serializable {

    @PrimaryKey
    @SerializedName("uid")
    @Expose
    public Integer uid;

    @SerializedName("title")
    @Expose
    public String title;

    @ColumnInfo(name = "profile_id")
    @SerializedName("profile_id")
    @Expose
    public Integer profileId;

    @SerializedName("secret")
    @Expose
    public String secret;

    @SerializedName("date")
    @Expose
    public Long date;

    @SerializedName("active")
    @Expose
    public int active;

    @Ignore
    public Event(){
    }

    public Event(int uid, String title, int profileId, String secret, Long date, int active) {
        super();
        this.uid = uid;
        this.title = title;
        this.profileId = profileId;
        this.secret = secret;
        this.date = date;
        this.active = active;
    }

    @Ignore
    public static Event of(String title, int profileId, String secret, Long date){
        return new Event(0, title, profileId, secret, date, 1);
    }

    @Ignore
    public static Event empty(){
        return new Event();
    }

    @Ignore
    public boolean isEmpty(){
        return uid==null;
    }
}

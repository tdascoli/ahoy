package com.apollo29.ahoy.comm.queue;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.apollo29.ahoy.view.events.EventUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "queue",
        indices = {@Index("uid"), @Index("event_id")})
public class Queue implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("uid")
    @Expose
    public Integer uid;

    @SerializedName("event_id")
    @ColumnInfo(name = "event_id")
    @Expose
    public Integer eventId;

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
    public long birthday;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("timestamp")
    @Expose
    public long timestamp;

    @Ignore
    public Queue(){
    }

    public Queue(Integer uid, Integer eventId, String firstname, String lastname, String address, long birthday, String mobile, String email, long timestamp) {
        super();
        this.uid = uid;
        this.eventId = eventId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.birthday = birthday;
        this.mobile = mobile;
        this.email = email;
        this.timestamp = timestamp;
    }

    @Ignore
    public String[] asArray(){
        return new String[]{
            this.firstname,
            this.lastname,
            this.address,
            EventUtil.getDate(this.birthday),
            this.mobile,
            this.email,
            EventUtil.getCheckIn(this.timestamp)
        };
    }
}

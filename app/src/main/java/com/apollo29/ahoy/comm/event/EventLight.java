package com.apollo29.ahoy.comm.event;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "event",
        indices = {@Index("uid"), @Index("date")})
public class EventLight implements Serializable {

    @PrimaryKey
    @SerializedName("uid")
    @Expose
    public Integer uid;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("date")
    @Expose
    public Long date;

    @Ignore
    public EventLight(){
    }

    public EventLight(int uid, String title, Long date) {
        super();
        this.uid = uid;
        this.title = title;
        this.date = date;
    }

    @Ignore
    public static EventLight of(String title, Long date){
        return new EventLight(0, title, date);
    }

    @Ignore
    public static EventLight empty(){
        return new EventLight();
    }

    @Ignore
    public boolean isEmpty(){
        return uid==null || uid==0;
    }
}

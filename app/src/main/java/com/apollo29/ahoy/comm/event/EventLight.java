package com.apollo29.ahoy.comm.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventLight implements Serializable {

    @SerializedName("uid")
    @Expose
    public Integer uid;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("date")
    @Expose
    public Long date;

    public EventLight(){
    }

    public EventLight(int uid, String title, Long date) {
        super();
        this.uid = uid;
        this.title = title;
        this.date = date;
    }

    public static EventLight of(String title, Long date){
        return new EventLight(0, title, date);
    }

    public static EventLight empty(){
        return new EventLight();
    }

    public boolean isEmpty(){
        return uid==null || uid==0;
    }
}

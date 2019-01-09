package com.code_dream.almanach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 11/15/17.
 */

public class Notification {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("sender")
    private Person sender;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("time")
    private String time;

    public Notification(int id, Person sender, String title, String time) {
        this.id = id;
        this.sender = sender;
        this.title = title;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

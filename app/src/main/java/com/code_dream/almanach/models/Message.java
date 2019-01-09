package com.code_dream.almanach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 9/16/17.
 */

public class Message {

    @SerializedName("content")
    @Expose
    private String message;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("time")
    @Expose
    private String createdAt;
    @SerializedName("owner")
    @Expose
    private boolean owner;
    @SerializedName("token")
    @Expose
    private String token;

    public Message(String message, String person, String createdAt, boolean owner, String token) {
        this.message = message;
        this.person = person;
        this.createdAt = createdAt;
        this.owner = owner;
        this.token = token;
    }

    public Message(String message, String person, boolean owner, String token) {
        this.message = message;
        this.person = person;
        this.owner = owner;
        this.token = token;
        this.createdAt = "Just now";
    }

    public Message(String message, String person, String createdAt, boolean owner) {
        this.message = message;
        this.person = person;
        this.createdAt = createdAt;
        this.owner = owner;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public String getPerson() {
        return person;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

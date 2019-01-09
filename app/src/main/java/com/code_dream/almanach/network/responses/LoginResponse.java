package com.code_dream.almanach.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.code_dream.almanach.models.Person;

/**
 * Created by Qwerasdzxc on 2/13/17.
 */

public class LoginResponse {

    @SerializedName("response")
    @Expose
    private int response;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("person")
    @Expose
    private Person person;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

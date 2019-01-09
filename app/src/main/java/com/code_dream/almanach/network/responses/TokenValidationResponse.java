package com.code_dream.almanach.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.code_dream.almanach.models.Person;

/**
 * Created by Qwerasdzxc on 2/19/17.
 */

public class TokenValidationResponse {

    @SerializedName("new_token")
    @Expose
    private String newToken;

    @SerializedName("person")
    @Expose
    private Person person;

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

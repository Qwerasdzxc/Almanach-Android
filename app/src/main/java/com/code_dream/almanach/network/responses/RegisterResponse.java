package com.code_dream.almanach.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 2/14/17.
 */

public class RegisterResponse {

    @SerializedName("response")
    @Expose
    private int response;
    @SerializedName("token")
    @Expose
    private String token;

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
}

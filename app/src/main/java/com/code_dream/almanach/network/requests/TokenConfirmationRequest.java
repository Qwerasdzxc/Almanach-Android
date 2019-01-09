package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 9/23/17.
 */

public class TokenConfirmationRequest {

    private String token;

    public TokenConfirmationRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

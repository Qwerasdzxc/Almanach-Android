package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/17/17.
 */

public class FirebaseTokenUpdateRequest {

    private String newToken;

    public FirebaseTokenUpdateRequest(String newToken){
        this.newToken = newToken;
    }

    public String getNewToken(){
        return newToken;
    }
}

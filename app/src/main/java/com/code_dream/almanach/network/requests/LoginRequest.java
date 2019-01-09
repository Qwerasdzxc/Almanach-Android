package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/13/17.
 */

public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}

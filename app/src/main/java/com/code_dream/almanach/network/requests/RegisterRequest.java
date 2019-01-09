package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/14/17.
 */

public class RegisterRequest {

    private String name;
    private String dob;
    private String email;
    private String password;
    private String subjects;

    public RegisterRequest(String name, String dob, String email, String password, String subjects){
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.subjects = subjects;
    }

    public String getFullName(){
        return name;
    }
}

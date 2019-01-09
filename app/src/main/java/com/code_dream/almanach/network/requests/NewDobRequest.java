package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class NewDobRequest {

    private String newDob;

    public NewDobRequest(String newDob){
        this.newDob = newDob;
    }

    public String getNewDob(){
        return newDob;
    }
}

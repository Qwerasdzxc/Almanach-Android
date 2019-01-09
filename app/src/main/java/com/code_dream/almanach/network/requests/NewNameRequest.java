package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class NewNameRequest {

    private String newName;

    public NewNameRequest(String newName){
        this.newName = newName;
    }

    public String getNewName(){
        return newName;
    }
}

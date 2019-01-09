package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 9/23/17.
 */

public class LoadAllMessagesRequest {

    private int id;

    public LoadAllMessagesRequest(int id){
        this.id = id;
    }

    public int getPostId(){
        return id;
    }
}

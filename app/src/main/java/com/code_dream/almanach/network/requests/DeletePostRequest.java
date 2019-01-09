package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class DeletePostRequest {

    private int postId;

    public DeletePostRequest(int postId){
        this.postId = postId;
    }

    public int getPostId(){
        return postId;
    }
}

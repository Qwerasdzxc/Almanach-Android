package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class CreatePostRequest {

    private String postContent;

    public CreatePostRequest(String postContent){
        this.postContent = postContent;
    }

    public String getPostContent(){
        return postContent;
    }
}

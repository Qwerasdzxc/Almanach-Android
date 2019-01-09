package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class EditPostRequest {

    private int postId;
    private String postContent;

    public EditPostRequest(int postId, String postContent){
        this.postId = postId;
        this.postContent = postContent;
    }

    public int getPostId(){
        return postId;
    }

    public String getPostContent(){
        return postContent;
    }
}

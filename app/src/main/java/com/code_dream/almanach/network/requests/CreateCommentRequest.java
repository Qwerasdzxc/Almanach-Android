package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 3/16/17.
 */

public class CreateCommentRequest {

    private int commentId;

    private String commentContent;

    public CreateCommentRequest(int commentId, String commentContent){
        this.commentId = commentId;
        this.commentContent = commentContent;
    }

    public int getPostId() {
        return commentId;
    }

    public String getCommentContent(){
        return commentContent;
    }
}

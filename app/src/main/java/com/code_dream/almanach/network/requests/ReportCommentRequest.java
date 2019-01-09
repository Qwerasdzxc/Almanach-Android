package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 1/8/18.
 */

public class ReportCommentRequest {

    private int commentId;

    public ReportCommentRequest(int commentId){
        this.commentId = commentId;
    }

    public int getCommentId(){
        return commentId;
    }
}

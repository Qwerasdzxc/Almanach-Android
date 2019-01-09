package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 3/14/17.
 */

public class SchoolPostCommentsRequest {

    private int postId;
    private int lastCommentId;

    public SchoolPostCommentsRequest(int postId, int lastCommentId){
        this.postId = postId;
        this.lastCommentId = lastCommentId;
    }

    public int getPostId() {
        return postId;
    }

    public int getLastCommentId(){
        return lastCommentId;
    }
}

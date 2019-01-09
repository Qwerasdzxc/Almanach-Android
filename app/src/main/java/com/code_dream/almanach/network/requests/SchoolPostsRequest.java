package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/14/17.
 */

public class SchoolPostsRequest {

    private int lastPostId;

    public SchoolPostsRequest(int lastPostId){
        this.lastPostId = lastPostId;
    }

    public int getLastPostId(){
        return lastPostId;
    }
}

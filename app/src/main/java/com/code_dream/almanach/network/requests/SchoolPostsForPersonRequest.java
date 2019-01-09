package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 11/11/17.
 */

public class SchoolPostsForPersonRequest {

    private int personId;
    private int lastPostId;

    public SchoolPostsForPersonRequest(int personId, int lastPostId){
        this.personId = personId;
        this.lastPostId = lastPostId;
    }

    public int getLastPostId(){
        return lastPostId;
    }

    public int getPersonId() {
        return personId;
    }
}

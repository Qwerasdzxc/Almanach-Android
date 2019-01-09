package com.code_dream.almanach.network.requests;

import com.code_dream.almanach.news_feed.ReactionType;

/**
 * Created by Qwerasdzxc on 3/9/17.
 */

public class PostReactionRequest {

    private int postId;

    private ReactionType reactionType;

    public PostReactionRequest(int postId, ReactionType reactionType){
        this.postId = postId;
        this.reactionType = reactionType;
    }

    public int getPostId(){
        return postId;
    }

    public String getReactionType(){
        return reactionType.toString();
    }
}

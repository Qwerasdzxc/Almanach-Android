package com.code_dream.almanach.news_feed.network;

/**
 * Created by Qwerasdzxc on 2/2/17.
 */

public interface INewsFeedInteractor {

    void loadMorePosts(boolean refreshAll, OnNetworkRequestFinished listener, int lastPostId);

    void upvotePost(OnNetworkRequestFinished listener, int postId);

    void downvotePost(OnNetworkRequestFinished listener, int postId);

    void reportPost(OnNetworkRequestFinished listener, int postId);

    void deletePost(OnNetworkRequestFinished listener, int postId);
}

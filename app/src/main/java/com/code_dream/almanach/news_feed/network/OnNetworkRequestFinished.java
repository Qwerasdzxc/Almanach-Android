package com.code_dream.almanach.news_feed.network;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/2/17.
 */

public interface OnNetworkRequestFinished {

    void onMorePostsSuccessfullyLoaded(ArrayList<Object> schoolPosts, boolean refreshedAll);

    void onNoMorePostsToLoad();

    void onPostSuccessfullyUpvoted();

    void onPostSuccessfullyDownvoted();

    void onPostSuccessfullyReported();

    void onPostSuccessfullyDeleted();

    void onFailure();
}

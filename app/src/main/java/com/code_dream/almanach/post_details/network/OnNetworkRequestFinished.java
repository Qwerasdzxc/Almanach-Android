package com.code_dream.almanach.post_details.network;

import com.code_dream.almanach.post_details.SchoolPostComment;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 3/14/17.
 */

public interface OnNetworkRequestFinished {

    void onCommentsSuccessfullyLoaded(ArrayList<SchoolPostComment> schoolPostComments);

    void onNoMoreCommentsToLoad();

    void onPostSuccessfullyUpvoted();

    void onPostSuccessfullyDownvoted();

    void onCommentSuccessfullyCreated(SchoolPostComment schoolPostComment);

    void onCommentSuccessfullyUpvoted();

    void onCommentSuccessfullyDownvoted();

    void onCommentSuccessfullyDeleted();

    void onCommentSuccessfullyReported();

    void onCommentSuccessfullyEdited(SchoolPostComment schoolPostComment);

    void onFailure();
}

package com.code_dream.almanach.post_details.network;

/**
 * Created by Qwerasdzxc on 3/14/17.
 */

public interface IPostDetailsInteractor {

    void loadMoreComments(OnNetworkRequestFinished listener, int postId, int lastCommentId);

    void upvotePost(OnNetworkRequestFinished listener, int postId);

    void downvotePost(OnNetworkRequestFinished listener, int postId);

    void upvoteComment(OnNetworkRequestFinished listener, int commentId);

    void downvoteComment(OnNetworkRequestFinished listener, int commentId);

    void reportComment(OnNetworkRequestFinished listener, int commentId);

    void addComment(OnNetworkRequestFinished listener, int postId, String content);

    void deleteComment(OnNetworkRequestFinished listener, int commentId);

    void editComment(OnNetworkRequestFinished listener, int commentId, String content);
}

package com.code_dream.almanach.post_details;

import android.widget.ImageView;

import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.ReactionType;
import com.code_dream.almanach.post_details.network.OnNetworkRequestFinished;
import com.code_dream.almanach.post_details.network.PostDetailsInteractor;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 3/11/17.
 */

public class PostDetailsPresenter implements PostDetailsContract.UserActionsListener,
                                             OnNetworkRequestFinished {

    private SchoolPostComment selectedSchoolPostComment;

    private final PostDetailsInteractor interactor;
    private final PostDetailsContract.View view;

    public PostDetailsPresenter(PostDetailsContract.View view, PostDetailsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void setupReactions() {
        if (view.getSchoolPost().isUpvoted())
            view.boldReaction(ReactionType.LIKE);
        else if (view.getSchoolPost().isDownvoted())
            view.boldReaction(ReactionType.DISLIKE);

        if (view.getSchoolPost().getReactionCount() != 0)
            view.updateReactionCount(view.getSchoolPost().getReactionCount());
    }

    @Override
    public void loadAllComments(int postId) {
        interactor.loadMoreComments(this, postId, view.getLastCommentId());
    }

    @Override
    public void showContextMenu(ImageView imgContextMenu, SchoolPostComment item) {
        if (item.isCommentOwner())
            view.showPostOwnerContextMenu(imgContextMenu, item);
        else
            view.showPostOthersContextMenu(imgContextMenu, item);
    }

    @Override
    public void onPostUpvoteClick(SchoolPost upvotedSchoolPost) {
        if (upvotedSchoolPost.isUpvoted()) {
            upvotedSchoolPost.setPostUpvoted(false);
            view.clearReactionBolding(ReactionType.LIKE);
        }
        else if (!upvotedSchoolPost.isDownvoted()) {
            upvotedSchoolPost.setPostUpvoted(true);
            view.boldReaction(ReactionType.LIKE);
        }
        else if (upvotedSchoolPost.isDownvoted()){
            upvotedSchoolPost.setPostUpvoted(true);
            upvotedSchoolPost.setPostDownvoted(false);
            view.boldReaction(ReactionType.LIKE);
            view.clearReactionBolding(ReactionType.DISLIKE);
        }

        interactor.upvotePost(this, upvotedSchoolPost.getId());
        view.updateReactionCount(upvotedSchoolPost.getReactionCount());
    }

    @Override
    public void onPostDownvoteClick(SchoolPost downvotedSchoolPost) {
        if (downvotedSchoolPost.isDownvoted()) {
            downvotedSchoolPost.setPostDownvoted(false);
            view.clearReactionBolding(ReactionType.DISLIKE);
        }
        else if (!downvotedSchoolPost.isUpvoted()) {
            downvotedSchoolPost.setPostDownvoted(true);
            view.boldReaction(ReactionType.DISLIKE);
        }
        else {
            downvotedSchoolPost.setPostDownvoted(true);
            downvotedSchoolPost.setPostUpvoted(false);
            view.boldReaction(ReactionType.DISLIKE);
            view.clearReactionBolding(ReactionType.LIKE);
        }

        interactor.downvotePost(this, downvotedSchoolPost.getId());
        view.updateReactionCount(downvotedSchoolPost.getReactionCount());
    }

    @Override
    public void onCommentSubmitClick(String commentContent) {
        view.showLoadingToast("Adding comment...");

        interactor.addComment(this, view.getSchoolPost().getId(), commentContent);
    }

    @Override
    public void onCommentUpvoteClick(SchoolPostComment upvotedComment) {
        if (upvotedComment.isUpvoted())
            upvotedComment.setCommentUpvoted(false);

        else if (!upvotedComment.isDownvoted())
            upvotedComment.setCommentUpvoted(true);

        else if (upvotedComment.isDownvoted()){
            upvotedComment.setCommentUpvoted(true);
            upvotedComment.setCommentDownvoted(false);
        }

        interactor.upvoteComment(this, upvotedComment.getCommentId());
    }

    @Override
    public void onCommentDownvoteClick(SchoolPostComment downvotedComment) {
        if (downvotedComment.isDownvoted())
            downvotedComment.setCommentDownvoted(false);

        else if (!downvotedComment.isUpvoted())
            downvotedComment.setCommentDownvoted(true);

        else {
            downvotedComment.setCommentDownvoted(true);
            downvotedComment.setCommentUpvoted(false);
        }

        interactor.downvoteComment(this, downvotedComment.getCommentId());
    }

    @Override
    public void onCommentReplyClick(SchoolPostComment commentToReply) {
        //TODO: Update when Backend is finished

    }

    @Override
    public void deleteComment(SchoolPostComment schoolPostComment) {
        selectedSchoolPostComment = schoolPostComment;

        view.showLoadingToast("Deleting comment...");

        interactor.deleteComment(this, schoolPostComment.getCommentId());

    }

    @Override
    public void editComment(SchoolPostComment schoolPostComment) {
        //TODO: Update when Backend is finished
        this.selectedSchoolPostComment = schoolPostComment;

        //interactor.editComment(this, schoolPostComment.getCommentId(), schoolPostComment.getContent());
    }

    @Override
    public void reportComment(SchoolPostComment schoolPostComment) {
        selectedSchoolPostComment = schoolPostComment;
        view.showLoadingToast("Reporting comment...");

        interactor.reportComment(this, schoolPostComment.getCommentId());
    }


    @Override
    public void onCommentsSuccessfullyLoaded(ArrayList<SchoolPostComment> schoolPostComments) {
        view.updateRecyclerView(schoolPostComments);
    }

    @Override
    public void onNoMoreCommentsToLoad() {
        
    }

    @Override
    public void onPostSuccessfullyUpvoted() {
        
    }

    @Override
    public void onPostSuccessfullyDownvoted() {
        
    }

    @Override
    public void onCommentSuccessfullyCreated(SchoolPostComment schoolPostComment) {
        view.dismissLoadToastSuccessfully();
        view.addNewCommentToRecyclerView(schoolPostComment);
    }

    @Override
    public void onCommentSuccessfullyUpvoted() {
        
    }

    @Override
    public void onCommentSuccessfullyDownvoted() {
        
    }

    @Override
    public void onCommentSuccessfullyDeleted() {
        view.dismissLoadToastSuccessfully();
        view.removeCommentFromRecyclerView(selectedSchoolPostComment);
    }

    @Override
    public void onCommentSuccessfullyReported() {
        view.dismissLoadToastSuccessfully();
    }

    @Override
    public void onCommentSuccessfullyEdited(SchoolPostComment schoolPostComment) {

    }

    @Override
    public void onFailure() {
        view.dismissLoadToastWithError();
    }
}

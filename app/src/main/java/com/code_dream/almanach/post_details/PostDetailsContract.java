package com.code_dream.almanach.post_details;

import android.support.annotation.UiThread;
import android.widget.ImageView;

import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.ReactionType;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 3/11/17.
 */

public interface PostDetailsContract {

    @UiThread
    interface View {

        int getLastCommentId();

        SchoolPost getSchoolPost();

        void showLoadingToast(String text);

        void dismissLoadToastSuccessfully();

        void dismissLoadToastWithError();

        void displayReactionCount(boolean display);

        void updateReactionCount(int reactionCount);

        void boldReaction(ReactionType reactionType);

        void clearReactionBolding(ReactionType reactionType);

        void updateRecyclerView(ArrayList<SchoolPostComment> schoolPostComments);

        void addNewCommentToRecyclerView(SchoolPostComment schoolPostComment);

        void removeCommentFromRecyclerView(SchoolPostComment schoolPostComment);

        void showPostOwnerContextMenu(ImageView imgContextMenu, SchoolPostComment schoolPostComment);

        void showPostOthersContextMenu(ImageView imgContextMenu, SchoolPostComment schoolPostComment);
    }

    interface UserActionsListener {

        void onPostUpvoteClick(SchoolPost upvotedSchoolPost);

        void onPostDownvoteClick(SchoolPost downvotedSchoolPost);

        void onCommentSubmitClick(String commentContent);

        void onCommentUpvoteClick(SchoolPostComment upvotedComment);

        void onCommentDownvoteClick(SchoolPostComment downvotedComment);

        void onCommentReplyClick(SchoolPostComment commentToReply);

        void deleteComment(SchoolPostComment schoolPostComment);

        void editComment(SchoolPostComment schoolPostComment);

        void reportComment(SchoolPostComment schoolPostComment);

        void showContextMenu(ImageView imgContextMenu, SchoolPostComment item);

        void loadAllComments(int postId);

        void setupReactions();
    }
}

package com.code_dream.almanach.news_feed;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.UiThread;
import android.widget.ImageView;

import com.code_dream.almanach.models.SchoolPost;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/2/17.
 */

public interface NewsFeedContract {

    @UiThread
    interface View {

        void addNewSchoolPostsToRecyclerView(ArrayList<Object> newSchoolPosts);

        void notifyAdapterNoMorePostsToLoad();

        void addProgressBarItemToRecyclerView();

        void removeProgressBarItemFromRecyclerView();

        void setSwipeRefreshLayoutRefreshing(boolean refreshing);

        void swapSchoolPostsInAdapter(ArrayList<Object> schoolPosts);

        void setProgressBarVisibility(int visibility);

        void setMoreAdapterDataAvailable();

        void showPostOwnerContextMenu(ImageView imgContextMenu, SchoolPost schoolPost);

        void showPostOthersContextMenu(ImageView imgContextMenu, SchoolPost schoolPost);

        int getLastPostId();

        int getPostId(SchoolPost schoolPost);

        boolean isAdapterBinding();

        int layoutManagerGetItemCount();

        int layoutManagerFindLastVisibleItemPosition();

        void dismissLoadToastWithError();

        void dismissLoadToastSuccessfully();

        void showPostReportingToast();

        void showPostDeletingToast();

        void showEmptyDataSetView(boolean show);

        void removePostFromList(SchoolPost schoolPostToRemove);

        void startAddEditActivity();

        void startAddEditActivity(Uri imageUri);

        void startAddEditActivity(int postId, String postContent);

        void showServerErrorSnackbar();

        void showBottomSheet();

        void dismissBottomSheet();

        void onNewPhotoRequested();

        void onImageFromGalleryRequested();

        void openCameraActivity();

        void openImageSelectionActivity();

        void openPostDetailsActivity(SchoolPost selectedSchoolPost, byte[] selectedPostImage);

        void updatePostDetails(SchoolPost updatedSchoolPost, int originalPostPosition);

        void addCreatedPost(SchoolPost createdSchoolPost);

        boolean isRecyclerViewEmpty();
    }

    interface UserActionsListener {

        void onScrolled();

        void loadAllPostsFirstTime();

        void refreshAllPosts(boolean progressBarVisible);

        void loadMorePosts();

        void updatePostDetails(SchoolPost schoolPostToUpdate);

        void addCreatedPost(SchoolPost createdSchoolPost);

        void showContextMenu(ImageView imgContextMenu, SchoolPost schoolPost);

        void onAddNewPostClick();

        void onPostClick(SchoolPost selectedSchoolPost, int selectedPostPosition, Bitmap imageBitmap);

        void onUpvoteClick(SchoolPost upvotedSchoolPost);

        void onDownvoteClick(SchoolPost downvotedSchoolPost);

        void editPost(SchoolPost schoolPost, int selectedPostPosition);

        void reportPost(SchoolPost schoolPost);

        void deletePost(SchoolPost schoolPost);

        void onAddPhotoClick();

        void onNewPictureRequested();

        void onGalleryPictureRequested();
    }
}

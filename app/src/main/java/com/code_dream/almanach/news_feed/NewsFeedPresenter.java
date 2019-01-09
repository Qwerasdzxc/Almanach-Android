package com.code_dream.almanach.news_feed;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.network.NewsFeedInteractor;
import com.code_dream.almanach.news_feed.network.OnNetworkRequestFinished;
import com.code_dream.almanach.utility.Registry;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/2/17.
 */

public class NewsFeedPresenter implements NewsFeedContract.UserActionsListener, OnNetworkRequestFinished {

    private NewsFeedContract.View view;
    private NewsFeedInteractor newsFeedInteractor;

    // Trigger boolean for one time refresh onViewCreated
    private boolean firstTimeRefresh = true;

    // Position of the selected SchoolPost, used in the View
    // to replace the original one with the edited from the Post Details Activity
    private int selectedPostPosition;

    private SchoolPost selectedSchoolPost;

    public NewsFeedPresenter(NewsFeedContract.View view){
        this.view = view;
        newsFeedInteractor = new NewsFeedInteractor();
    }

    @Override
    public void loadAllPostsFirstTime() {
        if (firstTimeRefresh) {
            newsFeedInteractor.loadMorePosts(true, this, Registry.LOAD_ALL_POSTS_CODE);
            firstTimeRefresh = false;
        } else
            view.setProgressBarVisibility(View.GONE);
    }

    @Override
    public void refreshAllPosts(boolean progressBarVisible) {
        // Hide the Empty data set view.
        view.showEmptyDataSetView(false);

        if (!view.isAdapterBinding())
            if (progressBarVisible)
                view.setProgressBarVisibility(View.VISIBLE);

        newsFeedInteractor.loadMorePosts(true, this, Registry.LOAD_ALL_POSTS_CODE);
    }

    @Override
    public void loadMorePosts() {
        if (!view.isRecyclerViewEmpty()) {
            newsFeedInteractor.loadMorePosts(false, this, view.getLastPostId());
            view.addProgressBarItemToRecyclerView();
        }
    }

    @Override
    public void onScrolled() {
        if (view.layoutManagerFindLastVisibleItemPosition() >= view.layoutManagerGetItemCount() - 10)
            view.setMoreAdapterDataAvailable();
    }

    @Override
    public void showContextMenu(ImageView imgContextMenu, SchoolPost schoolPost) {
        if (schoolPost.getOwner())
            view.showPostOwnerContextMenu(imgContextMenu, schoolPost);
        else
            view.showPostOthersContextMenu(imgContextMenu, schoolPost);
    }

    @Override
    public void onPostClick(SchoolPost selectedSchoolPost, int selectedPostPosition, Bitmap imageBitmap) {
        this.selectedPostPosition = selectedPostPosition;

        if (imageBitmap != null) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte[] selectedPostImageByteArray = stream.toByteArray();


            view.openPostDetailsActivity(selectedSchoolPost, selectedPostImageByteArray);
        } else {
            view.openPostDetailsActivity(selectedSchoolPost, null);
        }
    }

    @Override
    public void onUpvoteClick(SchoolPost upvotedSchoolPost) {
        if (upvotedSchoolPost.isUpvoted())
            upvotedSchoolPost.setPostUpvoted(false);

        else if (!upvotedSchoolPost.isDownvoted())
            upvotedSchoolPost.setPostUpvoted(true);

        else if (upvotedSchoolPost.isDownvoted()){
            upvotedSchoolPost.setPostUpvoted(true);
            upvotedSchoolPost.setPostDownvoted(false);
        }

        newsFeedInteractor.upvotePost(this, upvotedSchoolPost.getId());
    }

    @Override
    public void onDownvoteClick(SchoolPost downVotedSchoolPost) {
        if (downVotedSchoolPost.isDownvoted())
            downVotedSchoolPost.setPostDownvoted(false);

        else if (!downVotedSchoolPost.isUpvoted())
            downVotedSchoolPost.setPostDownvoted(true);

        else {
            downVotedSchoolPost.setPostDownvoted(true);
            downVotedSchoolPost.setPostUpvoted(false);
        }

        newsFeedInteractor.downvotePost(this, downVotedSchoolPost.getId());
    }

    @Override
    public void onAddNewPostClick() {
        view.startAddEditActivity();
    }

    @Override
    public void onAddPhotoClick() {
        view.showBottomSheet();
    }

    @Override
    public void onNewPictureRequested() {
        view.onNewPhotoRequested();
    }

    @Override
    public void onGalleryPictureRequested() {
        view.onImageFromGalleryRequested();
    }

    @Override
    public void addCreatedPost(SchoolPost createdSchoolPost) {
        view.addCreatedPost(createdSchoolPost);
    }

    @Override
    public void updatePostDetails(SchoolPost schoolPostToUpdate) {
        view.updatePostDetails(schoolPostToUpdate, selectedPostPosition);
    }

    @Override
    public void editPost(SchoolPost schoolPost, int selectedPostPosition) {
        this.selectedPostPosition = selectedPostPosition;

        selectedSchoolPost = schoolPost;
        view.startAddEditActivity(schoolPost.getId(), schoolPost.getContent());
    }

    @Override
    public void reportPost(SchoolPost schoolPost) {
        selectedSchoolPost = schoolPost;
        view.showPostReportingToast();
        newsFeedInteractor.reportPost(this, view.getPostId(schoolPost));
    }

    @Override
    public void deletePost(SchoolPost schoolPost) {
        selectedSchoolPost = schoolPost;
        view.showPostDeletingToast();
        newsFeedInteractor.deletePost(this, view.getPostId(schoolPost));
    }

    @Override
    public void onMorePostsSuccessfullyLoaded(ArrayList<Object> schoolPosts, boolean refreshedAll) {
        if (refreshedAll) {
            view.setProgressBarVisibility(View.GONE);
            view.swapSchoolPostsInAdapter(schoolPosts);
            view.setSwipeRefreshLayoutRefreshing(false);
            view.showEmptyDataSetView(schoolPosts.isEmpty());
        }
        else {
            view.removeProgressBarItemFromRecyclerView();
            view.addNewSchoolPostsToRecyclerView(schoolPosts);
        }
    }

    @Override
    public void onNoMorePostsToLoad() {
        view.removeProgressBarItemFromRecyclerView();
        view.notifyAdapterNoMorePostsToLoad();
    }

    @Override
    public void onPostSuccessfullyUpvoted() {

    }

    @Override
    public void onPostSuccessfullyDownvoted() {

    }

    @Override
    public void onPostSuccessfullyReported() {
        view.dismissLoadToastSuccessfully();
    }

    @Override
    public void onPostSuccessfullyDeleted() {
        view.dismissLoadToastSuccessfully();
        view.removePostFromList(selectedSchoolPost);
    }

    @Override
    public void onFailure() {
        view.dismissLoadToastWithError();
        view.setProgressBarVisibility(View.INVISIBLE);
        view.setSwipeRefreshLayoutRefreshing(false);
        view.showServerErrorSnackbar();
        view.showEmptyDataSetView(true);
    }
}

package com.code_dream.almanach.person_profile;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.person_profile.models.PersonProfileHeaderItem;
import com.code_dream.almanach.person_profile.network.OnNetworkRequestFinished;
import com.code_dream.almanach.person_profile.network.PersonProfileInteractor;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Qwerasdzxc on 9/20/17.
 */

public class PersonProfilePresenter implements PersonProfileContract.UserActionsListener,
        OnNetworkRequestFinished {

    private PersonProfileContract.View view;
    private PersonProfileInteractor interactor;

    // Position of the selected SchoolPost, used in the View
    // to replace the original one with the edited from the Post Details Activity
    private int selectedPostPosition;
    private SchoolPost selectedSchoolPost;

    private Person person;
    private PersonProfileHeaderItem personProfileHeaderItem;

    public PersonProfilePresenter(PersonProfileContract.View view) {
        this.view = view;
        this.interactor = new PersonProfileInteractor();
    }

    @Override
    public void loadPersonProfile() {
        interactor.loadPersonProfile(view.getPersonId(), this);
    }

    @Override
    public void loadMorePosts() {
        if (!view.isRecyclerViewEmpty()) {
            interactor.loadMorePosts(false, this, person.getId(), view.getLastPostId());
            view.addProgressBarItemToRecyclerView();
        }
    }

    @Override
    public void onScrolled() {
        if (view.layoutManagerFindLastVisibleItemPosition() >= view.layoutManagerGetItemCount() - 10)
            view.setMoreAdapterDataAvailable();
    }

    @Override
    public void refreshAllPosts() {
        interactor.loadMorePosts(true, this, person.getId(), Registry.LOAD_ALL_POSTS_CODE);
    }

    @Override
    public void onAddFriendClick() {
        if (person.getFriendType() == Person.FriendType.FRIENDS)
            interactor.deleteFriend(person.getId(), this);
        else if (person.getFriendType() == Person.FriendType.NOT_FRIENDS)
            interactor.addFriend(person.getId(), this);
        else if (person.getFriendType() == Person.FriendType.FRIEND_REQUEST_RECEIVED)
            interactor.acceptFriend(person.getId(), this);
    }

    @Override
    public void onSendMessageClick() {
        interactor.createChatRoom(person.getId(), this);
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

        interactor.upvotePost(this, upvotedSchoolPost.getId());
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

        interactor.downvotePost(this, downVotedSchoolPost.getId());
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
        interactor.reportPost(this, schoolPost.getId());
    }

    @Override
    public void deletePost(SchoolPost schoolPost) {
        selectedSchoolPost = schoolPost;
        view.showPostDeletingToast();
        interactor.deletePost(this, schoolPost.getId());
    }

    @Override
    public void showContextMenu(ImageView contextMenuIV, SchoolPost schoolPost) {
        if (schoolPost.getOwner())
            view.showPostOwnerContextMenu(contextMenuIV, schoolPost);
        else
            view.showPostOthersContextMenu(contextMenuIV, schoolPost);
    }

    @Override
    public void onPersonSuccessfullyReceived(Person person) {
        this.person = person;
        this.personProfileHeaderItem = new PersonProfileHeaderItem(person.getSchoolName(), person.getFriendType());

        view.addPersonHeaderItem(personProfileHeaderItem);
        view.showProgressBar(false);

        // Now that we got the Person's information, we can load his posts.
        this.refreshAllPosts();
    }

    @Override
    public void onChatRoomSuccessfullyCreated(ChatRoom chatRoom) {
        view.startChatActivity(chatRoom);
    }

    @Override
    public void onFriendRequestAccepted() {
        this.person.setFriendType(Person.FriendType.FRIENDS);

        view.updatePersonHeaderItem(person.getFriendType());
    }

    @Override
    public void onFriendRequestSent() {
        this.person.setFriendType(Person.FriendType.FRIEND_REQUEST_SENT);

        view.updatePersonHeaderItem(person.getFriendType());
    }

    @Override
    public void onFriendDeleted() {
        person.setFriendType(Person.FriendType.NOT_FRIENDS);

        view.updatePersonHeaderItem(person.getFriendType());
    }

    @Override
    public void onMorePostsSuccessfullyLoaded(List<SchoolPost> loadedSchoolPosts, boolean refreshedAll) {
        ArrayList<Object> schoolPosts = new ArrayList<>(loadedSchoolPosts.size());
        schoolPosts.addAll(loadedSchoolPosts);

        if (refreshedAll) {
            if (schoolPosts.isEmpty())
                view.addEmptyDatasetItem();
            else
                view.showPosts(schoolPosts);

            view.setSwipeRefreshLayoutRefreshing(false);
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
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {
        view.dismissLoadToastWithError();
        view.showProgressBar(false);
        view.setSwipeRefreshLayoutRefreshing(false);
    }

    @Override
    public PersonProfileHeaderItem getPersonProfileHeaderItem() {
        return personProfileHeaderItem;
    }
}

package com.code_dream.almanach.person_profile;

import android.support.annotation.UiThread;
import android.widget.ImageView;

import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.person_profile.models.PersonProfileHeaderItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 9/20/17.
 */

public interface PersonProfileContract {

    @UiThread
    interface View {

        void showProgressBar(boolean show);

        void setSwipeRefreshLayoutRefreshing(boolean show);

        void showPosts(ArrayList<Object> schoolPosts);

        void addNewSchoolPostsToRecyclerView(ArrayList<Object> schoolPosts);

        void addProgressBarItemToRecyclerView();

        void removeProgressBarItemFromRecyclerView();

        void startChatActivity(ChatRoom chatRoom);

        void setMoreAdapterDataAvailable();

        void notifyAdapterNoMorePostsToLoad();

        void addPersonHeaderItem(PersonProfileHeaderItem item);

        void updatePersonHeaderItem(Person.FriendType friendType);

        void addEmptyDatasetItem();

        int getPersonId();

        int getLastPostId();

        int layoutManagerGetItemCount();

        int layoutManagerFindLastVisibleItemPosition();

        boolean isRecyclerViewEmpty();

        void showPostOwnerContextMenu(ImageView contextMenuIV, SchoolPost schoolPost);

        void showPostOthersContextMenu(ImageView contextMenuIV, SchoolPost schoolPost);

        void startAddEditActivity(int id, String content);

        void showPostReportingToast();

        void showPostDeletingToast();

        void dismissLoadToastSuccessfully();

        void removePostFromList(SchoolPost selectedSchoolPost);

        void dismissLoadToastWithError();
    }

    interface UserActionsListener {

        void loadPersonProfile();

        void loadMorePosts();

        void onScrolled();

        void refreshAllPosts();

        void onAddFriendClick();

        void onSendMessageClick();

        PersonProfileHeaderItem getPersonProfileHeaderItem();

        void onUpvoteClick(SchoolPost schoolPost);

        void onDownvoteClick(SchoolPost schoolPost);

        void showContextMenu(ImageView contextMenuIV, SchoolPost schoolPost);

        void editPost(SchoolPost schoolPost, int selectedPostPosition);

        void deletePost(SchoolPost schoolPost);

        void reportPost(SchoolPost schoolPost);
    }
}

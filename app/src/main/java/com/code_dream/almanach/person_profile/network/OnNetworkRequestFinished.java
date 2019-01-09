package com.code_dream.almanach.person_profile.network;

import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.SchoolPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qwerasdzxc on 9/21/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onPersonSuccessfullyReceived(Person person);

    void onChatRoomSuccessfullyCreated(ChatRoom chatRoom);

    void onFriendRequestAccepted();

    void onFriendRequestSent();

    void onFriendDeleted();

    void onMorePostsSuccessfullyLoaded(List<SchoolPost> schoolPosts, boolean refreshedAll);

    void onNoMorePostsToLoad();

    void onPostSuccessfullyUpvoted();

    void onPostSuccessfullyDownvoted();

    void onPostSuccessfullyReported();

    void onPostSuccessfullyDeleted();
}

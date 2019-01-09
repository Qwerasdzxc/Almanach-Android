package com.code_dream.almanach.chat_tab;

import com.code_dream.almanach.chat_tab.network.ChatTabInteractor;
import com.code_dream.almanach.chat_tab.network.OnNetworkRequestFinished;
import com.code_dream.almanach.models.ChatRoom;

import java.util.List;

/**
 * Created by Qwerasdzxc on 9/17/17.
 */

public class ChatTabPresenter implements ChatTabContract.UserActionsListener, OnNetworkRequestFinished {

    private ChatTabContract.View view;
    private ChatTabInteractor interactor;

    public ChatTabPresenter(ChatTabContract.View view){
        this.view = view;
        this.interactor = new ChatTabInteractor();

        view.setSwipeRefreshing(true);
    }

    @Override
    public void getChatRooms(){

        interactor.getChatRooms(this);
    }

    @Override
    public void onChatRoomsSuccessfullyLoaded(List<ChatRoom> chatRooms) {
        view.setSwipeRefreshing(false);
        view.updateList(chatRooms);
    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {

    }
}

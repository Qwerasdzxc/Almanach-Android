package com.code_dream.almanach.chat;

import android.content.Intent;
import android.util.Log;

import com.code_dream.almanach.chat.network.ChatInteractor;
import com.code_dream.almanach.chat.network.OnNetworkRequestFinished;
import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Message;
import com.code_dream.almanach.utility.UserInfo;

import java.util.List;

/**
 * Created by Qwerasdzxc on 9/17/17.
 */

public class ChatPresenter implements ChatContract.UserActionsListener, OnNetworkRequestFinished {

    private ChatContract.View view;
    private ChatInteractor interactor;

    private ChatRoom chatRoom;

    public ChatPresenter(ChatContract.View view) {
        this.view = view;
        this.interactor = new ChatInteractor();

        // Establish the WebSocket connection with the server.
        view.startChatService();
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Override
    public void loadAllMessages() {
        // Display the Progress bar while the messages are being loaded.
        view.showProgressBar(true);

        // Send an HTTP request to retrieve old messages.
        interactor.loadAllMessages(view.getChatRoomId(), this);
    }

    @Override
    public void onSendClick() {
        Message message = new Message(view.getInputText(), chatRoom.getPerson().getName(), true, UserInfo.TOKEN);

        view.sendMessage(message);

        // Clear the old input text and scroll the RecyclerView to bottom.
        view.clearInputText();
        view.scrollToBottom();

        // Display a sent message bubble.
        view.addMessageBubble(message);
    }

    @Override
    public void onReceiveFromService(Intent data) {
        Boolean success = data.getBooleanExtra("success", false);

        //TODO: Show a loading bar if the Socket connection service failed to start
        if (!success) {
            Log.d("onReceive", "FAILURE");
        } else {
            Log.d("onReceive", "SUCCESS");
        }
    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {

    }

    @Override
    public void onMessagesSuccessfullyReceived(List<Message> messages) {
        // Old messages have been successfully loaded so we show
        // them in the RecyclerView and hide the Progress bar.
        view.updateMessages(messages);
        view.showProgressBar(false);
    }
}

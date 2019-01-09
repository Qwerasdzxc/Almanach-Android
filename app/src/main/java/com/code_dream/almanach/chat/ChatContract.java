package com.code_dream.almanach.chat;

import android.content.Intent;
import android.support.annotation.UiThread;

import com.code_dream.almanach.models.Message;

import java.util.List;

/**
 * Created by qwerasdzxc on 9/17/17.
 */

public interface ChatContract {

    @UiThread
    interface View {

        String getInputText();

        void startChatService();

        void updateMessages(List<Message> messages);

        void clearInputText();

        void showProgressBar(boolean show);

        void addMessageBubble(Message message);

        int getChatRoomId();

        void scrollToBottom();

        void sendMessage(Message message);
    }

    interface UserActionsListener {

        void loadAllMessages();

        void onReceiveFromService(Intent data);

        void onSendClick();
    }
}

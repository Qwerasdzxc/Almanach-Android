package com.code_dream.almanach.chat_tab;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.ChatRoom;

import java.util.List;

/**
 * Created by Qwerasdzxc on 9/17/17.
 */

public interface ChatTabContract {

    @UiThread
    interface View {

        void updateList(List<ChatRoom> chatRooms);

        void setSwipeRefreshing(boolean enabled);
    }

    interface UserActionsListener {

        void getChatRooms();
    }
}

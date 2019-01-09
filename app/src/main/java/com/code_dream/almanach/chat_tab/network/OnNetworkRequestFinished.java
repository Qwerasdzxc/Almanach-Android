package com.code_dream.almanach.chat_tab.network;

import com.code_dream.almanach.models.ChatRoom;

import java.util.List;

/**
 * Created by Qwerasdzxc on 9/23/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onChatRoomsSuccessfullyLoaded(List<ChatRoom> chatRooms);
}

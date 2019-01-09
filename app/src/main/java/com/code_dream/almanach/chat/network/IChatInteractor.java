package com.code_dream.almanach.chat.network;


/**
 * Created by Qwerasdzxc on 9/18/17.
 */

public interface IChatInteractor {

    void loadAllMessages(int chatRoomId, final OnNetworkRequestFinished listener);
}

package com.code_dream.almanach.chat.network;

import com.code_dream.almanach.models.Message;

import java.util.List;

/**
 * Created by Qwerasdzxc on 9/18/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onMessagesSuccessfullyReceived(List<Message> messages);
}

package com.code_dream.almanach.chat.network;

import com.code_dream.almanach.models.Message;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.LoadAllMessagesRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 9/18/17.
 */

public class ChatInteractor implements IChatInteractor {

    @Override
    public void loadAllMessages(int chatRoomId, final OnNetworkRequestFinished listener) {
        Call<List<Message>> call = new RestClient().getApiService().loadAllMessages(new LoadAllMessagesRequest(chatRoomId));

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                listener.onMessagesSuccessfullyReceived(response.body());
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

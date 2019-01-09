package com.code_dream.almanach.chat_tab.network;

import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.utility.Registry;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 9/23/17.
 */

public class ChatTabInteractor {

    public void getChatRooms(final OnNetworkRequestFinished listener) {
        Call<List<ChatRoom>> call = new RestClient().getApiService().getChatRooms(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                listener.onChatRoomsSuccessfullyLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

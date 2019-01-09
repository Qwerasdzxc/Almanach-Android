package com.code_dream.almanach.notifications_tab.network;

import com.code_dream.almanach.models.Notification;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.utility.Registry;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 11/15/17.
 */

public class NotificationsTabInteractor {

    private OnNetworkRequestFinished listener;

    public NotificationsTabInteractor(OnNetworkRequestFinished listener) {
        this.listener = listener;
    }

    public void loadNotifications() {
        Call<List<Notification>> call = new RestClient().getApiService().getNotifications(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                listener.onNotificationsLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });
    }
}

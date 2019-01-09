package com.code_dream.almanach.home.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.responses.LogoutResponse;
import com.code_dream.almanach.utility.Registry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public class HomeInteractor implements IHomeInteractor {

    @Override
    public void logoutUser(final OnHomeRequestFinishedListener listener) {
        Call<LogoutResponse> call = new RestClient().getApiService().logoutUser(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.body().getLogoutResponseCode() == Registry.CODE_SUCCESSFUL_RESPONSE)
                    listener.onLogoutSuccessful();
                else
                    listener.onFailure();
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

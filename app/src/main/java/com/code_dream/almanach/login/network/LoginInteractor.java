package com.code_dream.almanach.login.network;


import android.util.Log;

import com.code_dream.almanach.network.requests.FirebaseTokenUpdateRequest;
import com.code_dream.almanach.network.responses.LoginResponse;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.LoginRequest;
import com.code_dream.almanach.utility.Registry;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 1/29/17.
 */

public class LoginInteractor implements ILoginInteractor {

    @Override
    public void validateCredentials(final OnLoginFinishedListener listener, String email, String password) {
        Call<LoginResponse> loginUserCall = new RestClient().getApiService().loginUser(new LoginRequest(email, password));

        loginUserCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.w("login", "code: " + response.code());

                if (response.code() != 200) {
                    listener.onFailure();
                    return;
                }

                if (response.body().getResponse() != Registry.CODE_BAD_LOGIN)
                    listener.onLoginSuccess(response.body().getToken(), response.body().getPerson());
                else
                    listener.onLoginRejected();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void updateFirebaseToken(final OnLoginFinishedListener listener, String firebaseToken) {
        Call<ResponseBody> call = new RestClient().getApiService().updateFirebaseToken(new FirebaseTokenUpdateRequest(firebaseToken));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onFirebaseTokenUpdated();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

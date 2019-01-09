package com.code_dream.almanach.intro.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.FirebaseTokenUpdateRequest;
import com.code_dream.almanach.network.responses.RegisterResponse;
import com.code_dream.almanach.network.requests.RegisterRequest;
import com.code_dream.almanach.utility.Registry;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 1/31/17.
 */

public class IntroInteractor implements IIntroInteractor {

    @Override
    public void sendRegisterRequest(final OnRegisterFinishedListener listener, final String fullName, String dob, String email, String password, String allSubjects) {
        Call<RegisterResponse> registerUserCall = new RestClient().getApiService().registerUser(new RegisterRequest(fullName, dob, email, password, allSubjects));

        registerUserCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.body().getResponse() != Registry.CODE_BAD_REGISTER_USER_ALREADY_EXISTS)
                    listener.onSuccess(response.body().getToken());
                else
                    listener.onReject();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void updateFirebaseToken(final OnRegisterFinishedListener listener, String firebaseToken) {
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


package com.code_dream.almanach.splash_screen.network;

import android.util.Log;

import com.code_dream.almanach.network.ResponseHandler;
import com.code_dream.almanach.network.ResponseHandlerCallback;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.FirebaseTokenUpdateRequest;
import com.code_dream.almanach.network.responses.TokenValidationResponse;
import com.code_dream.almanach.utility.Registry;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public class SplashScreenInteractor implements ISplashScreenInteractor {

    @Override
    public void verifyTokenValidation(final OnSplashScreenRequestFinishedListener listener) {
        Call<TokenValidationResponse> call = new RestClient().getApiService().verifyTokenValidation(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<TokenValidationResponse>() {
            @Override
            public void onResponse(Call<TokenValidationResponse> call, final Response<TokenValidationResponse> response) {
                final TokenValidationResponse responseBody = response.body();

                ResponseHandler.getInstance().getResponse(response, new ResponseHandlerCallback() {
                    @Override
                    public void onSuccess(Response originalResponse) {
                        if (responseBody.getNewToken().isEmpty())
                            listener.onTokenVerificationValid(responseBody.getPerson());
                        else
                            listener.onTokenVerificationValid(responseBody.getNewToken(), responseBody.getPerson());
                    }

                    @Override
                    public void onServerFailure() {
                        // If we got 401, our Token has expired.
                        if (response.code() == 401) {
                            listener.onTokenVerificationInvalid();
                            return;
                        }

                        listener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure(Call<TokenValidationResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void updateFirebaseToken(final OnSplashScreenRequestFinishedListener listener, String firebaseToken) {
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

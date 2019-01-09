package com.code_dream.almanach.settings.account_preference.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.NewDobRequest;
import com.code_dream.almanach.network.requests.NewNameRequest;
import com.code_dream.almanach.network.responses.UpdatePreferencesResponse;
import com.code_dream.almanach.utility.Registry;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public class AccountPreferenceInteractor implements IAccountPreferenceInteractor {

    @Override
    public void loadPreferences(final OnPreferenceLoadListener listener) {

        Call<UpdatePreferencesResponse> call = new RestClient().getApiService().updatePreferences(Registry.NO_DATA_TO_SEND);
        call.enqueue(new Callback<UpdatePreferencesResponse>() {
            @Override
            public void onResponse(Call<UpdatePreferencesResponse> call, Response<UpdatePreferencesResponse> response) {
                String date[] = response.body().getDob().split("/");
                listener.onPreferencesLoaded(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
            }

            @Override
            public void onFailure(Call<UpdatePreferencesResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void updateDobPreference(final OnPreferenceLoadListener listener, int day, int month, int year) {
        String fullDob = day + "/" + month + "/" + year;

        Call<ResponseBody> call = new RestClient().getApiService().changeDob(new NewDobRequest(fullDob));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onDobPreferenceUpdated();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void deleteUser(final OnPreferenceLoadListener listener) {

        Call<ResponseBody> call = new RestClient().getApiService().deleteUser(Registry.NO_DATA_TO_SEND);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onUserDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void updateNamePreference(final OnPreferenceLoadListener listener, final String newName) {

        Call<ResponseBody> call = new RestClient().getApiService().changeName(new NewNameRequest(newName));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onNamePreferenceUpdated(newName);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

package com.code_dream.almanach.school_profile.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.SetSchoolRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public class SchoolProfileInteractor implements ISchoolProfileInteractor{
    @Override
    public void selectSchool(final OnSchoolSetListener listener, String schoolName) {

        Call<ResponseBody> call = new RestClient().getApiService().setSchool(new SetSchoolRequest(schoolName));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onSchoolSuccessfullySet();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

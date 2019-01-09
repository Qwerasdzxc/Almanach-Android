package com.code_dream.almanach.intro.slides.school.network;

import com.code_dream.almanach.models.School;
import com.code_dream.almanach.network.ResponseHandler;
import com.code_dream.almanach.network.ResponseHandlerCallback;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.SearchRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 7/5/17.
 */

public class SchoolInteractor implements ISchoolInteractor {

    private OnNetworkRequestFinished callback;

    public SchoolInteractor(OnNetworkRequestFinished callback) {
        this.callback = callback;
    }

    @Override
    public void searchSchool(String schoolName) {
        Call<List<School>> call = new RestClient().getApiService().searchSchool(new SearchRequest(schoolName));

        call.enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(final Call<List<School>> call, final Response<List<School>> response) {
                ResponseHandler.getInstance().getResponse(response, new ResponseHandlerCallback() {
                    @Override
                    public void onSuccess(Response originalResponse) {
                        callback.onSchoolsSuccessfullyReceived(response.body());
                    }

                    @Override
                    public void onServerFailure() {
                        callback.onServerError();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                callback.onInternetConnectionProblem();
            }
        });
    }
}

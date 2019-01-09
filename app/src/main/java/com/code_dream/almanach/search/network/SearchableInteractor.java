package com.code_dream.almanach.search.network;

import android.util.Log;

import com.code_dream.almanach.models.SearchResult;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.SearchRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public class SearchableInteractor implements ISearchableInteractor {
    @Override
    public void refreshSchools(final OnSearchListener listener, final String searchText) {
        Call<SearchResult> call = new RestClient().getApiService().search(new SearchRequest(searchText));

        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (!searchText.trim().isEmpty()){
                    Log.e("onResponse", "schools: " + response.body().getSchools().size() + ", people: " + response.body().getPeople().size());
                    if (!response.body().getPeople().isEmpty())
                        Log.e("people", "name 1st: " + response.body().getPeople().get(0).getName());
                    listener.onSearchResultsSuccessfullyLoaded(response.body());
                }
                else
                    listener.onNoSearchResults();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                listener.onNoSearchResults();
            }
        });
    }

//    @Override
//    public Single<List<School>> refreshSchoolsReactively(String searchText) {
//        return new RestClient().getApiService().se(new SearchRequest(searchText));
//    }
}

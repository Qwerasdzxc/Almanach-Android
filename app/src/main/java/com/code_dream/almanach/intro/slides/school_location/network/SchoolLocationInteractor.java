package com.code_dream.almanach.intro.slides.school_location.network;

import com.code_dream.almanach.models.geocoding.LocationsResponse;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Peter on 7/28/2017.
 */

public class SchoolLocationInteractor implements ISchoolLocationInteractor {

    private OnNetworkRequestFinished callback;

    public SchoolLocationInteractor(OnNetworkRequestFinished callback) {
        this.callback = callback;
    }

    @Override
    public void getLatLngFromLocationName(String schoolName, String apiKey) {
        Call<LocationsResponse> call = new RestClient().getApiService().getFromLocationName(Utility.buildGeocodingUrl(schoolName, apiKey));

        call.enqueue(new Callback<LocationsResponse>() {
            @Override
            public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
                callback.onSchoolLocationsSuccessfullyReceived(response.body());
            }

            @Override
            public void onFailure(Call<LocationsResponse> call, Throwable t) {

            }
        });
    }
}

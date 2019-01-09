package com.code_dream.almanach.intro.slides.school_location.network;

import com.code_dream.almanach.models.geocoding.LocationsResponse;

/**
 * Created by Peter on 7/28/2017.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onSchoolLocationsSuccessfullyReceived(LocationsResponse locations);
}

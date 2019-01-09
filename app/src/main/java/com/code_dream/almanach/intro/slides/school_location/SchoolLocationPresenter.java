package com.code_dream.almanach.intro.slides.school_location;

import com.google.android.gms.maps.model.LatLng;
import com.code_dream.almanach.intro.slides.school_location.network.OnNetworkRequestFinished;
import com.code_dream.almanach.intro.slides.school_location.network.SchoolLocationInteractor;
import com.code_dream.almanach.models.geocoding.LocationsResponse;
import com.code_dream.almanach.models.geocoding.Result;
import com.code_dream.almanach.utility.Registry;

/**
 * Created by Peter on 7/27/2017.
 */

public class SchoolLocationPresenter implements SchoolLocationContract.UserActionsListener,
                                                OnNetworkRequestFinished{

    private SchoolLocationInteractor interactor;
    private SchoolLocationContract.View view;

    public SchoolLocationPresenter(SchoolLocationContract.View view) {
        interactor = new SchoolLocationInteractor(this);
        this.view = view;
    }

    @Override
    public void onMapClick(LatLng point) {
        view.moveAndMarkLocationOnMap(point);
    }

    @Override
    public void getSchoolLocation(String schoolName) {
        interactor.getLatLngFromLocationName(schoolName, view.getGeocodingApiKey());
    }

    @Override
    public void onSchoolLocationsSuccessfullyReceived(LocationsResponse locations) {
        if (locations != null && locations.getResults().size() > 0){
            Result result = locations.getResults().get(0);

            LatLng location = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());

            view.moveAndMarkLocationOnMap(location);
        }
    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {

    }

    @Override
    public Boolean canMoveFurther() {
        return (view.markerSet() && Registry.addingNewSchool) || !Registry.addingNewSchool;
    }

    @Override
    public String cantMoveFurtherMessage() {
        return "You have to select the school location";
    }
}

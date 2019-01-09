package com.code_dream.almanach.intro.slides.school_location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 7/27/2017.
 */

public class SchoolLocationContract {

    public interface View {

        void setProperView(Boolean addingNewSchool);

        void moveAndMarkLocationOnMap(LatLng location);

        String getGeocodingApiKey();

        Boolean markerSet();
    }

    public interface UserActionsListener {

        void onMapClick(LatLng point);

        void getSchoolLocation(String schoolName);

        Boolean canMoveFurther();

        String cantMoveFurtherMessage();
    }
}

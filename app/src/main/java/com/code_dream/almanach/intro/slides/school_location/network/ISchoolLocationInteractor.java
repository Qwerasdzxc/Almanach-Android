package com.code_dream.almanach.intro.slides.school_location.network;

/**
 * Created by Peter on 7/28/2017.
 */

public interface ISchoolLocationInteractor {

    void getLatLngFromLocationName(String schoolName, String apiKey);
}

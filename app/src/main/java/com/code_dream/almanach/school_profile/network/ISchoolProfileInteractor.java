package com.code_dream.almanach.school_profile.network;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface ISchoolProfileInteractor {
    void selectSchool(OnSchoolSetListener listener, String schoolName);
}

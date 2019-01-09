package com.code_dream.almanach.school_profile;

import android.support.annotation.UiThread;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface SchoolProfileContract {

    @UiThread
    interface View {

        String getSchoolName();

        void showLoadingToast();

        void dismissLoadingToast();

        void dismissLoadingToastWithError();
    }

    interface UserActionsListener {

        void onSelectSchool();
    }
}

package com.code_dream.almanach.intro;

import android.support.annotation.UiThread;

import com.code_dream.almanach.intro.slides.register.SlideRegister;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface IntroContract {

    @UiThread
    interface View {

        void onRegistrationStart();

        SlideRegister getSlideRegister();

        String getSavedFirebaseToken();

        String getAllSubjects();

        String getFullName();

        String getDob();

        String getEmail();

        String getPassword();

        void showEmailAlreadyExistsMessage();

        void startHomeActivity();

        void showTimeoutDialog();
    }

    interface UserActionsListener {

        void onFinishClick();

        void onBackClick();

        boolean canFinish();
    }
}

package com.code_dream.almanach.splash_screen;

import android.support.annotation.UiThread;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public interface SplashScreenContract {

    @UiThread
    interface View {

        void saveSessionToken(String token);

        void saveFullName(String fullName);

        void saveId(int id);

        String getFullNameFromStorage();

        String getTokenFromStorage();

        int getIdFromStorage();

        void startHomeActivity();

        void startLoginActivity();
    }

    interface UserActionsListener {

        void verifyTokenValidation();
    }
}

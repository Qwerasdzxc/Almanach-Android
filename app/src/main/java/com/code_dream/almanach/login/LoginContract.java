package com.code_dream.almanach.login;

import android.support.annotation.StringRes;
import android.support.annotation.UiThread;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface LoginContract {

    @UiThread
    interface View {

        String getEmail();

        String getPassword();

        String getSavedFirebaseToken();

        String getSavedEmail();

        void saveId(int id);

        void saveFullName(String fullName);

        void saveEmailAutoFill(String email);

        void saveSessionToken(String token);

        void showEmptyEmailErrorMessage(@StringRes int resId);

        void showEmptyPasswordErrorMessage(@StringRes int resId);

        void showUnsuccessfulAuthenticationMessage(@StringRes int resId);

        void startHomeActivity();

        void showTimeoutDialog();

        void showProgressDialog();
    }

    interface UserActionsListener {

        void onLoginButtonClick();

        void onRegisterLongClick();
    }
}

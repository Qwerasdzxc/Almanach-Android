package com.code_dream.almanach.settings.account_preference;

import android.content.SharedPreferences;

/**
 * Created by Qwerasdzxc on 2/4/17.
 */

public interface AccountPreferenceContract {

    interface View {

        void returnToLoginActivity();

        void showDeleteUserConfirmationDialog();

        void setDateOfBirth(int day, int month, int year);

        void dismissProgressDialog();

        void showProgressDialog();

        SharedPreferences getSharedPreferences();
    }

    interface UserActionsListener {

        void deleteUser();

        void loadPreferences();

        void updateDobPreference(int dayOfMonth, int month, int year);

        void deleteUserConfirmation();

        void updateNamePreference(String newName);
    }
}

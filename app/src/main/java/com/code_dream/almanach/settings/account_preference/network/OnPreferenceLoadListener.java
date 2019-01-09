package com.code_dream.almanach.settings.account_preference.network;

/**
 * Created by Qwerasdzxc on 2/4/17.
 */

public interface OnPreferenceLoadListener {

    void onPreferencesLoaded(int day, int month, int year);

    void onFailure();

    void onDobPreferenceUpdated();

    void onUserDeleted();

    void onNamePreferenceUpdated(String updatedName);
}

package com.code_dream.almanach.settings.account_preference.network;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public interface IAccountPreferenceInteractor {

    void loadPreferences(OnPreferenceLoadListener listener);

    void updateDobPreference(OnPreferenceLoadListener listener, int day, int month, int year);

    void deleteUser(OnPreferenceLoadListener listener);

    void updateNamePreference(OnPreferenceLoadListener listener, String newName);
}

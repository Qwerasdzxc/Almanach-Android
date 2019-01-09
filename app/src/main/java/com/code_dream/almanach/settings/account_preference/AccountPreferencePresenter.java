package com.code_dream.almanach.settings.account_preference;

import android.content.SharedPreferences;

import com.code_dream.almanach.settings.account_preference.network.AccountPreferenceInteractor;
import com.code_dream.almanach.settings.account_preference.network.OnPreferenceLoadListener;
import com.code_dream.almanach.utility.UserInfo;

/**
 * Created by Qwerasdzxc on 2/4/17.
 */

public class AccountPreferencePresenter implements AccountPreferenceContract.UserActionsListener, OnPreferenceLoadListener {

    private AccountPreferenceContract.View view;
    private AccountPreferenceInteractor accountPreferenceInteractor;

    public AccountPreferencePresenter(AccountPreferenceContract.View view) {
        this.view = view;
        accountPreferenceInteractor = new AccountPreferenceInteractor();
    }

    @Override
    public void loadPreferences() {
        view.showProgressDialog();
        accountPreferenceInteractor.loadPreferences(this);
    }

    @Override
    public void updateDobPreference(int day, int month, int year) {
        view.showProgressDialog();
        accountPreferenceInteractor.updateDobPreference(this, day, month, year);
    }

    @Override
    public void deleteUserConfirmation() {
        view.showDeleteUserConfirmationDialog();
    }

    @Override
    public void deleteUser() {
        view.showProgressDialog();
        accountPreferenceInteractor.deleteUser(this);
    }

    @Override
    public void updateNamePreference(String newName) {
        view.showProgressDialog();
        accountPreferenceInteractor.updateNamePreference(this, newName);
    }

    @Override
    public void onPreferencesLoaded(int day, int month, int year) {
        SharedPreferences.Editor editor = view.getSharedPreferences().edit();
        editor.putString("change_name_preference", UserInfo.CURRENT_USER.getName());
        editor.apply();

        view.setDateOfBirth(day, month, year);
        view.dismissProgressDialog();
    }

    @Override
    public void onDobPreferenceUpdated() {
        view.dismissProgressDialog();
    }

    @Override
    public void onUserDeleted() {
        view.returnToLoginActivity();
    }

    @Override
    public void onNamePreferenceUpdated(String updatedName) {
        UserInfo.CURRENT_USER.setName(updatedName);
        view.dismissProgressDialog();
    }

    @Override
    public void onFailure() {

    }
}

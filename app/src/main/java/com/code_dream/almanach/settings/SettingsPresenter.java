package com.code_dream.almanach.settings;

import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public class SettingsPresenter implements SettingsContract.UserActionsListener{

    private SettingsContract.ActivityView activityView;
    private SettingsContract.FragmentView fragmentView;

    public SettingsPresenter(SettingsContract.ActivityView activityView) {
        this.activityView = activityView;
    }

    public SettingsPresenter(SettingsContract.FragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void initializeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            activityView.displayMainSettingsFragment();
    }

    @Override
    public boolean onActivityOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onActivityBackArrowClicked();
                return true;

            default: return activityView.superOnOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityBackButtonPressed() {
        if (activityView.getBackStackEntryCount() == 0)
            activityView.superOnBackButtonPressed();
        else
            activityView.popBackStack();
    }

    @Override
    public void onActivityBackArrowClicked() {
        if (activityView.getBackStackEntryCount() == 0)
            activityView.finishActivity();
        else
            activityView.popBackStack();
    }

}

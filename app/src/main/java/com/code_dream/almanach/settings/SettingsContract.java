package com.code_dream.almanach.settings;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.view.MenuItem;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public interface SettingsContract {

    @UiThread
    interface ActivityView {

        void displayMainSettingsFragment();

        void finishActivity();

        void popBackStack();

        int getBackStackEntryCount();

        void superOnBackButtonPressed();

        boolean superOnOptionsItemSelected(MenuItem item);
    }

    @UiThread
    interface FragmentView {

    }

    interface UserActionsListener {

        void initializeFragment(Bundle savedInstanceState);

        boolean onActivityOptionsItemSelected(MenuItem item);

        void onActivityBackButtonPressed();

        void onActivityBackArrowClicked();
    }
}

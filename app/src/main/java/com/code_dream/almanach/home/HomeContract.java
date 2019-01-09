package com.code_dream.almanach.home;

import android.support.annotation.UiThread;
import android.view.MenuItem;

import com.code_dream.almanach.adapters.ViewPagerAdapter;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public interface HomeContract {

    @UiThread
    interface View {

        void showProgressDialog();

        void dismissProgressDialog();

        void search();

        void settings();

        void openLoginActivity();

        void setupTabLayout(ViewPagerAdapter viewPagerAdapter);

        void changeTabBadgeNumber(int number);

        void showLogoutErrorToast();
    }

    interface UserActionsListener {

        void onOptionsItemSelected(MenuItem menuItem);

        void setupTabLayout(ViewPagerAdapter viewPagerAdapter);

        void openSettings();

        void logout();

        void onSearchViewClick();

        void onSearchFocusChange(boolean hasFocus);
    }
}

package com.code_dream.almanach.home;

import android.view.MenuItem;

import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.ViewPagerAdapter;
import com.code_dream.almanach.home.network.HomeInteractor;
import com.code_dream.almanach.home.network.OnHomeRequestFinishedListener;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public class HomePresenter implements HomeContract.UserActionsListener,
        OnHomeRequestFinishedListener {

    private HomeContract.View view;
    private HomeInteractor interactor;

    public HomePresenter(HomeContract.View view, HomeInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void setupTabLayout(ViewPagerAdapter viewPagerAdapter) {
        view.setupTabLayout(viewPagerAdapter);
    }

    @Override
    public void onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_settings)
            openSettings();
        else if (id == R.id.action_logout)
            logout();
    }

    @Override
    public void onSearchFocusChange(boolean hasFocus) {
        if (hasFocus)
            view.search();
    }

    @Override
    public void onSearchViewClick() {
        view.search();
    }

    @Override
    public void openSettings() {
        view.settings();
    }

    @Override
    public void logout() {
        view.showProgressDialog();
        interactor.logoutUser(this);
    }

    @Override
    public void onLogoutSuccessful() {
        view.dismissProgressDialog();
        view.openLoginActivity();
    }

    @Override
    public void onFailure() {
        view.dismissProgressDialog();
        view.showLogoutErrorToast();
    }
}

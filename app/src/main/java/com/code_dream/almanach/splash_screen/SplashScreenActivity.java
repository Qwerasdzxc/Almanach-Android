package com.code_dream.almanach.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.home.HomeActivity;
import com.code_dream.almanach.login.LoginActivity;
import com.code_dream.almanach.utility.Registry;

public class SplashScreenActivity extends BaseActivity implements SplashScreenContract.View {

    private SplashScreenPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setupView() {
        // We don't have any Views to setup
    }

    @Override
    public void setupPresenter(){
        presenter = new SplashScreenPresenter(this);
        presenter.verifyTokenValidation();
    }

    @Override
    public void saveSessionToken(String token) {
        sharedPreferences.edit().putString(Registry.SHARED_PREFS_TOKEN_KEY, token).apply();
    }

    @Override
    public void saveId(int id) {
        sharedPreferences.edit().putInt(Registry.SHARED_PREFS_ID_KEY, id).apply();
    }

    @Override
    public void saveFullName(String fullName) {
        sharedPreferences.edit().putString(Registry.SHARED_PREFS_FULL_NAME_KEY, fullName).apply();
    }

    @Override
    public String getTokenFromStorage() {
        return sharedPreferences.getString(Registry.SHARED_PREFS_TOKEN_KEY, Registry.EMPTY_TOKEN);
    }

    @Override
    public String getFullNameFromStorage() {
        return sharedPreferences.getString(Registry.SHARED_PREFS_FULL_NAME_KEY, Registry.EMPTY_TOKEN);
    }

    @Override
    public int getIdFromStorage() {
        return sharedPreferences.getInt(Registry.SHARED_PREFS_ID_KEY, 0);
    }

    @Override
    public void startHomeActivity() {
        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
        this.finish();
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        this.finish();
    }
}

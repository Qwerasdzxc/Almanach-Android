package com.code_dream.almanach;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.code_dream.almanach.network.InternetConnectionRetryCallback;

import javax.inject.Inject;

/**
 * Created by Qwerasdzxc on 2/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Inject public SharedPreferences sharedPreferences;
    @Inject public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SPApplication) getApplication()).getAppComponent().inject(this);

        setupPresenter();
    }

    // Initialization part that all Activities override

    public abstract void setupPresenter();

    public abstract void setupView();

    public void showInternetConnectionErrorSnackbar(final InternetConnectionRetryCallback callback) {
        Snackbar.make(findViewById(android.R.id.content), "No Internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.onRetry();
                    }
                }).show();
    }
}

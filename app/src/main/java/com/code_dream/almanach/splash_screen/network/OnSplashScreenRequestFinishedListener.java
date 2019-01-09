package com.code_dream.almanach.splash_screen.network;

import com.code_dream.almanach.models.Person;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public interface OnSplashScreenRequestFinishedListener {

    void onTokenVerificationValid(Person person);

    void onTokenVerificationValid(String newToken, Person person);

    void onTokenVerificationInvalid();

    void onFailure();

    void onFirebaseTokenUpdated();
}

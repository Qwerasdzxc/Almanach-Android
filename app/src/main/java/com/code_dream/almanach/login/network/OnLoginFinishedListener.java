package com.code_dream.almanach.login.network;

import com.code_dream.almanach.models.Person;

/**
 * Created by Qwerasdzxc on 1/29/17.
 */

public interface OnLoginFinishedListener {

    void onLoginRejected();

    void onFailure();

    void onLoginSuccess(String token, Person person);

    void onFirebaseTokenUpdated();
}

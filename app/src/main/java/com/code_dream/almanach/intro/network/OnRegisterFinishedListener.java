package com.code_dream.almanach.intro.network;

/**
 * Created by Qwerasdzxc on 1/31/17.
 */

public interface OnRegisterFinishedListener {

    void onReject();

    void onFailure();

    void onSuccess(String token);

    void onFirebaseTokenUpdated();
}

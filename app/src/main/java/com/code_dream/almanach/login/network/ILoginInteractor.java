package com.code_dream.almanach.login.network;

/**
 * Created by Qwerasdzxc on 1/29/17.
 */

public interface ILoginInteractor {

    void validateCredentials(OnLoginFinishedListener listener, String email, String password);

    void updateFirebaseToken(OnLoginFinishedListener listener, String firebaseToken);
}

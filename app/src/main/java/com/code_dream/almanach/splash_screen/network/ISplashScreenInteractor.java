package com.code_dream.almanach.splash_screen.network;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public interface ISplashScreenInteractor {

    void verifyTokenValidation(OnSplashScreenRequestFinishedListener listener);

    void updateFirebaseToken(OnSplashScreenRequestFinishedListener listener, String savedFirebaseToken);
}

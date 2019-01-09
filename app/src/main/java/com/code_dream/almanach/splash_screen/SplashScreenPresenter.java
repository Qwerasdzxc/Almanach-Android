package com.code_dream.almanach.splash_screen;

import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.splash_screen.network.OnSplashScreenRequestFinishedListener;
import com.code_dream.almanach.splash_screen.network.SplashScreenInteractor;
import com.code_dream.almanach.utility.UserInfo;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public class SplashScreenPresenter implements SplashScreenContract.UserActionsListener,
                                              OnSplashScreenRequestFinishedListener {

    private SplashScreenContract.View view;
    private SplashScreenInteractor interactor;

    public SplashScreenPresenter(SplashScreenContract.View view){
        this.view = view;
        interactor = new SplashScreenInteractor();
    }

    @Override
    public void verifyTokenValidation() {
        // Load Token from storage.
        String token = view.getTokenFromStorage();

        // If we already have a saved Token then we send a verification
        // request to the server to see if it's still valid.
        if (!token.isEmpty()) {
            UserInfo.TOKEN = token;
            interactor.verifyTokenValidation(this);
        }
        else {
            // No Token found so the user needs to login to their account.
            view.startLoginActivity();
        }
    }

    @Override
    public void onTokenVerificationValid(Person person) {
        // Token is valid and we got the user's full name.
        UserInfo.CURRENT_USER = person;

        // Save the User's name and ID to device storage.
        view.saveFullName(person.getName());
        view.saveId(person.getId());

        // If we have the user's Firebase token, then we send the request to the server and update it.
        if (!UserInfo.FIREBASE_TOKEN.isEmpty())
            interactor.updateFirebaseToken(this, UserInfo.FIREBASE_TOKEN);
        else {
            // Token hasn't been refreshed/updated so we just continue to the Home activity.
            view.startHomeActivity();
        }
    }

    @Override
    public void onTokenVerificationValid(String newToken, Person person) {
        // Token is valid but it needed to be refreshed,
        // so we set the newly created token and user's full name.
        UserInfo.TOKEN = newToken;
        UserInfo.CURRENT_USER = person;

        // Save the newly created Token, User's full name and ID to device storage.
        view.saveSessionToken(newToken);
        view.saveFullName(person.getName());
        view.saveId(person.getId());

        // If we have the user's Firebase token, then we send the request to the server and update it.
        if (!UserInfo.FIREBASE_TOKEN.isEmpty())
            interactor.updateFirebaseToken(this, UserInfo.FIREBASE_TOKEN);
        else {
            // Token hasn't been refreshed/updated so we just continue to the Home activity.
            view.startHomeActivity();
        }
    }

    @Override
    public void onTokenVerificationInvalid() {
        // Token was invalid so the user needs to login again.
        view.startLoginActivity();
    }

    @Override
    public void onFailure() {
        // Load the User's name from device storage.
        UserInfo.CURRENT_USER = new Person(view.getFullNameFromStorage(), view.getIdFromStorage());

        // User wasn't able to connect to the Server so we enter in offline mode.
        view.startHomeActivity();
    }

    @Override
    public void onFirebaseTokenUpdated() {
        // Firebase Token was successfully updated so we continue to the Home activity.
        view.startHomeActivity();
    }
}

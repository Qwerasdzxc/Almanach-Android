package com.code_dream.almanach.login;

import com.code_dream.almanach.R;
import com.code_dream.almanach.login.network.LoginInteractor;
import com.code_dream.almanach.login.network.OnLoginFinishedListener;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;

/**
 * Created by Qwerasdzxc on 1/29/17.
 */
public class LoginPresenter implements LoginContract.UserActionsListener, OnLoginFinishedListener{

    private LoginContract.View view;
    private LoginInteractor loginInteractor;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.loginInteractor = new LoginInteractor();
    }

    @Override
    public void onLoginButtonClick() {
        if (view.getEmail().isEmpty()) {
            view.showEmptyEmailErrorMessage(R.string.error_field_required);
            return;
        }

        if (view.getPassword().isEmpty()) {
            view.showEmptyPasswordErrorMessage(R.string.error_field_required);
            return;
        }

        view.showProgressDialog();
        loginInteractor.validateCredentials(this, view.getEmail(), view.getPassword());
    }

    @Override
    public void onRegisterLongClick() {
        view.showProgressDialog();
        loginInteractor.validateCredentials(this, Registry.DEV_EMAIL, Registry.DEV_PASS);
    }

    @Override
    public void onLoginRejected() {
        view.showUnsuccessfulAuthenticationMessage(R.string.invalid_login_credentials);
    }

    @Override
    public void onFailure() {
        view.showTimeoutDialog();
    }

    @Override
    public void onLoginSuccess(String serverToken, Person person){
        UserInfo.TOKEN = serverToken;
        UserInfo.CURRENT_USER = person;

        view.saveSessionToken(serverToken);
        view.saveEmailAutoFill(view.getEmail());
        view.saveFullName(person.getName());
        view.saveId(person.getId());

        if (!view.getSavedFirebaseToken().equals(Registry.EMPTY_TOKEN))
            loginInteractor.updateFirebaseToken(this, view.getSavedFirebaseToken());
        else
            view.startHomeActivity();
    }

    @Override
    public void onFirebaseTokenUpdated() {
        view.startHomeActivity();
    }
}

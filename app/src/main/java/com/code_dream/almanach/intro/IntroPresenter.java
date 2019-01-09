package com.code_dream.almanach.intro;

import com.code_dream.almanach.intro.network.IntroInteractor;
import com.code_dream.almanach.intro.network.OnRegisterFinishedListener;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;

/**
 * Created by Qwerasdzxc on 1/31/17.
 */

public class IntroPresenter implements IntroContract.UserActionsListener, OnRegisterFinishedListener{

    private IntroContract.View view;
    private IntroInteractor introInteractor;

    private boolean canFinishActivity = false;

    public boolean registrationFinished = false;

    public IntroPresenter(IntroContract.View view, IntroInteractor interactor) {
        this.view = view;
        this.introInteractor = interactor;
    }

    @Override
    public boolean canFinish(){
        return canFinishActivity;
    }

    @Override
    public void onFinishClick() {
        if (view.getSlideRegister().canRegister()) {
            view.onRegistrationStart();
            introInteractor.sendRegisterRequest(this, view.getFullName(), view.getDob(), view.getEmail(), view.getPassword(), view.getAllSubjects());
        }
    }

    @Override
    public void onBackClick() {
        canFinishActivity = true;
    }

    @Override
    public void onReject() {
        view.showEmailAlreadyExistsMessage();
    }

    @Override
    public void onFailure() {
        view.showTimeoutDialog();
    }

    @Override
    public void onSuccess(String token) {
        UserInfo.TOKEN = token;
        UserInfo.CURRENT_USER = new Person(view.getFullName());

        if (!view.getSavedFirebaseToken().equals(Registry.EMPTY_TOKEN))
            introInteractor.updateFirebaseToken(this, view.getSavedFirebaseToken());

        registrationFinished = true;
        view.startHomeActivity();
    }

    @Override
    public void onFirebaseTokenUpdated() {
        view.startHomeActivity();
    }
}

package com.code_dream.almanach.school_profile;

import com.code_dream.almanach.school_profile.network.OnSchoolSetListener;
import com.code_dream.almanach.school_profile.network.SchoolProfileInteractor;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public class SchoolProfilePresenter implements SchoolProfileContract.UserActionsListener,
                                               OnSchoolSetListener {

    private SchoolProfileContract.View view;
    private SchoolProfileInteractor interactor;

    public SchoolProfilePresenter(SchoolProfileContract.View view){
        this.view = view;
        interactor = new SchoolProfileInteractor();
    }

    @Override
    public void onSelectSchool() {
        view.showLoadingToast();
        
        interactor.selectSchool(this, view.getSchoolName());
    }

    @Override
    public void onSchoolSuccessfullySet() {
        view.dismissLoadingToast();
    }

    @Override
    public void onFailure() {
        view.dismissLoadingToastWithError();
    }
}

package com.code_dream.almanach.intro.slides.school;

import com.code_dream.almanach.intro.slides.school.network.OnNetworkRequestFinished;
import com.code_dream.almanach.intro.slides.school.network.SchoolInteractor;
import com.code_dream.almanach.models.School;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qwerasdzxc on 7/5/17.
 */

public class SchoolPresenter implements SchoolContract.UserActionsListener,
                                        OnNetworkRequestFinished {

    private enum SelectionMode {
        SelectSchool,
        NewSchool
    }

    private SchoolContract.View view;
    private SchoolInteractor interactor;

    private SelectionMode selectionMode;

    private List<School> schoolList;

    public SchoolPresenter(SchoolContract.View view) {
        this.view = view;
        interactor = new SchoolInteractor(this);

        selectionMode = SelectionMode.SelectSchool;
        schoolList = new ArrayList<>();
    }

    @Override
    public void onQueryChanged(String oldText, String newText) {
        if (newText.isEmpty())
            return;

        view.enableSearchProgressBar(true);
        interactor.searchSchool(newText);
    }

    @Override
    public void onSuggestionClick(String schoolName, String queryText) {
        if (schoolName.equals(Registry.INTRO_ADD_SCHOOL_SUGGESTION_TEXT)) {
            selectionMode = SelectionMode.NewSchool;

            view.switchToAddSchoolLayout(queryText);

            return;
        }

        School schoolObject = null;

        for (School school : schoolList){
            if (school.getName().equals(schoolName)){
                schoolObject = school;
                break;
            }
        }
    }


    @Override
    public void onSchoolsSuccessfullyReceived(List<School> receivedSchools) {
        //Updating list before we add "INTRO_ADD_SCHOOL_SUGGESTION_TEXT"
        schoolList.clear();
        schoolList.addAll(receivedSchools);

        receivedSchools.add(new School(Registry.INTRO_ADD_SCHOOL_SUGGESTION_TEXT));

        view.enableSearchProgressBar(false);
        view.updateSearchView(receivedSchools);
    }

    @Override
    public boolean canMoveFurther() {
        if (selectionMode == SelectionMode.NewSchool) {
            Registry.addingNewSchool = true;
            Registry.schoolName = view.getNewSchoolText();

            if (!view.getNewSchoolText().isEmpty()){
                Registry.userFinishedChoosing = true;
                return true;
            }

            Registry.userFinishedChoosing = false;
            return false;
        }
        else {
            Registry.addingNewSchool = false;
            Registry.schoolName = view.getSearchQuery();

            if (!view.getSearchQuery().isEmpty()){
                Registry.userFinishedChoosing = true;
                return true;
            }

            Registry.userFinishedChoosing = false;
            return false;

        }
    }

    @Override
    public String getMovingPreventionErrorMessage() {
        return "School not selected!";
    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {

    }
}

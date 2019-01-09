package com.code_dream.almanach.subject_list;

import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.subject_list.network.OnNetworkRequestFinished;
import com.code_dream.almanach.subject_list.network.SubjectListInteractor;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public class SubjectListPresenter implements SubjectListContract.UserActionsListener,
                                             OnNetworkRequestFinished {

    private SubjectListContract.View view;

    private SubjectListInteractor interactor;

    public SubjectListPresenter(SubjectListContract.View view) {
        this.view = view;
        interactor = new SubjectListInteractor(this);
    }

    @Override
    public void loadAllSubjects() {
        view.setSwipeRefreshLayoutRefreshing(false);
        view.displayLoadingBar(true);

        // Sending the request to the server to get all existing Subjects.
        interactor.loadAllSubjects();
    }

    @Override
    public void deleteSubject(SubjectListItem subjectToDelete) {
        view.showLoadingToast("Deleting subject...");

        interactor.deleteSubject(subjectToDelete);
    }

    @Override
    public void addNewSubject(String newSubjectName) {
        view.showLoadingToast("Adding subject...");

        // Sending the newly added Subject to the server.
        interactor.addNewSubject(newSubjectName);
    }

    @Override
    public void onSubjectsSuccessfullyLoaded(ArrayList<SubjectListItem> loadedSubjects, boolean loadedFromServer) {
        view.displayLoadingBar(false);
        
        // Show all loaded Subjects in the RecyclerView
        view.displaySubjectList(loadedSubjects);
        view.showFab(loadedFromServer);
    }

    @Override
    public void onSubjectSuccessfullyDeleted(SubjectListItem deletedSubject) {
        view.dismissLoadingToastSuccessfully();

        // Removing the selected Subject from the RecyclerView
        view.removeSubjectFromList(deletedSubject);
    }

    @Override
    public void onSubjectSuccessfullyAdded(SubjectListItem addedSubject) {
        view.dismissLoadingToastSuccessfully();

        // Adding a new Subject with selected name and no Calendar events.
        view.addSubjectToList(addedSubject);
    }

    @Override
    public void onFailure() {
        view.dismissLoadingToastWithFailure();
        view.setSwipeRefreshLayoutRefreshing(false);
        view.displayLoadingBar(false);
    }
}

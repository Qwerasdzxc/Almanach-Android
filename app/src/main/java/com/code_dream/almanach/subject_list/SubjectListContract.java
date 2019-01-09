package com.code_dream.almanach.subject_list;

import com.code_dream.almanach.models.SubjectListItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public interface SubjectListContract {

    interface View {

        void displaySubjectList(ArrayList<SubjectListItem> loadedSubjects);

        void removeSubjectFromList(SubjectListItem subjectToRemove);

        void addSubjectToList(SubjectListItem subjectToAdd);

        void displayLoadingBar(boolean display);

        void showLoadingToast(String text);

        void showFab(boolean show);

        void dismissLoadingToastSuccessfully();

        void dismissLoadingToastWithFailure();

        void setSwipeRefreshLayoutRefreshing(boolean refreshing);
    }

    interface UserActionsListener {

        void loadAllSubjects();

        void deleteSubject(SubjectListItem subjectToDelete);

        void addNewSubject(String newSubjectName);
    }
}

package com.code_dream.almanach.subject_list.network;

import com.code_dream.almanach.models.SubjectListItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public interface OnNetworkRequestFinished {

    void onSubjectsSuccessfullyLoaded(ArrayList<SubjectListItem> loadedSubjects, boolean loadedFromServer);

    void onSubjectSuccessfullyDeleted(SubjectListItem deletedSubject);

    void onSubjectSuccessfullyAdded(SubjectListItem addedSubject);

    void onFailure();
}

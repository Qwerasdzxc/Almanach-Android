package com.code_dream.almanach.subject_list.network;

import com.code_dream.almanach.models.SubjectListItem;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public interface ISubjectListInteractor {

    void loadAllSubjects();

    void deleteSubject(SubjectListItem subjectToDelete);

    void addNewSubject(String subjectName);
}

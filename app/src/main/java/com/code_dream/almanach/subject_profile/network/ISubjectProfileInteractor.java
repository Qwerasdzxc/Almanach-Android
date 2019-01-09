package com.code_dream.almanach.subject_profile.network;

/**
 * Created by Qwerasdzxc on 6/21/17.
 */

public interface ISubjectProfileInteractor {

    void loadAllCalendarEvents(String subjectName);

    void deleteSubject(String subjectName);
}

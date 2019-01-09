package com.code_dream.almanach.subject_profile;

import android.support.annotation.UiThread;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.SubjectCategory;
import com.code_dream.almanach.models.SubjectCategoryItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Qwerasdzxc on 6/19/17.
 */

public interface SubjectProfileContract {

    @UiThread
    interface View {

        void displayAllCalendarEvents(HashMap<EventType, ArrayList<SubjectCategoryItem>> allCalendarEvents);

        void displayNoEventsScreen();

        void displayLoadingBar(boolean display);

        void showFinalGrade(Grades grade);

        void showLoadingToast(String text);

        void dismissLoadingToastSuccessfully();

        void dismissLoadingToastWithFailure();

        void finishActivityAfterSubjectDeletion();

        void finishActivity();

        String getSubjectName();
    }

    interface UserActionsListener {

        void loadCalendarEvents();

        void deleteSubject();

        void finishActivity();

        Grades calculateFinalGrade(ArrayList<SubjectCategory> subjectCategories, boolean displayInView);
    }
}

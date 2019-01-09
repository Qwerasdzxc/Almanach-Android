package com.code_dream.almanach.intro.slides.school;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.School;

import java.util.List;

/**
 * Created by Qwerasdzxc on 7/5/17.
 */

public interface SchoolContract {

    @UiThread
    interface View {

        void enableSearchProgressBar(boolean enable);

        void updateSearchView(List<School> receivedSchools);

        void switchToAddSchoolLayout(String typedText);

        void updateNewSchoolText(String newSchoolText);

        String getSearchQuery();

        String getNewSchoolText();
    }

    interface UserActionsListener {

        void onQueryChanged(String oldText, String newText);

        void onSuggestionClick(String schoolName, String queryText);

        boolean canMoveFurther();

        String getMovingPreventionErrorMessage();
    }
}

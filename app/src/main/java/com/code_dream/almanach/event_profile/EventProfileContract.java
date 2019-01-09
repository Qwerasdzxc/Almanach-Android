package com.code_dream.almanach.event_profile;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.CalendarEvent;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public interface EventProfileContract {

    @UiThread
    interface View {

        void enableSaveButton(boolean enable);

        void setImageViewSelected(int imageViewID);

        void setImageViewDeselected(int imageViewID);

        void finishActivity(boolean eventDeleted);

        void showLoadingToast();

        void dismissLoadingToastSuccessfully();

        void dismissLoadingToastWithError();

        void displaySaveConfirmationDialog();

        void showInternetConnectionProblemSnackbar();

        CalendarEvent getOriginalCalendarEvent();

        String getEventDate();

        String getEventDescription();

        String getEventTitle();
    }

    interface UserActionsListener {

        void setOriginallySelectedGrade(Grades selectedGrade);

        void saveEventChanges(boolean finishActivity);

        void onDeleteClicked();

        void onGradeSelected(Grades selectedGrade);

        void onDateChanged(String currentDate);

        void onDescriptionTextChanged(String currentText);

        void onTitleTextChanged(String currentText);

        void onReturnToSubjectActivity();

        void highlightEventGrade();

        void onRetryInternetConnection();
    }
}

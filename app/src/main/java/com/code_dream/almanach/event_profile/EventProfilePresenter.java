package com.code_dream.almanach.event_profile;


import com.code_dream.almanach.event_profile.network.EventProfileInteractor;
import com.code_dream.almanach.event_profile.network.EventProfileNetworkCallback;
import com.code_dream.almanach.utility.Utility;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public class EventProfilePresenter implements EventProfileContract.UserActionsListener,
                                              EventProfileNetworkCallback {

    private EventProfileContract.View view;
    private EventProfileInteractor interactor;

    // Did the user change the title of the event?
    private boolean titleChanged;

    // Did the user change anything in the description?
    private boolean descriptionChanged;

    // Did the user change the date of the event?
    private boolean dateChanged;

    // Did the user change the event grade?
    private boolean gradeChanged;

    // Grade that the user selects.
    private Grades selectedGrade;

    // True when grade gets de-selected.
    private boolean gradeSwitchedState;

    public EventProfilePresenter(EventProfileContract.View view){
        this.view = view;
        interactor = new EventProfileInteractor(this);
    }

    @Override
    public void setOriginallySelectedGrade(Grades selectedGrade) {
        this.selectedGrade = selectedGrade;
    }

    @Override
    public void saveEventChanges(boolean finishActivity) {
        view.showLoadingToast();

        view.getOriginalCalendarEvent().setDate(view.getEventDate());
        view.getOriginalCalendarEvent().setTitle(view.getEventTitle());
        view.getOriginalCalendarEvent().setDescription(view.getEventDescription());
        view.getOriginalCalendarEvent().setGrade(selectedGrade);

        interactor.saveEventChanges(view.getOriginalCalendarEvent(), finishActivity);
    }

    @Override
    public void onDeleteClicked() {
        interactor.deleteEvent(view.getOriginalCalendarEvent());
    }

    @Override
    public void highlightEventGrade() {
        // If the event has no grade, then we don't have any ImageView to highlight.
        if (view.getOriginalCalendarEvent().getGrade() == Grades.GRADE_NONE)
            return;

        view.setImageViewSelected(Utility.getImageViewIDByGrade(view.getOriginalCalendarEvent().getGrade(), false));
    }

    @Override
    public void onGradeSelected(Grades selectedGrade) {
        // Grade changed if the selected grade doesn't match
        // the previously selected one.
        gradeChanged = !selectedGrade.equals(this.selectedGrade);

        if (gradeChanged || gradeSwitchedState) {
            // Make the appropriate ImageView highlighted in the view.
            view.setImageViewSelected(Utility.getImageViewIDByGrade(selectedGrade, false));

            // Updating the selected grade.
            this.selectedGrade = selectedGrade;

            // Since different grade was selected, that means that the grade
            // didn't switch it's state (from selected to de-selected)
            gradeSwitchedState = false;

            // If the currently selected grade matched the original one, then there is nothing to update on the server.
            if (this.selectedGrade == view.getOriginalCalendarEvent().getGrade())
                gradeChanged = false;
        }
        else {
            // Make the currently highlighted ImageView normal again.
            view.setImageViewDeselected(Utility.getImageViewIDByGrade(selectedGrade, false));

            // Updating the selected grade.
            this.selectedGrade = Grades.GRADE_NONE;

            // Same grade was selected so that means we want to switch it's state (from de-selected to selected).
            gradeSwitchedState = true;

            // If the currently selected grade doesn't match the original one, then we want to update it on the server.
            if (this.selectedGrade != view.getOriginalCalendarEvent().getGrade())
                gradeChanged = true;
        }

        // Notifying the user that some changes were made.
        this.changesMade();
    }

    @Override
    public void onDateChanged(String currentDate) {
        // Date changed if the date text doesn't match
        // the original calendar event's date.
        dateChanged = !currentDate.equals(view.getOriginalCalendarEvent().getDate());

        // Notifying the user that some changes were made.
        this.changesMade();
    }

    @Override
    public void onTitleTextChanged(String currentText) {
        // Description changed if the description text doesn't match
        // the original calendar event's description.
        titleChanged = !currentText.equals(view.getOriginalCalendarEvent().getTitle());

        // Notifying the user that some changes were made.
        this.changesMade();
    }

    @Override
    public void onDescriptionTextChanged(String currentText) {
        // Description changed if the description text doesn't match
        // the original calendar event's description.
        descriptionChanged = !currentText.equals(view.getOriginalCalendarEvent().getDescription());

        // Notifying the user that some changes were made.
        this.changesMade();
    }

    @Override
    public void onReturnToSubjectActivity() {
        if (!dateChanged && !titleChanged && !descriptionChanged && !gradeChanged) {
            // Nothing has changed in the event, so the user can leave the activity.
            view.finishActivity(false);
        } else {
            // Something has changed so we display the confirmation dialog.
            view.displaySaveConfirmationDialog();
        }
    }

    private void changesMade() {
        if (!dateChanged && !titleChanged && !descriptionChanged && !gradeChanged) {
            // If nothing has changed, we disable the save button
            view.enableSaveButton(false);
        }
        else {
            // Else, the user changed something in the event so the save button is enabled.
            view.enableSaveButton(true);
        }
    }

    @Override
    public void onEventSuccessfullySaved(boolean finishActivity) {
        // Restarting the save button since the saved event is now the original one.
        resetSaveButton();

        view.dismissLoadingToastSuccessfully();

        // If the view requested a finish of the activity, we close it.
        if (finishActivity)
            view.finishActivity(false);
    }

    @Override
    public void onEventSuccessfullyDeleted() {
        view.finishActivity(true);
    }

    @Override
    public void onRetryInternetConnection() {
        if (descriptionChanged || titleChanged || dateChanged || gradeChanged) {
            // If anything was changed by the user we retry the save request.
            this.saveEventChanges(false);
        }
    }

    @Override
    public void onServerError() {
        view.dismissLoadingToastWithError();
    }

    @Override
    public void onInternetConnectionProblem() {
        view.dismissLoadingToastWithError();
        view.showInternetConnectionProblemSnackbar();
    }

    private void resetSaveButton() {
        dateChanged = false;
        titleChanged = false;
        descriptionChanged = false;
        gradeChanged = false;

        view.enableSaveButton(false);
    }
}

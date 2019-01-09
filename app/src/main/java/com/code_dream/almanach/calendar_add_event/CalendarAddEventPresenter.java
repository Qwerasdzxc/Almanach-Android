package com.code_dream.almanach.calendar_add_event;

import com.code_dream.almanach.calendar_add_event.network.CalendarAddEventInteractor;
import com.code_dream.almanach.calendar_add_event.network.OnNetworkRequestFinished;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.utility.Utility;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public class CalendarAddEventPresenter implements CalendarAddEventContract.UserActionsListener,
        OnNetworkRequestFinished {

    private CalendarAddEventContract.View view;
    private CalendarAddEventInteractor interactor;

    // Event type that the user selects.
    private EventType selectedEventType;

    // Grade that the user selects.
    private Grades selectedGrade;

    // True when any event type gets selected.
    private boolean eventTypeSelected;

    // Did the user change the event grade?
    private boolean gradeChanged;

    private boolean gradeSwitchedState;

    public CalendarAddEventPresenter(CalendarAddEventContract.View view, CalendarAddEventInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onAddEventClick() {
        view.showLoadingToast();

        if ((view.getSelectedCalendarDay() != null || eventTypeSelected) && view.isRequiredDataFilled())
            interactor.addNewEvent(this, view.getSelectedSubject(), view.getSelectedDate(), selectedGrade, eventTypeSelected ? selectedEventType : view.getSelectedEventType(), view.getEventTitle(), view.getEventDescription());
        else {
            view.onLoadingToastError();
            view.showRequiredDataErrorToast();
        }
    }

    @Override
    public void loadSubjects() {
        view.showLoadingBar(true);

        interactor.loadAllSubjects(this);
    }

    @Override
    public void onEventTypeSelected(EventType eventType) {
        // Highlight the selected Event type.
        view.setEventImageViewSelected(Utility.getImageViewIDByEvent(eventType));

        this.eventTypeSelected = true;
        this.selectedEventType = eventType;
    }

    @Override
    public void onGradeSelected(Grades selectedGrade) {
        // Grade changed if the selected grade doesn't match
        // the previously selected one.
        gradeChanged = !selectedGrade.equals(this.selectedGrade);

        if (gradeChanged || !gradeSwitchedState) {
            // Make the appropriate ImageView highlighted in the view.
            view.setGradeImageViewSelected(Utility.getImageViewIDByGrade(selectedGrade, true));

            // Since different grade was selected, that means that the grade
            // didn't switch it's state (from selected to de-selected)
            gradeSwitchedState = true;

            // If the currently selected grade matched the original one, then there is nothing to update on the server.
            if (this.selectedGrade == selectedGrade)
                gradeChanged = false;

            // Updating the selected grade.
            this.selectedGrade = selectedGrade;
        }
        else {
            // Make the currently highlighted ImageView normal again.
            view.setImageViewDeselected(Utility.getImageViewIDByGrade(selectedGrade, true));

            // Same grade was selected so that means we want to switch it's state (from de-selected to selected).
            gradeSwitchedState = false;

            // If the currently selected grade doesn't match the original one, then we want to update it on the server.
            if (this.selectedGrade != selectedGrade)
                gradeChanged = true;

            // Updating the selected grade.
            this.selectedGrade = Grades.GRADE_NONE;
        }
    }

    @Override
    public void onEventSuccessfullyAdded(CalendarEvent calendarEvent) {
        view.onLoadingToastSuccess();

        if (view.shouldSendNotification())
            view.setNotificationAlarm(new GregorianCalendar(view.getSelectedCalendarDay().getYear(), view.getSelectedCalendarDay().getMonth(), view.getSelectedCalendarDay().getDay())
                    .getTimeInMillis() - 1000 * view.getNotificationDelayInHours() * 60 * 60);

        view.returnToHomeActivity(calendarEvent);
    }

    @Override
    public void onSubjectsSuccessfullyLoaded(ArrayList<SubjectListItem> loadedSubjects) {
        ArrayList<String> subjectNames = new ArrayList<>(loadedSubjects.size());

        for (SubjectListItem item : loadedSubjects)
            subjectNames.add(item.getSubjectName());

        view.showLoadingBar(false);
        view.loadSubjectsSpinner(subjectNames);
    }

    @Override
    public void onFailure() {
        view.onLoadingToastError();
    }
}

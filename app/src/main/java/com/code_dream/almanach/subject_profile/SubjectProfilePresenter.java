package com.code_dream.almanach.subject_profile;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.SubjectCategory;
import com.code_dream.almanach.models.SubjectCategoryItem;
import com.code_dream.almanach.subject_profile.network.OnNetworkRequestFinished;
import com.code_dream.almanach.subject_profile.network.SubjectProfileInteractor;
import com.code_dream.almanach.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Qwerasdzxc on 6/19/17.
 */

public class SubjectProfilePresenter implements SubjectProfileContract.UserActionsListener,
                                                OnNetworkRequestFinished {

    private SubjectProfileContract.View view;
    private SubjectProfileInteractor interactor;

    public SubjectProfilePresenter(SubjectProfileContract.View view){
        this.view = view;
        interactor = new SubjectProfileInteractor(this);
    }

    @Override
    public void loadCalendarEvents() {
        view.displayLoadingBar(true);

        interactor.loadAllCalendarEvents(view.getSubjectName());
    }

    @Override
    public void deleteSubject() {
        view.showLoadingToast("Deleting subject...");

        interactor.deleteSubject(view.getSubjectName());
    }

    @Override
    public void finishActivity() {
        view.finishActivity();
    }

    @Override
    public void onCalendarEventsSuccessfullyLoaded(ArrayList<CalendarEvent> calendarEvents) {
        // Loading from the server finished so we dismiss the loading bar.
        view.displayLoadingBar(false);

        // If there are no calendar events then we skip
        // the sorting of events and just display an empty list.
        if (calendarEvents.isEmpty()) {
            view.displayNoEventsScreen();
            return;
        }

        // HashMap which will store calendar events by their event type.
        HashMap<EventType, ArrayList<SubjectCategoryItem>> map = new HashMap<>();

        ArrayList<SubjectCategoryItem> testItems = new ArrayList<>();
        ArrayList<SubjectCategoryItem> homeworkItems = new ArrayList<>();
        ArrayList<SubjectCategoryItem> presentationItems = new ArrayList<>();
        ArrayList<SubjectCategoryItem> otherAssignmentItems = new ArrayList<>();

        // Sorting calendar events by their type.
        for (CalendarEvent event : calendarEvents){
            switch (event.getEventType()){
                case TEST:
                    testItems.add(new SubjectCategoryItem(event.getTitle(), event));
                    break;
                case HOME_WORK:
                    homeworkItems.add(new SubjectCategoryItem(event.getTitle(), event));
                    break;
                case PRESENTATION:
                    presentationItems.add(new SubjectCategoryItem(event.getTitle(), event));
                    break;
                case OTHER_ASSIGNMENT:
                    otherAssignmentItems.add(new SubjectCategoryItem(event.getTitle(), event));
                    break;
            }
        }

        // Every event type has it's array of events.
        for (EventType eventType : EventType.values())
            switch (eventType){
                case TEST:
                    map.put(EventType.TEST, testItems);
                    break;
                case HOME_WORK:
                    map.put(EventType.HOME_WORK, homeworkItems);
                    break;
                case PRESENTATION:
                    map.put(EventType.PRESENTATION, presentationItems);
                    break;
                case OTHER_ASSIGNMENT:
                    map.put(EventType.OTHER_ASSIGNMENT, otherAssignmentItems);
                    break;
            }

        // Display the populated HashMap in the RecyclerView.
        view.displayAllCalendarEvents(map);

        ArrayList<Grades> allGrades = new ArrayList<>(calendarEvents.size());

        // Adding all existing grades from all calendar events to the single array.
        for (CalendarEvent calendarEvent : calendarEvents) {
            if (calendarEvent.getGrade() != Grades.GRADE_NONE)
                allGrades.add(calendarEvent.getGrade());
        }

        // If the subject doesn't have any existing grades,
        // then we don't have anything to calculate.
        if (allGrades.isEmpty()) {
            view.showFinalGrade(Grades.GRADE_NONE);
            return;
        }

        // Calculate the final grade given all grades array.
        view.showFinalGrade(Utility.calculateFinalGrade(allGrades));
    }

    @Override
    public void onSubjectSuccessfullyDeleted() {
        view.dismissLoadingToastSuccessfully();
        view.finishActivityAfterSubjectDeletion();
    }

    @Override
    public void onServerError() {
        view.dismissLoadingToastWithFailure();
    }

    @Override
    public void onInternetConnectionProblem() {
        view.dismissLoadingToastWithFailure();
    }

    @Override
    public Grades calculateFinalGrade(ArrayList<SubjectCategory> subjectCategories, boolean showInView) {
        ArrayList<CalendarEvent> loadedCalendarEvents = new ArrayList<>(subjectCategories.size());

        // Populating the loaded calendar events array.
        for (SubjectCategory subjectCategory : subjectCategories) {
            List<SubjectCategoryItem> subjectCategoryItems = subjectCategory.getItems();
            for (SubjectCategoryItem item : subjectCategoryItems)
                loadedCalendarEvents.add(item.getCalendarEvent());
        }

        ArrayList<Grades> allGrades = new ArrayList<>(loadedCalendarEvents.size());

        // Adding all existing grades from all calendar events to the single array.
        for (CalendarEvent calendarEvent : loadedCalendarEvents) {
            if (calendarEvent.getGrade() != Grades.GRADE_NONE)
                allGrades.add(calendarEvent.getGrade());
        }

        // If the subject doesn't have any existing grades,
        // then we don't have anything to calculate.
        if (allGrades.isEmpty()) {
            if (showInView)
                view.showFinalGrade(Grades.GRADE_NONE);

            return Grades.GRADE_NONE;
        }

        // Calculate the final grade given all grades array.
        Grades finalGrade = Utility.calculateFinalGrade(allGrades);

        if (showInView) {
            // If the view requested the final grade
            // to be shown, we let that happen.
            view.showFinalGrade(finalGrade);
        }

        return finalGrade;
    }
}

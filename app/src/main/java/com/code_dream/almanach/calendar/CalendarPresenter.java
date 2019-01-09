package com.code_dream.almanach.calendar;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.calendar.network.CalendarInteractor;
import com.code_dream.almanach.calendar.network.OnCalendarRequestFinishedListener;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.Utility;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/4/17.
 */

public class CalendarPresenter implements CalendarContract.UserActionsListener,
        OnCalendarRequestFinishedListener {

    private CalendarContract.View view;
    private CalendarInteractor calendarInteractor;

    // Decorator ArrayLists
    private ArrayList<CalendarDay> calendarDaysWithTest = new ArrayList<>();
    private ArrayList<CalendarDay> calendarDaysWithHomework = new ArrayList<>();
    private ArrayList<CalendarDay> calendarDaysWithPresentation = new ArrayList<>();
    private ArrayList<CalendarDay> calendarDaysWithOtherAssignment = new ArrayList<>();

    private ArrayList<DateTime> loadedCalendarDates = new ArrayList<>();
    private ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

    private boolean dataLoadedFromServer;

    public CalendarPresenter(CalendarContract.View view, CalendarInteractor calendarInteractor) {
        this.view = view;
        this.calendarInteractor = calendarInteractor;
    }

    @Override
    public void onRecyclerViewClick(Class<?> viewHolder, int position) {
        if (viewHolder == RecyclerViewAdapter.CalendarEventTypeBigViewHolder.class)
            view.startAddEventActivity(EventType.values()[position]);
        else
            Log.d("CalendarPresenter", "Calendar event info:" + view.getCalendarEventFromRecyclerView(position).getTitle());
    }

    @Override
    public void onContextMenuClick(ImageView imgContextMenu, CalendarEvent calendarEvent) {
        view.showContextMenu(imgContextMenu, calendarEvent);
    }

    @Override
    public void onEventTypeClick(EventType selectedEventType) {
        view.startAddEventActivity(selectedEventType);
    }

    @Override
    public void onDateSelected() {
        boolean datesMatch = false;

        DateTime selectedDate = view.getSelectedDateTime();
        ArrayList<CalendarEvent> calendarEventsForSelectedDay = new ArrayList<>();

        for (DateTime date : loadedCalendarDates) {
            if (Utility.checkIfDatesAreSame(date, selectedDate)) {
                for (CalendarEvent calendarEvent : calendarEvents) {
                    if (Utility.checkIfDatesAreSame(calendarEvent, selectedDate)) {
                        calendarEventsForSelectedDay.add(calendarEvent);
                        datesMatch = true;
                    }
                }
                // Breaks the loop when selected date and loaded event
                // date match (so that there are no event duplicates)
                break;
            }
        }

        if (datesMatch) {
            view.showOfflineModeView(false);
            view.showLoadedEventsRecyclerView(calendarEventsForSelectedDay);
        }
        else if (dataLoadedFromServer) {
            view.showOfflineModeView(false);
            view.showEventSelectionRecyclerView();
        }
        else
            view.showOfflineModeView(true);
    }

    @Override
    public void loadAllCalendarEvents() {
        view.setProgressBarVisibility(View.VISIBLE);
        calendarInteractor.loadAllCalendarEvents(this);
    }

    @Override
    public void editEvent(CalendarEvent calendarEvent) {
        view.startEditEventActivity(calendarEvent);
    }

    @Override
    public void deleteEvent(CalendarEvent calendarEvent) {
        view.showEventDeletingLoadToast();
        calendarInteractor.deleteEvent(this, calendarEvent);
    }

    @Override
    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Registry.ACTIVITY_RESULT_OK) {

            CalendarEvent calendarEvent = data.getParcelableExtra("calendar_event");

            int eventTypeColor = Utility.getEventTypeColor(calendarEvent.getEventType());

            switch (calendarEvent.getEventType()) {
                case TEST:
                    calendarDaysWithTest.add(calendarEvent.getCalendarDay());
                    view.addCalendarDecorator(eventTypeColor, calendarDaysWithTest);
                    break;
                case HOME_WORK:
                    calendarDaysWithHomework.add(calendarEvent.getCalendarDay());
                    view.addCalendarDecorator(eventTypeColor, calendarDaysWithHomework);
                    break;
                case PRESENTATION:
                    calendarDaysWithPresentation.add(calendarEvent.getCalendarDay());
                    view.addCalendarDecorator(eventTypeColor, calendarDaysWithPresentation);
                    break;
                case OTHER_ASSIGNMENT:
                    calendarDaysWithOtherAssignment.add(calendarEvent.getCalendarDay());
                    view.addCalendarDecorator(eventTypeColor, calendarDaysWithOtherAssignment);
                    break;
            }

            loadedCalendarDates.add(view.getSelectedDateTime());
            calendarEvents.add(calendarEvent);

            this.onDateSelected();
        }
    }

    @Override
    public void onEventsSuccessfullyLoaded(ArrayList<CalendarEvent> loadedCalendarEvents, boolean loadedFromServer) {
        dataLoadedFromServer = loadedFromServer;

        for (CalendarEvent calendarEvent: loadedCalendarEvents)
            calendarEvent.setCalendarDay(new CalendarDay(calendarEvent.getYear(), calendarEvent.getMonth() - 1, calendarEvent.getDay()));

        calendarEvents.clear();
        calendarEvents.addAll(loadedCalendarEvents);

        calendarDaysWithTest.clear();
        calendarDaysWithHomework.clear();
        calendarDaysWithPresentation.clear();
        calendarDaysWithOtherAssignment.clear();

        updateCalendarDecorators();
    }

    @Override
    public void onContextMenuItemClick(MenuItem item, CalendarEvent calendarEvent) {
        switch (item.getItemId()) {
            case R.id.action_edit_event:
                this.editEvent(calendarEvent);
                break;
            case R.id.action_delete_event:
                this.deleteEvent(calendarEvent);
                break;
        }
    }

    @Override
    public void onEventSuccessfullyEdited() {

    }

    @Override
    public void onEventSuccessfullyDeleted(CalendarEvent deletedCalendarEvent) {
        calendarEvents.remove(deletedCalendarEvent);
        CalendarDay deletedEventCalendarDay = deletedCalendarEvent.getCalendarDay();

        switch (deletedCalendarEvent.getEventType()){
            case TEST:
                calendarDaysWithTest.remove(deletedEventCalendarDay);
                break;
            case HOME_WORK:
                calendarDaysWithHomework.remove(deletedEventCalendarDay);
                break;
            case PRESENTATION:
                calendarDaysWithPresentation.remove(deletedEventCalendarDay);
                break;
            case OTHER_ASSIGNMENT:
                calendarDaysWithOtherAssignment.remove(deletedEventCalendarDay);
                break;
        }

        view.removeEventFromList(deletedCalendarEvent);
    }

    @Override
    public void onFailure() {
        view.dismissLoadToastWithError();
        view.setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void updateCalendarDecorators() {
        view.removeAllCalendarDecorators();

        for (CalendarEvent calendarEvent : calendarEvents) {
            CalendarDay calendarDay = new CalendarDay(calendarEvent.getYear(), calendarEvent.getMonth() - 1, calendarEvent.getDay());
            EventType eventType = calendarEvent.getEventType();

            switch (eventType) {
                case TEST:
                    calendarDaysWithTest.add(calendarDay);
                    view.addCalendarDecorator(Utility.getEventTypeColor(eventType), calendarDaysWithTest);
                    break;
                case HOME_WORK:
                    calendarDaysWithHomework.add(calendarDay);
                    view.addCalendarDecorator(Utility.getEventTypeColor(eventType), calendarDaysWithHomework);
                    break;
                case PRESENTATION:
                    calendarDaysWithPresentation.add(calendarDay);
                    view.addCalendarDecorator(Utility.getEventTypeColor(eventType), calendarDaysWithPresentation);
                    break;
                case OTHER_ASSIGNMENT:
                    calendarDaysWithOtherAssignment.add(calendarDay);
                    view.addCalendarDecorator(Utility.getEventTypeColor(eventType), calendarDaysWithOtherAssignment);
                    break;
            }
            DateTime loadedDateTime = new DateTime(calendarEvent.getYear(), calendarEvent.getMonth(), calendarEvent.getDay(), 0, 0);
            loadedCalendarDates.add(loadedDateTime);
        }

        view.setProgressBarVisibility(View.GONE);
        this.onDateSelected();
    }
}

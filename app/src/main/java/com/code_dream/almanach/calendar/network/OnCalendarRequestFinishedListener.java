package com.code_dream.almanach.calendar.network;

import com.code_dream.almanach.models.CalendarEvent;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/23/17.
 */

public interface OnCalendarRequestFinishedListener {

    void onEventsSuccessfullyLoaded(ArrayList<CalendarEvent> calendarEvents, boolean loadedFromServer);

    void onEventSuccessfullyEdited();

    void onEventSuccessfullyDeleted(CalendarEvent deletedCalendarEvent);

    void onFailure();
}

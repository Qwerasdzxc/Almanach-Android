package com.code_dream.almanach.calendar.network;

import com.code_dream.almanach.models.CalendarEvent;

/**
 * Created by Qwerasdzxc on 2/23/17.
 */

public interface ICalendarInteractor {

    void loadAllCalendarEvents(OnCalendarRequestFinishedListener listener);

    void editEvent(OnCalendarRequestFinishedListener listener, CalendarEvent calendarEvent);

    void deleteEvent(OnCalendarRequestFinishedListener listener, CalendarEvent calendarEvent);
}

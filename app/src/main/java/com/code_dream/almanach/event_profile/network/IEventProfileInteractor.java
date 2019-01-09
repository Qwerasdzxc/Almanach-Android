package com.code_dream.almanach.event_profile.network;

import com.code_dream.almanach.models.CalendarEvent;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public interface IEventProfileInteractor {

    void saveEventChanges(CalendarEvent calendarEvent, boolean finishActivity);

    void deleteEvent(CalendarEvent calendarEvent);
}

package com.code_dream.almanach.calendar_add_event.network;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public interface ICalendarAddEventInteractor {

    void addNewEvent(OnNetworkRequestFinished listener, String subject, String date, Grades grade, EventType eventType, String title, String description);

    void loadAllSubjects(OnNetworkRequestFinished listener);
}

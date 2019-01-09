package com.code_dream.almanach.subject_profile.network;

import com.code_dream.almanach.models.CalendarEvent;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/21/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished{

    void onCalendarEventsSuccessfullyLoaded(ArrayList<CalendarEvent> calendarEvents);

    void onSubjectSuccessfullyDeleted();

}

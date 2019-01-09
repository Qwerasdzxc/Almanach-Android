package com.code_dream.almanach.calendar_add_event.network;

import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.SubjectListItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/22/17.
 */

public interface OnNetworkRequestFinished {

    void onEventSuccessfullyAdded(CalendarEvent calendarEvent);

    void onFailure();

    void onSubjectsSuccessfullyLoaded(ArrayList<SubjectListItem> loadedSubjects);
}

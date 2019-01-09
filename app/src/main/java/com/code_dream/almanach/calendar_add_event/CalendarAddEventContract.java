package com.code_dream.almanach.calendar_add_event;

import android.support.annotation.UiThread;

import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public interface CalendarAddEventContract {

    @UiThread
    interface View {

        void returnToHomeActivity(CalendarEvent calendarEvent);

        boolean shouldSendNotification();

        void showLoadingToast();

        void onLoadingToastSuccess();

        void onLoadingToastError();

        String getSelectedSubject();

        String getSelectedDate();

        EventType getSelectedEventType();

        CalendarDay getSelectedCalendarDay();

        String getEventDescription();

        String getEventTitle();

        void setNotificationAlarm(Long alertTime);

        int getNotificationDelayInHours();

        void showLoadingBar(boolean show);

        void loadSubjectsSpinner(ArrayList<String> subjectNames);

        boolean isRequiredDataFilled();

        void showRequiredDataErrorToast();

        void setEventImageViewSelected(int id);

        void setGradeImageViewSelected(int id);

        void setImageViewDeselected(int id);
    }

    interface UserActionsListener {

        void onAddEventClick();

        void loadSubjects();

        void onEventTypeSelected(EventType eventType);

        void onGradeSelected(Grades grades);
    }
}

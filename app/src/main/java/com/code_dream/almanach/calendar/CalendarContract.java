package com.code_dream.almanach.calendar;

import android.content.Intent;
import android.support.annotation.UiThread;
import android.view.MenuItem;
import android.widget.ImageView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.models.CalendarEvent;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/4/17.
 */

public interface CalendarContract {

    @UiThread
    interface View {

        void showEventSelectionRecyclerView();

        void showLoadedEventsRecyclerView(ArrayList<CalendarEvent> calendarEventsForSelectedDay);

        void showOfflineModeView(boolean show);

        void addCalendarDecorator(int eventColor, ArrayList<CalendarDay> calendarDays);

        void dismissLoadToastWithError();

        void startAddEventActivity(EventType selectedEventType);

        void startEditEventActivity(CalendarEvent calendarEvent);

        void setProgressBarVisibility(int visibility);

        void showContextMenu(ImageView imgContextMenu, CalendarEvent calendarEvent);

        void showEventDeletingLoadToast();

        void removeEventFromList(CalendarEvent deletedCalendarEvent);

        void removeAllCalendarDecorators();

        CalendarEvent getCalendarEventFromRecyclerView(int position);

        DateTime getSelectedDateTime();
    }

    interface UserActionsListener {

        void loadAllCalendarEvents();

        void onEventTypeClick(EventType selectedEventType);

        void onDateSelected();

        void onActivityResult(int resultCode, Intent data);

        void onRecyclerViewClick(Class<?> viewHolder, int position);

        void onContextMenuClick(ImageView imgContextMenu, CalendarEvent calendarEvent);

        void onContextMenuItemClick(MenuItem item, CalendarEvent calendarEvent);

        void editEvent(CalendarEvent calendarEvent);

        void deleteEvent(CalendarEvent calendarEvent);

        void updateCalendarDecorators();
    }
}

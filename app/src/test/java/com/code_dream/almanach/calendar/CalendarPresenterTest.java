package com.code_dream.almanach.calendar;

import com.code_dream.almanach.calendar.network.CalendarInteractor;
import com.code_dream.almanach.calendar.network.OnCalendarRequestFinishedListener;
import com.code_dream.almanach.models.CalendarEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Created by Qwerasdzxc on 3/2/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class CalendarPresenterTest {

    @Mock
    CalendarInteractor interactor;

    @Mock
    CalendarContract.View view;

    @Captor
    ArgumentCaptor<OnCalendarRequestFinishedListener> listenerArgumentCaptor;

    CalendarPresenter presenter;

    ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

    @Before
    public void setUp() {
        presenter = new CalendarPresenter(view, interactor);

//        calendarEvents.add(new CalendarEvent("TimetableItem", "TEST", "Title", "Description", "01/01/2011", 0, new CalendarDay(2011, 0, 1)));
//        calendarEvents.add(new CalendarEvent("Subject2", "HOME_WORK", "Title2", "Description2", "03/01/2017", 1, new CalendarDay(2017, 0, 3)));
//        calendarEvents.add(new CalendarEvent("Subject3", "PRESENTATION", "Title3", "Description3", "18/01/2003", 2, new CalendarDay(2003, 0, 18)));
    }

    @Test
    public void test(){
        presenter.onFailure();

        verify(view).dismissLoadToastWithError();
    }

//    @TEST
//    public void shouldShowLoadedEventsRecyclerViewWhenEventsGetLoaded(){
//        when(view.getSelectedDateTime()).thenReturn(new DateTime(2017, 1, 3, 0, 0));
//
//        presenter.loadAllCalendarEvents();
//
//        verify(interactor, times(1)).loadAllCalendarEvents(listenerArgumentCaptor.capture());
//
//        listenerArgumentCaptor.getValue().onEventsSuccessfullyLoaded(calendarEvents);
//
//        ArrayList<CalendarEvent> calendarEventsForSelectedDay = new ArrayList<>();
//        calendarEventsForSelectedDay.add(calendarEvents.get(1));
//
//        verify(view).showLoadedEventsRecyclerView(calendarEventsForSelectedDay);
//    }
//
//    @TEST public void shouldShowEventTypeSelectionRecyclerViewWhenLoadedEventsDontMatchWithSelectedDay(){
//        when(view.getSelectedDateTime()).thenReturn(new DateTime(2010, 10, 10, 0, 0));
//
//        presenter.loadAllCalendarEvents();
//
//        verify(interactor, times(1)).loadAllCalendarEvents(listenerArgumentCaptor.capture());
//
//        listenerArgumentCaptor.getValue().onEventsSuccessfullyLoaded(calendarEvents);
//
//        verify(view).showEventSelectionRecyclerView();
//    }
//
//    @TEST public void shouldDismissProgressBarWhenNoEventsGetLoaded(){
//        presenter.onNoEventsLoaded();
//
//        verify(view).setProgressBarVisibility(View.GONE);
//    }
//
//    @TEST public void shouldRemoveEventFromRecyclerViewWhenDeleted(){
//        CalendarEvent deletedCalendarEvent = calendarEvents.get(0);
//
//        presenter.onEventSuccessfullyDeleted(deletedCalendarEvent);
//
//        verify(view).removeEventFromList(deletedCalendarEvent);
//    }

//    @TEST public void shouldNotifyUserWhenNetworkRequestFailed(){
//        presenter.onFailure();
//
//        verify(view).dismissLoadToastWithError();
//        verify(view).setProgressBarVisibility(View.VISIBLE);
//    }


}

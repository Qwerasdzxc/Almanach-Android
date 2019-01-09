package com.code_dream.almanach.calendar_add_event;

import com.code_dream.almanach.calendar_add_event.network.CalendarAddEventInteractor;
import com.code_dream.almanach.calendar_add_event.network.OnNetworkRequestFinished;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Qwerasdzxc on 3/2/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class CalendarAddEventPresenterTest {

    @Mock
    CalendarAddEventInteractor interactor;

    @Mock
    CalendarAddEventContract.View view;

    @Captor
    ArgumentCaptor<OnNetworkRequestFinished> listenerArgumentCaptor;

    CalendarAddEventPresenter presenter;

    @Before
    public void setUp(){
        presenter = new CalendarAddEventPresenter(view, interactor);
    }

    @Test public void shouldReturnToHomeActivityAfterAddingNewEvent(){
        String subject = "Srpski";
        String selectedDate = "1/1/2001";
        EventType selectedEventType = EventType.TEST;
        String eventTitle = "Event title";
        String eventDescription = "Event description";

        when(view.getSelectedSubject()).thenReturn(subject);
        when(view.getSelectedDate()).thenReturn(selectedDate);
        when(view.getSelectedEventType()).thenReturn(selectedEventType);
        when(view.getEventTitle()).thenReturn(eventTitle);
        when(view.getEventDescription()).thenReturn(eventDescription);

        presenter.onAddEventClick();

        verify(interactor, times(1)).addNewEvent(listenerArgumentCaptor.capture(), eq(subject), eq(selectedDate), eq(selectedEventType), eq(eventTitle), eq(eventDescription));
        listenerArgumentCaptor.getValue().onEventSuccessfullyAdded(1);

        verify(view).onLoadingToastSuccess();
        verify(view).returnToHomeActivity();
    }

    @Test public void shouldDismissLoadToastWithErrorOnNetworkRequestFailure(){
        presenter.onFailure();

        verify(view).onLoadingToastError();
    }
}

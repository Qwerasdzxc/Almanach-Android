package com.code_dream.almanach.calendar_add_event.network;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.CreateCalendarEventRequest;
import com.code_dream.almanach.network.responses.CalendarEventAddedResponse;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/3/17.
 */

public class CalendarAddEventInteractor implements ICalendarAddEventInteractor {

    @Override
    public void addNewEvent(final OnNetworkRequestFinished listener, String subject, String date, Grades grade, EventType eventType, String title, String description) {
        Call<CalendarEvent> call = new RestClient().getApiService().addCalendarEvent(new CreateCalendarEventRequest(subject, date, grade, eventType, title, description));

        call.enqueue(new Callback<CalendarEvent>() {
            @Override
            public void onResponse(Call<CalendarEvent> call, Response<CalendarEvent> response) {
                listener.onEventSuccessfullyAdded(response.body());
            }

            @Override
            public void onFailure(Call<CalendarEvent> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void loadAllSubjects(final OnNetworkRequestFinished listener) {
        Call<List<SubjectListItem>> call = new RestClient().getApiService().getAllSubjectNames(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<SubjectListItem>>() {
            @Override
            public void onResponse(Call<List<SubjectListItem>> call, Response<List<SubjectListItem>> response) {
                ArrayList<SubjectListItem> loadedSubjects = new ArrayList<>(response.body().size());
                loadedSubjects.addAll(response.body());

                listener.onSubjectsSuccessfullyLoaded(loadedSubjects);
            }

            @Override
            public void onFailure(Call<List<SubjectListItem>> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

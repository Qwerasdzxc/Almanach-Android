package com.code_dream.almanach.calendar.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.DeleteCalendarEventRequest;
import com.code_dream.almanach.network.requests.EditCalendarEventRequest;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.utility.OfflineDatabase;
import com.code_dream.almanach.utility.Registry;

import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.filters.ObjectFilters;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/23/17.
 */

public class CalendarInteractor implements ICalendarInteractor {

    @Override
    public void loadAllCalendarEvents(final OnCalendarRequestFinishedListener listener) {
        Call<List<CalendarEvent>> call = new RestClient().getApiService().getCalendarEvents(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, Response<List<CalendarEvent>> response) {
                ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
                calendarEvents.addAll(response.body());

                if (!calendarEvents.isEmpty()) {
                    if (OfflineDatabase.instance.calendarEventsRepo.size() > 0)
                        OfflineDatabase.instance.calendarEventsRepo.remove(ObjectFilters.ALL);

                    OfflineDatabase.instance.calendarEventsRepo.insert(response.body().toArray(new CalendarEvent[response.body().size()]));

                    listener.onEventsSuccessfullyLoaded(calendarEvents, true);
                }
                else
                    listener.onEventsSuccessfullyLoaded(new ArrayList<CalendarEvent>(), true);
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                Cursor<CalendarEvent> cursor = OfflineDatabase.instance.calendarEventsRepo.find(ObjectFilters.ALL);
                ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

                calendarEvents.addAll(cursor.toList());
                listener.onEventsSuccessfullyLoaded(calendarEvents, false);
            }
        });
    }

    @Override
    public void editEvent(final OnCalendarRequestFinishedListener listener, final CalendarEvent calendarEvent) {
        Call<ResponseBody> call = new RestClient().getApiService().editCalendarEvent(new EditCalendarEventRequest(calendarEvent.getSubject(), calendarEvent.getDate(),
                                                                                                                  calendarEvent.getEventType(), calendarEvent.getTitle(),
                                                                                                                  calendarEvent.getDescription(), calendarEvent.getEventId(),
                                                                                                                  calendarEvent.getGrade()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                OfflineDatabase.instance.calendarEventsRepo.update(ObjectFilters.eq("eventId", calendarEvent.getEventId()), calendarEvent);

                listener.onEventSuccessfullyEdited();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void deleteEvent(final OnCalendarRequestFinishedListener listener, final CalendarEvent calendarEvent) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteCalendarEvent(new DeleteCalendarEventRequest(calendarEvent.getEventId()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                OfflineDatabase.instance.calendarEventsRepo.remove(ObjectFilters.eq("eventId", calendarEvent.getEventId()));

                listener.onEventSuccessfullyDeleted(calendarEvent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

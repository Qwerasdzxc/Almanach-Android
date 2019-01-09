package com.code_dream.almanach.event_profile.network;

import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.network.ResponseHandler;
import com.code_dream.almanach.network.ResponseHandlerCallback;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.DeleteCalendarEventRequest;
import com.code_dream.almanach.network.requests.EditCalendarEventRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public class EventProfileInteractor implements IEventProfileInteractor {

    private EventProfileNetworkCallback listener;

    public EventProfileInteractor(EventProfileNetworkCallback listener) {
        this.listener = listener;
    }

    @Override
    public void saveEventChanges(CalendarEvent calendarEvent, final boolean finishActivity) {
        Call<ResponseBody> call = new RestClient().getApiService().editCalendarEvent(new EditCalendarEventRequest(calendarEvent.getSubject(), calendarEvent.getDate(), calendarEvent.getEventType(), calendarEvent.getTitle(), calendarEvent.getDescription(), calendarEvent.getEventId(), calendarEvent.getGrade()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                ResponseHandler.getInstance().getResponse(response, new ResponseHandlerCallback() {
                    @Override
                    public void onSuccess(Response originalResponse) {
                        listener.onEventSuccessfullySaved(finishActivity);
                    }

                    @Override
                    public void onServerFailure() {
                        listener.onServerError();
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    @Override
    public void deleteEvent(CalendarEvent calendarEvent) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteCalendarEvent(new DeleteCalendarEventRequest(calendarEvent.getEventId()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onEventSuccessfullyDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

package com.code_dream.almanach.subject_profile.network;

import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.network.ResponseHandler;
import com.code_dream.almanach.network.ResponseHandlerCallback;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.CalendarEventsForSubjectRequest;
import com.code_dream.almanach.network.requests.DeleteSubjectRequest;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 6/21/17.
 */

public class SubjectProfileInteractor implements ISubjectProfileInteractor {

    private final OnNetworkRequestFinished listener;

    public SubjectProfileInteractor(OnNetworkRequestFinished listener) {
        this.listener = listener;
    }

    @Override
    public void loadAllCalendarEvents(String subjectName) {
        Call<List<CalendarEvent>> call = new RestClient().getApiService().getCalendarEventsForSubject(new CalendarEventsForSubjectRequest(subjectName));

        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, final Response<List<CalendarEvent>> response) {
                ResponseHandler.getInstance().getResponse(response, new ResponseHandlerCallback() {
                    @Override
                    public void onSuccess(Response originalResponse) {
                        ArrayList<CalendarEvent> loadedCalendarEvents = new ArrayList<>(response.body().size());
                        loadedCalendarEvents.addAll(response.body());

                        listener.onCalendarEventsSuccessfullyLoaded(loadedCalendarEvents);
                    }

                    @Override
                    public void onServerFailure() {
                        listener.onServerError();
                    }
                });

            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    @Override
    public void deleteSubject(String subjectName) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteSubject(new DeleteSubjectRequest(subjectName));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseHandler.getInstance().getResponse(response, new ResponseHandlerCallback() {
                    @Override
                    public void onSuccess(Response originalResponse) {
                        listener.onSubjectSuccessfullyDeleted();
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
}

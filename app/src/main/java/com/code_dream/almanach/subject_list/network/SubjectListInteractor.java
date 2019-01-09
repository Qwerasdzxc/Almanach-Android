package com.code_dream.almanach.subject_list.network;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.DeleteSubjectRequest;
import com.code_dream.almanach.network.requests.NewSubjectRequest;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.utility.OfflineDatabase;
import com.code_dream.almanach.utility.Registry;

import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.filters.ObjectFilters;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public class SubjectListInteractor implements ISubjectListInteractor {

    private OnNetworkRequestFinished listener;

    public SubjectListInteractor(OnNetworkRequestFinished listener) {
        this.listener = listener;
    }

    @Override
    public void loadAllSubjects() {
        Call<List<SubjectListItem>> call = new RestClient().getApiService().getAllSubjects(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<SubjectListItem>>() {
            @Override
            public void onResponse(Call<List<SubjectListItem>> call, Response<List<SubjectListItem>> response) {
                ArrayList<SubjectListItem> loadedSubjects = new ArrayList<>(response.body().size());
                loadedSubjects.addAll(response.body());

                if (OfflineDatabase.instance.subjectsRepo.size() > 0)
                    OfflineDatabase.instance.subjectsRepo.remove(ObjectFilters.ALL);

                OfflineDatabase.instance.subjectsRepo.insert(response.body().toArray(new SubjectListItem[response.body().size()]));

                listener.onSubjectsSuccessfullyLoaded(loadedSubjects, true);
            }

            @Override
            public void onFailure(Call<List<SubjectListItem>> call, Throwable t) {
                Cursor<SubjectListItem> cursor = OfflineDatabase.instance.subjectsRepo.find(ObjectFilters.ALL);
                ArrayList<SubjectListItem> subjectItems = new ArrayList<>();

                subjectItems.addAll(cursor.toList());
                listener.onSubjectsSuccessfullyLoaded(subjectItems, false);
            }
        });
    }

    @Override
    public void deleteSubject(final SubjectListItem subjectToDelete) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteSubject(new DeleteSubjectRequest(subjectToDelete.getSubjectName()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                OfflineDatabase.instance.subjectsRepo.remove(ObjectFilters.eq("subjectName", subjectToDelete.getSubjectName()));

                listener.onSubjectSuccessfullyDeleted(subjectToDelete);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void addNewSubject(final String subjectName) {
        Call<ResponseBody> call = new RestClient().getApiService().addSubject(new NewSubjectRequest(subjectName));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SubjectListItem addedSubject = new SubjectListItem(subjectName, EnumSet.noneOf(EventType.class), Grades.GRADE_NONE);
                OfflineDatabase.instance.subjectsRepo.insert(addedSubject);

                listener.onSubjectSuccessfullyAdded(addedSubject);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

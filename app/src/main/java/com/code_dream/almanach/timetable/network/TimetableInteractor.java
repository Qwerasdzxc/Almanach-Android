package com.code_dream.almanach.timetable.network;

import android.util.Log;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.models.TimetableItem;
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
 * Created by Qwerasdzxc on 6/2/17.
 */

public class TimetableInteractor implements ITimetableInteractor {

    @Override
    public void loadItems(final OnTimetableRequestFinished listener) {
        Call<List<TimetableItem>> call = new RestClient().getApiService().loadTimetable(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<TimetableItem>>() {
            @Override
            public void onResponse(Call<List<TimetableItem>> call, Response<List<TimetableItem>> response) {

                Log.e("onResponse", "code: " + response.code());

                // If there was an error with the server connection, load the Timetable from the database.
                if (response.code() != 200) {
                    Cursor<TimetableItem> cursor = OfflineDatabase.instance.timetableItemRepo.find(ObjectFilters.ALL);
                    ArrayList<TimetableItem> timetableItems = new ArrayList<>();

                    timetableItems.addAll(cursor.toList());
                    listener.onItemsSuccessfullyLoaded(timetableItems, false);

                    return;
                }

                ArrayList<TimetableItem> timetableItems = new ArrayList<>();
                timetableItems.addAll(response.body());

                if (OfflineDatabase.instance.timetableItemRepo.size() > 0)
                    OfflineDatabase.instance.timetableItemRepo.remove(ObjectFilters.ALL);

                OfflineDatabase.instance.timetableItemRepo.insert(response.body().toArray(new TimetableItem[response.body().size()]));

                listener.onItemsSuccessfullyLoaded(timetableItems, true);
            }

            @Override
            public void onFailure(Call<List<TimetableItem>> call, Throwable t) {
                Log.e("onResponse", "failed: " + t.getMessage());
                Cursor<TimetableItem> cursor = OfflineDatabase.instance.timetableItemRepo.find(ObjectFilters.ALL);
                ArrayList<TimetableItem> timetableItems = new ArrayList<>();

                timetableItems.addAll(cursor.toList());
                listener.onItemsSuccessfullyLoaded(timetableItems, false);
            }
        });
    }

    @Override
    public void uploadItems(final ArrayList<TimetableItem> timetableItems, final OnTimetableRequestFinished listener) {
        Call<ResponseBody> call = new RestClient().getApiService().uploadTimetable(timetableItems);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (OfflineDatabase.instance.timetableItemRepo.size() > 0)
                    OfflineDatabase.instance.timetableItemRepo.remove(ObjectFilters.ALL);

                OfflineDatabase.instance.timetableItemRepo.insert(timetableItems.toArray(new TimetableItem[timetableItems.size()]));

                listener.onTimetableSuccessfullyUploaded();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

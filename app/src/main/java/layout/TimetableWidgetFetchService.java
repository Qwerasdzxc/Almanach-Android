package layout;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.code_dream.almanach.R;
import com.code_dream.almanach.models.TimetableItem;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.timetable.models.TimetableListItem;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 9/13/17.
 */

public class TimetableWidgetFetchService extends Service {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        getDataFromServer();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getDataFromServer() {
        Call<List<TimetableItem>> call = new RestClient().getApiService().loadTimetableForToday(Registry.NO_DATA_TO_SEND);
        call.enqueue(new Callback<List<TimetableItem>>() {
            @Override
            public void onResponse(Call<List<TimetableItem>> call, Response<List<TimetableItem>> response) {
                Log.e("onResponse", "RECEIVED RESPONSE CODE: " + response.code());
                List<TimetableItem> timetableItems = new ArrayList<>();

                timetableItems.addAll(response.body());
                processData(timetableItems);
            }

            @Override
            public void onFailure(Call<List<TimetableItem>> call, Throwable t) {
                Log.e("onFailure", "NO DATA FROM SERVER: " + t.getMessage());
            }
        });
    }

    private void processData(List<TimetableItem> receivedData) {
        TimetableListItem[] timetableItems = new TimetableListItem[receivedData.size()];

        // Populate the Timetable items by the order which they have been saved.
        for (int i = 0; i < timetableItems.length; i++) {
            for (TimetableItem item : receivedData) {
                if (item.getOrder() == i) {
                    timetableItems[i] = new TimetableListItem(item.getName(), R.drawable.ic_subject_1, item);
                }
            }
        }

//        if (OfflineDatabase.instance.timetableListItemRepo.size() > 0)
//            OfflineDatabase.instance.timetableListItemRepo.remove(ObjectFilters.ALL);
//
//        OfflineDatabase.instance.timetableListItemRepo.insert(timetableItems);

        populateWidget();
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(TimetableWidgetProvider.DATA_FETCHED);
        widgetUpdateIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(widgetUpdateIntent);

        Log.e("populateWidget", "broadcast sent");

        this.stopSelf();
    }
}
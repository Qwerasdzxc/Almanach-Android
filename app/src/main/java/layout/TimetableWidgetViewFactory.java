package layout;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.code_dream.almanach.R;
import com.code_dream.almanach.timetable.models.TimetableListItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 9/12/17.
 */

public class TimetableWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public static ArrayList<TimetableListItem> items = new ArrayList<>();
    private int appWidgetId;
    private Context context;

    public TimetableWidgetViewFactory(Context context, Intent intent) {
        Log.e("ViewFactory", "Constructor called");
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItems();

//        //Some random data to display
//        for (int i = 0; i < 10; i++) {
//            TimetableListItem item = new TimetableListItem("Item text", R.drawable.ic_subject_1, new TimetableItem("Srpski", TimetableItem.Day.MONDAY, 1));
//
//            items.add(item);
//        }
    }

    public static ArrayList<TimetableListItem> getItems() {
        if (items == null){
            items = new ArrayList<>();

            items.addAll(getItemsFromDatabase());
        }

        return items;
    }

    public static ArrayList<TimetableListItem> getItemsFromDatabase() {
//        ArrayList<TimetableListItem> data = new ArrayList<>();
//
//        Cursor<TimetableListItem> cursor = OfflineDatabase.instance.timetableListItemRepo.find(ObjectFilters.ALL);
//        TimetableListItem[] timetableItems = new TimetableListItem[cursor.size()];
//
//        // Populate the Timetable items by the order which they have been saved.
//        for (int i = 0; i < timetableItems.length; i++) {
//            for (TimetableListItem item : cursor.toList()) {
//                if (item.getTimetableItem().getOrder() == i) {
//                    timetableItems[i] = item;
//                    break;
//                }
//            }
//        }
//
//        data.addAll(Arrays.asList(timetableItems));
//
//        return data;

        //TODO: Restore old code
        return null;
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return getItems().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        TimetableListItem item = getItems().get(position);


        RemoteViews itemView = new RemoteViews(context.getPackageName(), R.layout.list_item_timetable_widget_subject);
        //itemView.removeAllViews(R.id.subject_timetable_body);

        itemView.setTextViewText(R.id.subject_timetable_widget_item_tv, item.getTimetableItem().getName());

        return itemView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        Log.w("onDataSetChanged", "Called");
        populateListItems();
    }

    private void populateListItems() {
        getItems().clear();
        items.addAll(getItemsFromDatabase());
    }

}
package com.code_dream.almanach.timetable.network;

import com.code_dream.almanach.models.TimetableItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/2/17.
 */

public interface OnTimetableRequestFinished {

    void onItemsSuccessfullyLoaded(ArrayList<TimetableItem> loadedTimetableItems, boolean loadedFromServer);

    void onTimetableSuccessfullyUploaded();

    void onFailure();
}

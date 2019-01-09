package com.code_dream.almanach.timetable.network;

import com.code_dream.almanach.models.TimetableItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/2/17.
 */

public interface ITimetableInteractor {

    void loadItems(OnTimetableRequestFinished listener);

    void uploadItems(ArrayList<TimetableItem> timetableItems, OnTimetableRequestFinished listener);
}

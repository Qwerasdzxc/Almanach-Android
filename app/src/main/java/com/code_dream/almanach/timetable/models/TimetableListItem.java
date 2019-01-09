package com.code_dream.almanach.timetable.models;

import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.TimetableItem;

/**
 * Created by Peter on 6/11/2017.
 */

public class TimetableListItem extends GenericListItem {

    private TimetableItem timetableItem;

    public TimetableListItem(String itemText, int imageResource, TimetableItem timetableItem) {
        super(itemText, imageResource);

        this.timetableItem = timetableItem;
    }

    public TimetableItem getTimetableItem() {
        return timetableItem;
    }

    public void setTimetableItem(TimetableItem timetableItem) {
        this.timetableItem = timetableItem;
    }
}

package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Qwerasdzxc on 6/21/17.
 */

public class SubjectCategoryItem implements Parcelable {

    private String title;

    private CalendarEvent calendarEvent;

    public SubjectCategoryItem(String title, CalendarEvent calendarEvent){
        this.title = title;
        this.calendarEvent = calendarEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }

    public void setCalendarEvent(CalendarEvent calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

    protected SubjectCategoryItem(Parcel in) {
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubjectCategoryItem> CREATOR = new Creator<SubjectCategoryItem>() {
        @Override
        public SubjectCategoryItem createFromParcel(Parcel in) {
            return new SubjectCategoryItem(in);
        }

        @Override
        public SubjectCategoryItem[] newArray(int size) {
            return new SubjectCategoryItem[size];
        }
    };
}

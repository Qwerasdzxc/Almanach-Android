package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.code_dream.almanach.R;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;

/**
 * Created by Qwerasdzxc on 2/23/17.
 */

// Nitrite Database indices
@Indices({
        @Index(value = "subject", type = IndexType.NonUnique),
        @Index(value = "title", type = IndexType.NonUnique),
        @Index(value = "description", type = IndexType.NonUnique),
        @Index(value = "date", type = IndexType.NonUnique),
        @Index(value = "eventType", type = IndexType.NonUnique),
        @Index(value = "grade", type = IndexType.NonUnique),

})
public class CalendarEvent implements Parcelable, Serializable {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private EventType eventType;
    @Id
    @SerializedName("id")
    @Expose
    private int eventId;
    @SerializedName("grade")
    @Expose
    private Grades grade;

    private CalendarDay calendarDay;

    public CalendarEvent() {
        // Empty constructor needed for Nitrite
    }

    public CalendarEvent(String subject, String title, String description, String date, EventType eventType, int eventId, CalendarDay calendarDay) {
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.date = date;
        this.eventType = eventType;
        this.eventId = eventId;
        this.calendarDay = calendarDay;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }

    public void setCalendarDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
    }

    public int getDay() {
        return Integer.parseInt(date.split("/")[0]);
    }

    public int getMonth() {
        return Integer.parseInt(date.split("/")[1]);
    }

    public int getYear() {
        return Integer.parseInt(date.split("/")[2]);
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType(){
        return eventType;
    }

    public void setGrade(Grades grade) {
        this.grade = grade;
    }

    public Grades getGrade() {
        return grade;
    }

    public int getImageResource() {
        switch (getEventType()){
            case TEST:
                return R.drawable.ic_test;
            case HOME_WORK:
                return R.drawable.ic_homework;
            case PRESENTATION:
                return R.drawable.ic_presentation;
            case OTHER_ASSIGNMENT:
                return R.drawable.ic_other_assignment;
            default:
                return 0;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getSubject());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeString(getDate());
        dest.writeString(getEventType().name());
        dest.writeInt(getEventId());
        dest.writeString(getGrade().toString());
        dest.writeParcelable(calendarDay, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private CalendarEvent(Parcel in){
        setSubject(in.readString());
        setTitle(in.readString());
        setDescription(in.readString());
        setDate(in.readString());
        setEventType(EventType.valueOf(in.readString()));
        setEventId(in.readInt());
        setGrade(Grades.valueOf(in.readString()));

        calendarDay = in.readParcelable(CalendarDay.class.getClassLoader());
    }

    public static final Parcelable.Creator<CalendarEvent> CREATOR = new Parcelable.Creator<CalendarEvent>() {

        @Override
        public CalendarEvent createFromParcel(Parcel in) {
            return new CalendarEvent(in);
        }

        @Override
        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };
}

package com.code_dream.almanach.network.requests;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;

/**
 * Created by Qwerasdzxc on 2/25/17.
 */

public class EditCalendarEventRequest {

    private String subject;
    private String date;
    private EventType eventType;
    private String title;
    private String description;
    private int eventId;
    private Grades grade;

    public EditCalendarEventRequest(String subject, String date, EventType eventType, String title, String description, int eventId, Grades grade){
        this.subject = subject;
        this.date = date;
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.eventId = eventId;
        this.grade = grade;
    }

    public String getSubject(){
        return subject;
    }

    public String getDate() {
        return date;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getEventId(){
        return eventId;
    }

    public Grades getGrade() {
        return grade;
    }
}

package com.code_dream.almanach.network.requests;

import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;

/**
 * Created by Qwerasdzxc on 2/22/17.
 */

public class CreateCalendarEventRequest {

    private String subject;
    private String date;
    private Grades grade;
    private EventType eventType;
    private String title;
    private String description;

    public CreateCalendarEventRequest(String subject, String date, Grades grade, EventType eventType, String title, String description){
        this.subject = subject;
        this.date = date;
        this.grade = grade;
        this.eventType = eventType;
        this.title = title;
        this.description = description;
    }

    public String getSubject(){
        return subject;
    }

    public String getDate() {
        return date;
    }

    public Grades getGrade() {
        return grade;
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
}

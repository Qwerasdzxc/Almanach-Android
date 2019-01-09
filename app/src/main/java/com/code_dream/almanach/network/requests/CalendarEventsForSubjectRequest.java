package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 6/25/17.
 */

public class CalendarEventsForSubjectRequest {

    private String subjectName;

    public CalendarEventsForSubjectRequest(String subjectName){
        this.subjectName = subjectName;
    }

    public String getSubjectName(){
        return subjectName;
    }
}

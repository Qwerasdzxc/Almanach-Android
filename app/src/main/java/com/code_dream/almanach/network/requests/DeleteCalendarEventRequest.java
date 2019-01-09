package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/25/17.
 */

public class DeleteCalendarEventRequest {

    private int eventId;

    public DeleteCalendarEventRequest(int eventId){
        this.eventId = eventId;
    }

    public int getEventId(){
        return eventId;
    }
}

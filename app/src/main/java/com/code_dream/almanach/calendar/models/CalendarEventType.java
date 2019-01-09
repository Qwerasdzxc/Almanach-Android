package com.code_dream.almanach.calendar.models;

/**
 * Created by Qwerasdzxc on 26-Dec-16.
 */

public class CalendarEventType {

	private String eventTypeName;
	private int eventTypeDrawable;

	public CalendarEventType(String eventTypeName, int eventTypeDrawable) {
		this.setEventTypeName(eventTypeName);
		this.setEventTypeDrawable(eventTypeDrawable);
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public int getEventTypeDrawable() {
		return eventTypeDrawable;
	}

	public void setEventTypeDrawable(int eventTypeDrawable) {
		this.eventTypeDrawable = eventTypeDrawable;
	}

	public int getImageResource() {
		return eventTypeDrawable;
	}
}

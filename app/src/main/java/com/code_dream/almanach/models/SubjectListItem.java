package com.code_dream.almanach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.util.EnumSet;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

// Nitrite Database indices
@Indices({
        @Index(value = "subjectName", type = IndexType.Unique),
        @Index(value = "eventTypesSerialized", type = IndexType.NonUnique),
        @Index(value = "grade", type = IndexType.NonUnique),
})
public class SubjectListItem {

    @Id
    @SerializedName("subject_name")
    @Expose
    private String subjectName;

    @SerializedName("event_types")
    @Expose
    private EnumSet<EventType> eventTypes;

    @SerializedName("grade")
    @Expose
    private Grades grade;

    private String eventTypesSerialized;

    public SubjectListItem() {
        // Empty constructor needed for Nitrite
    }

    public SubjectListItem(String subjectName, EnumSet<EventType> eventTypes, Grades grade) {
        this.subjectName = subjectName;
        this.eventTypes = eventTypes;
        this.grade = grade;

        this.eventTypesSerialized = enumSetToString(eventTypes);
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setEventTypes(EnumSet<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public EnumSet<EventType> getEventTypes() {
        if (eventTypes == null)
            return stringToEnumSet(eventTypesSerialized);
        else
            return eventTypes;
    }

    public Grades getGrade() {
        return grade;
    }

    public void setGrade(Grades grade) {
        this.grade = grade;
    }

    private String enumSetToString(EnumSet<EventType> eventTypes) {
        StringBuilder sb = new StringBuilder();
        for (Enum event : eventTypes) {
            sb.append(event).append("|");
        }

        return sb.toString();
    }

    private EnumSet<EventType> stringToEnumSet(String eventTypesSerialized) {
        EnumSet<EventType> eventTypes = EnumSet.noneOf(EventType.class);
        String[] eventTypesStringArr = eventTypesSerialized.split("\\|");

        for (String eventType : eventTypesStringArr)
            eventTypes.add(Enum.valueOf(EventType.class, eventType));

        return eventTypes;
    }
}

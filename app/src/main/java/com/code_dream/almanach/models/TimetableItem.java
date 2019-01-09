package com.code_dream.almanach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;

/**
 * Created by Qwerasdzxc on 6/2/17.
 */

// Nitrite Database indices
@Indices({
        @Index(value = "name", type = IndexType.NonUnique),
        @Index(value = "day", type = IndexType.NonUnique),
        @Index(value = "order", type = IndexType.NonUnique),
})

public class TimetableItem implements Serializable {

    // Id counter required by Nitrite for database key
    private static int idCount;

    @Id
    private int id;

    public enum Day {
        @SerializedName("monday")
        MONDAY,
        @SerializedName("tuesday")
        TUESDAY,
        @SerializedName("wednesday")
        WEDNESDAY,
        @SerializedName("thursday")
        THURSDAY,
        @SerializedName("friday")
        FRIDAY,
        @SerializedName("saturday")
        SATURDAY,
        @SerializedName("sunday")
        SUNDAY,
        @SerializedName("all_subjects")
        ALL_SUBJECTS
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("day")
    @Expose
    private Day day;

    @SerializedName("order")
    @Expose
    private int order;

    @SerializedName("subject")
    @Expose
    private Subject subject;

    // Used for backup (if the user cancel's the Timetable modification)
    // Transient keyword prevents it's serialization
    private transient int originalOrder;

    // Default constructor for Nitrite database
    public TimetableItem() {
        this.id = idCount;
        idCount++;
    }

    public TimetableItem(String name, Day day, int order){
        this.name = name;
        this.day = day;
        this.order = order;

        this.id = idCount;
        idCount++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(int originalOrder) {
        this.originalOrder = originalOrder;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}

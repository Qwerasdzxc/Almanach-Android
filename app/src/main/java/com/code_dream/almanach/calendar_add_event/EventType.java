package com.code_dream.almanach.calendar_add_event;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 2/22/17.
 */

public enum EventType {

    @SerializedName("test")
    TEST,
    @SerializedName("homework")
    HOME_WORK,
    @SerializedName("presentation")
    PRESENTATION,
    @SerializedName("other_assignment")
    OTHER_ASSIGNMENT
}

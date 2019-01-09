package com.code_dream.almanach.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class UpdatePreferencesResponse {

    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("full_name")
    @Expose
    private String fullName;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

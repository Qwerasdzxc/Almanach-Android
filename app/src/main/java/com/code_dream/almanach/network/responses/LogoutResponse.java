package com.code_dream.almanach.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 2/20/17.
 */

public class LogoutResponse {

    @SerializedName("response")
    @Expose
    private Integer logoutResponseCode;

    public Integer getLogoutResponseCode() {
        return logoutResponseCode;
    }

    public void setLogoutResponseCode(Integer logoutResponseCode) {
        this.logoutResponseCode = logoutResponseCode;
    }
}

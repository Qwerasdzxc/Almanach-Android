package com.code_dream.almanach.notifications_tab.network;

import com.code_dream.almanach.models.Notification;

import java.util.List;

/**
 * Created by Qwerasdzxc on 11/15/17.
 */

public interface OnNetworkRequestFinished {

    void onNotificationsLoaded(List<Notification> notifications);
}

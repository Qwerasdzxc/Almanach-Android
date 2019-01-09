package com.code_dream.almanach.notifications_tab;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.Notification;

import java.util.List;

/**
 * Created by Qwerasdzxc on 11/15/17.
 */

public interface NotificationsTabContract {

    @UiThread
    interface View {

        void updateList(List<Notification> notifications);

        void setSwipeRefreshing(boolean enabled);
    }

    interface UsersActionListener {

        void getNotifications();
    }
}

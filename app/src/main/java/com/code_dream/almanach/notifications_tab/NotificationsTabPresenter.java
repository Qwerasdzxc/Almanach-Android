package com.code_dream.almanach.notifications_tab;

import com.code_dream.almanach.models.Notification;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.notifications_tab.network.NotificationsTabInteractor;
import com.code_dream.almanach.notifications_tab.network.OnNetworkRequestFinished;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qwerasdzxc on 11/15/17.
 */

public class NotificationsTabPresenter implements NotificationsTabContract.UsersActionListener, OnNetworkRequestFinished {

    private NotificationsTabContract.View view;
    private NotificationsTabInteractor interactor;

    public NotificationsTabPresenter(NotificationsTabContract.View view) {
        this.view = view;
        interactor = new NotificationsTabInteractor(this);
    }

    @Override
    public void getNotifications() {
        interactor.loadNotifications();
    }

    @Override
    public void onNotificationsLoaded(List<Notification> notifications) {
        view.setSwipeRefreshing(false);
        view.updateList(notifications);
    }
}

package com.code_dream.almanach.event_profile.network;


import com.code_dream.almanach.network.OnNetworkRequestFinished;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public interface EventProfileNetworkCallback extends OnNetworkRequestFinished {

    void onEventSuccessfullySaved(boolean finishActivity);

    void onEventSuccessfullyDeleted();

}

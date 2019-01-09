package com.code_dream.almanach.network;

import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public interface ResponseHandlerCallback {

    void onSuccess(Response originalResponse);

    void onServerFailure();
}

package com.code_dream.almanach.network;

import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 6/29/17.
 */

public class ResponseHandler {

    public static ResponseHandler instance;

    protected ResponseHandler() {

    }

    public static ResponseHandler getInstance() {
        if (instance == null)
            instance = new ResponseHandler();

        return instance;
    }

    public void getResponse(Response originalResponse, ResponseHandlerCallback callback) {
        if (originalResponse.isSuccessful())
            callback.onSuccess(originalResponse);
        else
            callback.onServerFailure();
    }
}

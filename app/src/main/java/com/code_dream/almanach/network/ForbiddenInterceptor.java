package com.code_dream.almanach.network;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Qwerasdzxc on 5/25/17.
 */

public class ForbiddenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

//        if (response.code() == 403) {
//
//        }
        return response;
    }
}
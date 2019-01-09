package com.code_dream.almanach.network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.code_dream.almanach.SPApplication;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Qwerasdzxc on 2/13/17.
 */

public class RestClient {

    private APIService apiService;

    public RestClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Setting up OkHttp interceptor to intercept the original request
        // and add the Authorization header
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // In case the UserInfo gets thrown out of memory and we're in the middle of the app,
                // we try to pull the Token from device storage for future app usage.
                if (UserInfo.TOKEN == null || UserInfo.TOKEN.isEmpty()) {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SPApplication.getInstance().context);
                    UserInfo.TOKEN = sharedPrefs.getString(Registry.SHARED_PREFS_TOKEN_KEY, Registry.EMPTY_TOKEN);
                }

                // Request customization: Request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "JWT " + UserInfo.TOKEN);

                // Header added to the request and now
                // it's continuing its original path
                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });

        httpClient.interceptors().add(new ForbiddenInterceptor());

        // Setting up OkHttp client with 1 minute timeouts
        OkHttpClient client = httpClient
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        // Setting up Retrofit client with Gson and OkHttp client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Registry.BASE_SERVER_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Saving the new Retrofit instance for later usage
        apiService = retrofit.create(APIService.class);
    }

    // Get the newly created Retrofit client
    public APIService getApiService(){
        return apiService;
    }
}



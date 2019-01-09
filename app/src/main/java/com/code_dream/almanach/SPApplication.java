package com.code_dream.almanach;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.code_dream.almanach.dagger.component.AppComponent;
import com.code_dream.almanach.dagger.component.DaggerAppComponent;
import com.code_dream.almanach.dagger.module.AppModule;
import com.code_dream.almanach.network.services.MessageService;
import com.code_dream.almanach.utility.OfflineDatabase;

/**
 * Created by Qwerasdzxc on 2/12/17.
 */

public class SPApplication extends MultiDexApplication {

    private static SPApplication instance;

    public Context context;

    AppComponent appComponent;

    public static SPApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();

        // Starting the Nitrite database.
        new OfflineDatabase(getFilesDir().getPath(), "OfflineDb");

        // Starting Dagger.
        appComponent = DaggerAppComponent.builder()
                                         .appModule(new AppModule(this))
                                         .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}

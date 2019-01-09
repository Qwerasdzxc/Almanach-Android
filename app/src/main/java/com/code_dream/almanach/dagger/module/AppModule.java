package com.code_dream.almanach.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.code_dream.almanach.SPApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Qwerasdzxc on 2/12/17.
 */

@Module
public class AppModule {

    private final SPApplication application;

    public AppModule(SPApplication application){
        this.application = application;
    }

    @Provides @Singleton
    Context providesApplicationContext(){
        return application;
    }

    @Provides @Singleton
    SharedPreferences provideSharedPreferences(Context app){
        return PreferenceManager.getDefaultSharedPreferences(app);
    }
}

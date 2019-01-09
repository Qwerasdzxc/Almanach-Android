package com.code_dream.almanach.dagger.component;

import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.SPApplication;
import com.code_dream.almanach.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Qwerasdzxc on 2/12/17.
 */

@Singleton @Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(SPApplication application);
    void inject(BaseActivity baseActivity);
}

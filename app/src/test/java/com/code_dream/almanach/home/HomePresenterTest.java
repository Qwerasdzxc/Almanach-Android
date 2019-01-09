package com.code_dream.almanach.home;

import com.code_dream.almanach.home.network.HomeInteractor;
import com.code_dream.almanach.home.network.OnHomeRequestFinishedListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Qwerasdzxc on 3/3/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class HomePresenterTest {

    @Mock
    HomeContract.View view;

    @Mock
    HomeInteractor interactor;

    @Captor
    ArgumentCaptor<OnHomeRequestFinishedListener> listenerArgumentCaptor;

    HomePresenter presenter;

    @Before
    public void setUp(){
        presenter = new HomePresenter(view, interactor);
    }

    @Test public void shouldLogoutSuccessfully(){
        presenter.logout();

        verify(view).showProgressDialog();

        verify(interactor, times(1)).logoutUser(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onLogoutSuccessful();

        verify(view).dismissProgressDialog();
        verify(view).openLoginActivity();
    }

    @Test public void shouldLogoutWithFailure(){
        presenter.logout();

        verify(view).showProgressDialog();

        verify(interactor, times(1)).logoutUser(listenerArgumentCaptor.capture());
        listenerArgumentCaptor.getValue().onFailure();

        verify(view).dismissProgressDialog();
        verify(view).showLogoutErrorToast();
    }

    @Test public void shouldOpenSettings(){
        presenter.openSettings();

        verify(view).settings();
    }

    @Test public void shouldSearchOnClick(){
        presenter.onSearchViewClick();

        verify(view).search();
    }
}

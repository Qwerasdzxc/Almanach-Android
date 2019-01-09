package com.code_dream.almanach.intro;

import com.code_dream.almanach.intro.network.IntroInteractor;
import com.code_dream.almanach.intro.network.OnRegisterFinishedListener;
import com.code_dream.almanach.intro.slides.register.SlideRegister;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Qwerasdzxc on 3/3/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class IntroPresenterTest {

    @Mock
    IntroContract.View view;

    @Mock
    IntroInteractor interactor;

    @Mock
    SlideRegister slideRegister;

    @Captor
    ArgumentCaptor<OnRegisterFinishedListener> argumentCaptor;

    IntroPresenter presenter;

    @Before
    public void setUp(){
        presenter = new IntroPresenter(view, interactor);
    }

    @Test public void shouldStartHomeActivityOnSuccessfullRegistration(){
        String fullName = "Mile Kitic";
        String dob = "01/01/2001";
        String email = "milekitic@gmail.com";
        String password = "password123";
        String allSubjects = "Srpski\nMatematika\nBios";

        String receivedToken = "jopdwapkoawd[okdaw[okdfaeiopjjpo21391893j;pklfasj;kdawpoi";

        when(view.getSlideRegister()).thenReturn(slideRegister);
        when(view.getSlideRegister().canRegister()).thenReturn(true);
        when(view.getFullName()).thenReturn(fullName);
        when(view.getDob()).thenReturn(dob);
        when(view.getEmail()).thenReturn(email);
        when(view.getPassword()).thenReturn(password);
        when(view.getAllSubjects()).thenReturn(allSubjects);

        presenter.onFinishClick();

        verify(view).onRegistrationStart();

        when(view.getSavedFirebaseToken()).thenReturn("");

        // TODO: Mockito throwing an error because we're trying to write in the Registry class.
        verify(interactor, times(1)).sendRegisterRequest(argumentCaptor.capture(), eq(fullName), eq(dob), eq(email), eq(password), eq(allSubjects));
        argumentCaptor.getValue().onSuccess(receivedToken);

        verify(view).startHomeActivity();
    }
}

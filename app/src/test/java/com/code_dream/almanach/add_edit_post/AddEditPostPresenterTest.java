package com.code_dream.almanach.add_edit_post;

import com.code_dream.almanach.add_edit_post.network.AddEditPostInteractor;
import com.code_dream.almanach.add_edit_post.network.OnAddEditPostFinishedListener;
import com.code_dream.almanach.models.SchoolPost;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Captor;
import org.mockito.Mock;

import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Qwerasdzxc on 3/1/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class AddEditPostPresenterTest {

    @Mock
    AddEditPostInteractor interactor;

    @Mock
    AddEditPostContract.View view;

    @Captor
    ArgumentCaptor<OnAddEditPostFinishedListener> listenerArgumentCaptor;

    AddEditPostPresenter presenter;

    @Before
    public void setUp() {
        presenter = new AddEditPostPresenter(view, interactor);
    }

    @Test public void shouldInitializeEditActivity(){
        when(view.getPostType()).thenReturn(PostType.EDIT);

        presenter.onInitialize();

        verify(view).initializeEditActivity();
    }

    @Test public void shouldFinishActivityWhenCreatingNewPost(){
        String contentText = "Some String longer than 6 characters";
        SchoolPost schoolPost = new SchoolPost("23/08/2017", contentText, "Preletacevic Beli", "Gimnazija Mladenovac", 69, 100, false, false, false);

        when(view.getContent()).thenReturn(contentText);
        when(view.getPostType()).thenReturn(PostType.NEW);

        presenter.onPostClick();

        verify(interactor, times(1)).createPost(listenerArgumentCaptor.capture(), eq(contentText));

        listenerArgumentCaptor.getValue().onPostSuccessfullyCreated(schoolPost);

        verify(view).finishActivity(schoolPost);
    }

    @Test public void shouldFinishActivityWhenEditingExistingPost(){
        String contentText = "Some String longer than 6 characters";
        int postId = 8;
        SchoolPost schoolPost = new SchoolPost("23/08/2017", contentText, "Preletacevic Beli", "Gimnazija Mladenovac", postId, 100, false, false, false);

        when(view.getContent()).thenReturn(contentText);
        when(view.getPostType()).thenReturn(PostType.EDIT);
        when(view.getOriginalPostId()).thenReturn(postId);

        presenter.onPostClick();

        verify(interactor, times(1)).editPost(listenerArgumentCaptor.capture(), eq(postId), eq(contentText));

        listenerArgumentCaptor.getValue().onPostSuccessfullyEdited(schoolPost);

        verify(view).finishActivity(schoolPost);
    }

    @Test public void shouldShowInvalidContentError(){
        String contentText = "Short";

        when(view.getContent()).thenReturn(contentText);

        presenter.onPostClick();

        verify(view).showInvalidContentError();
    }


}

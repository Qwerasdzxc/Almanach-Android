package com.code_dream.almanach.add_edit_post;

import com.code_dream.almanach.add_edit_post.network.AddEditPostInteractor;
import com.code_dream.almanach.add_edit_post.network.OnAddEditPostFinishedListener;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.Utility;

import okhttp3.MediaType;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public class AddEditPostPresenter implements AddEditPostContract.UserActionsListener,
                                             OnAddEditPostFinishedListener {

    private AddEditPostContract.View view;
    private AddEditPostInteractor addEditPostInteractor;

    public AddEditPostPresenter(AddEditPostContract.View view, AddEditPostInteractor interactor)
    {
        this.view = view;
        this.addEditPostInteractor = interactor;
    }

    @Override
    public void onInitialize() {
        if (view.getPostType() == PostType.EDIT)
            view.initializeEditActivity();
        else if (view.getImageUri() != null)
            view.initializeNewPostActivity();
    }

    @Override
    public void onPostClick() {
        if (view.getContent().length() >= Registry.ADD_EDIT_POST_MIN_CONTENT_LENGTH) {
            view.showLoadingToast();
            view.setPostButtonClickable(false);

            if (view.getPostType() == PostType.NEW)
                if (view.getImageUri() != null)
                    addEditPostInteractor.createPost(this, view.getContent(), Utility.getBitmapByUri(view.getImageUri(), view.getContentResolver()), view.getImageUri().getPath(), MediaType.parse(view.getContentResolver().getType(view.getImageUri())));
                else
                    addEditPostInteractor.createPost(this, view.getContent());
            else
                addEditPostInteractor.editPost(this, view.getOriginalPostId(), view.getContent());
        }
        else
            view.showInvalidContentError();
    }

    @Override
    public void onPostSuccessfullyCreated(SchoolPost schoolPost) {
        view.dismissLoadToastWithSuccess();
        view.finishActivity(schoolPost);
    }

    @Override
    public void onPostSuccessfullyEdited(SchoolPost schoolPost) {
        view.dismissLoadToastWithSuccess();
        view.finishActivity(schoolPost);
    }

    @Override
    public void onRetryConnection() {

    }

    @Override
    public void onServerError() {
        view.dismissLoadToastWithError();
        view.setPostButtonClickable(true);
    }

    @Override
    public void onInternetConnectionProblem() {
        view.dismissLoadToastWithError();
        view.setPostButtonClickable(true);

        view.showInternetConnectionProblemSnackbar();
    }
}

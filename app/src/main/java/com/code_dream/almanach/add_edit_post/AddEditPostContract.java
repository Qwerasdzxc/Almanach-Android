package com.code_dream.almanach.add_edit_post;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.UiThread;

import com.code_dream.almanach.models.SchoolPost;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface AddEditPostContract {

    @UiThread
    interface View {

        void finishActivity(SchoolPost schoolPost);

        void initializeNewPostActivity();

        void initializeEditActivity();

        void showInvalidContentError();

        String getContent();

        Uri getImageUri();

        PostType getPostType();

        ContentResolver getContentResolver();

        int getOriginalPostId();

        void showLoadingToast();

        void dismissLoadToastWithSuccess();

        void dismissLoadToastWithError();

        void setPostButtonClickable(boolean clickable);

        void showInternetConnectionProblemSnackbar();
    }

    interface UserActionsListener {

        void onInitialize();

        void onPostClick();

        void onRetryConnection();
    }
}

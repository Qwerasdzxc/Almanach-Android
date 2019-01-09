package com.code_dream.almanach.add_edit_post.network;

import android.graphics.Bitmap;

import java.io.IOException;

import okhttp3.MediaType;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface IAddEditPostInteractor {

    void createPost(OnAddEditPostFinishedListener listener, String content);

    void createPost(OnAddEditPostFinishedListener listener, String content, Bitmap image, String imagePath, MediaType imageMediaType) throws IOException;

    void editPost(OnAddEditPostFinishedListener listener, int postId, String content);
}

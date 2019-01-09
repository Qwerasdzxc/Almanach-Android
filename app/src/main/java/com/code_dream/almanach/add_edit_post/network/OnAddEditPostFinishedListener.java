package com.code_dream.almanach.add_edit_post.network;

import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.network.OnNetworkRequestFinished;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public interface OnAddEditPostFinishedListener extends OnNetworkRequestFinished {

    void onPostSuccessfullyCreated(SchoolPost schoolPost);

    void onPostSuccessfullyEdited(SchoolPost schoolPost);
}

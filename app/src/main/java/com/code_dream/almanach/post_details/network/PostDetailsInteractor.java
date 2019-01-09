package com.code_dream.almanach.post_details.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.CreateCommentRequest;
import com.code_dream.almanach.network.requests.DeletePostRequest;
import com.code_dream.almanach.network.requests.EditPostRequest;
import com.code_dream.almanach.network.requests.PostReactionRequest;
import com.code_dream.almanach.network.requests.ReportCommentRequest;
import com.code_dream.almanach.network.requests.ReportPostRequest;
import com.code_dream.almanach.network.requests.SchoolPostCommentsRequest;
import com.code_dream.almanach.news_feed.ReactionType;
import com.code_dream.almanach.post_details.SchoolPostComment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 3/14/17.
 */

public class PostDetailsInteractor implements IPostDetailsInteractor {

    @Override
    public void loadMoreComments(final OnNetworkRequestFinished listener, final int postId, final int lastCommentId) {
        Call<List<SchoolPostComment>> call = new RestClient().getApiService().getSchoolPostComments(new SchoolPostCommentsRequest(postId, lastCommentId));

        call.enqueue(new Callback<List<SchoolPostComment>>() {
            @Override
            public void onResponse(Call<List<SchoolPostComment>> call, Response<List<SchoolPostComment>> response) {
                ArrayList<SchoolPostComment> loadedSchoolPostComments = new ArrayList<>(response.body().size());
                loadedSchoolPostComments.addAll(response.body());

                if (loadedSchoolPostComments.size() > 0)
                    listener.onCommentsSuccessfullyLoaded(loadedSchoolPostComments);
                else
                    listener.onNoMoreCommentsToLoad();
            }

            @Override
            public void onFailure(Call<List<SchoolPostComment>> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void upvotePost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().upvotePost(new PostReactionRequest(postId, ReactionType.LIKE));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyUpvoted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void downvotePost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().downvotePost(new PostReactionRequest(postId, ReactionType.DISLIKE));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyDownvoted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void upvoteComment(final OnNetworkRequestFinished listener, int commentId) {
        Call<ResponseBody> call = new RestClient().getApiService().upvoteComment(new PostReactionRequest(commentId, ReactionType.LIKE));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onCommentSuccessfullyUpvoted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void downvoteComment(final OnNetworkRequestFinished listener, int commentId) {
        Call<ResponseBody> call = new RestClient().getApiService().downvoteComment(new PostReactionRequest(commentId, ReactionType.DISLIKE));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onCommentSuccessfullyDownvoted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void reportComment(final OnNetworkRequestFinished listener, int commentId) {
        Call<ResponseBody> call = new RestClient().getApiService().reportComment(new ReportCommentRequest(commentId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onCommentSuccessfullyReported();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void addComment(final OnNetworkRequestFinished listener, int postId, String content) {
        Call<SchoolPostComment> call = new RestClient().getApiService().createComment(new CreateCommentRequest(postId, content));

        call.enqueue(new Callback<SchoolPostComment>() {
            @Override
            public void onResponse(Call<SchoolPostComment> call, Response<SchoolPostComment> response) {
                listener.onCommentSuccessfullyCreated(response.body());
            }

            @Override
            public void onFailure(Call<SchoolPostComment> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void deleteComment(final OnNetworkRequestFinished listener, int commentId) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteComment(new DeletePostRequest(commentId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onCommentSuccessfullyDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void editComment(final OnNetworkRequestFinished listener, int commentId, String content) {
        Call<SchoolPostComment> call = new RestClient().getApiService().editComment(new EditPostRequest(commentId, content));

        call.enqueue(new Callback<SchoolPostComment>() {
            @Override
            public void onResponse(Call<SchoolPostComment> call, Response<SchoolPostComment> response) {
                listener.onCommentSuccessfullyEdited(response.body());
            }

            @Override
            public void onFailure(Call<SchoolPostComment> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

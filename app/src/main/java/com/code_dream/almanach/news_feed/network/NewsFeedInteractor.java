package com.code_dream.almanach.news_feed.network;

import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.DeletePostRequest;
import com.code_dream.almanach.network.requests.SchoolPostsRequest;
import com.code_dream.almanach.network.requests.PostReactionRequest;
import com.code_dream.almanach.network.requests.ReportPostRequest;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.ReactionType;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/2/17.
 */

public class NewsFeedInteractor implements INewsFeedInteractor{

    @Override
    public void loadMorePosts(final boolean refreshAll, final OnNetworkRequestFinished listener, int lastPostId) {
        Call<List<SchoolPost>> call = new RestClient().getApiService().getSchoolPosts(new SchoolPostsRequest(lastPostId));

        call.enqueue(new Callback<List<SchoolPost>>() {
            @Override
            public void onResponse(Call<List<SchoolPost>> call, Response<List<SchoolPost>> response) {
                ArrayList<Object> schoolPosts = new ArrayList<>();
                schoolPosts.addAll(response.body());

                if (schoolPosts.isEmpty()) {
                    if (refreshAll)
                        listener.onMorePostsSuccessfullyLoaded(new ArrayList<>(), true);
                    else
                        listener.onNoMorePostsToLoad();
                }
                else
                    listener.onMorePostsSuccessfullyLoaded(schoolPosts, refreshAll);
            }

            @Override
            public void onFailure(Call<List<SchoolPost>> call, Throwable t) {
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
    public void reportPost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().reportPost(new ReportPostRequest(postId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyReported();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void deletePost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().deletePost(new DeletePostRequest(postId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}

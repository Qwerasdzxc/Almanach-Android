package com.code_dream.almanach.person_profile.network;

import android.util.Log;

import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.DeletePostRequest;
import com.code_dream.almanach.network.requests.PersonRequest;
import com.code_dream.almanach.network.requests.PostReactionRequest;
import com.code_dream.almanach.network.requests.ReportPostRequest;
import com.code_dream.almanach.network.requests.SchoolPostsForPersonRequest;
import com.code_dream.almanach.network.requests.SchoolPostsRequest;
import com.code_dream.almanach.news_feed.ReactionType;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 9/21/17.
 */

public class PersonProfileInteractor {

    public void loadPersonProfile(int id, final OnNetworkRequestFinished listener) {
        Call<Person> call = new RestClient().getApiService().getPerson(new PersonRequest(id));

        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                listener.onPersonSuccessfullyReceived(response.body());
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void addFriend(int id, final OnNetworkRequestFinished listener) {
        Call<ResponseBody> call = new RestClient().getApiService().sendFriendRequest(new PersonRequest(id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onFriendRequestSent();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void deleteFriend(int id, final OnNetworkRequestFinished listener) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteFriend(new PersonRequest(id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onFriendDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void acceptFriend(int id, final OnNetworkRequestFinished listener) {
        Call<ResponseBody> call = new RestClient().getApiService().acceptFriendRequest(new PersonRequest(id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onFriendRequestAccepted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void createChatRoom(int id, final OnNetworkRequestFinished listener) {
        Call<ChatRoom> call = new RestClient().getApiService().createChatRoom(new PersonRequest(id));

        call.enqueue(new Callback<ChatRoom>() {
            @Override
            public void onResponse(Call<ChatRoom> call, Response<ChatRoom> response) {
                listener.onChatRoomSuccessfullyCreated(response.body());
            }

            @Override
            public void onFailure(Call<ChatRoom> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void loadMorePosts(final boolean refreshAll, final OnNetworkRequestFinished listener, int personId, int lastPostId) {
        Call<List<SchoolPost>> call = new RestClient().getApiService().getSchoolPostsForPerson(new SchoolPostsForPersonRequest(personId, lastPostId));

        call.enqueue(new Callback<List<SchoolPost>>() {
            @Override
            public void onResponse(Call<List<SchoolPost>> call, Response<List<SchoolPost>> response) {
                if (response.body().isEmpty()) {
                    if (refreshAll)
                        listener.onMorePostsSuccessfullyLoaded(new ArrayList<SchoolPost>(), true);
                    else
                        listener.onNoMorePostsToLoad();
                }
                else
                    listener.onMorePostsSuccessfullyLoaded(response.body(), refreshAll);
            }

            @Override
            public void onFailure(Call<List<SchoolPost>> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void upvotePost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().upvotePost(new PostReactionRequest(postId, ReactionType.LIKE));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyUpvoted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void downvotePost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().downvotePost(new PostReactionRequest(postId, ReactionType.DISLIKE));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyDownvoted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void reportPost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().reportPost(new ReportPostRequest(postId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyReported();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void deletePost(final OnNetworkRequestFinished listener, int postId) {
        Call<ResponseBody> call = new RestClient().getApiService().deletePost(new DeletePostRequest(postId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listener.onPostSuccessfullyDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

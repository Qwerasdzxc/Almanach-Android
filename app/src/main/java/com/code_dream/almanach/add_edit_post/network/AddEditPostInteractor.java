package com.code_dream.almanach.add_edit_post.network;

import android.graphics.Bitmap;
import android.os.Environment;

import com.code_dream.almanach.network.ResponseHandler;
import com.code_dream.almanach.network.ResponseHandlerCallback;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.CreatePostRequest;
import com.code_dream.almanach.network.requests.EditPostRequest;
import com.code_dream.almanach.models.SchoolPost;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public class AddEditPostInteractor implements IAddEditPostInteractor {
    @Override
    public void createPost(final OnAddEditPostFinishedListener listener, String content) {
        Call<SchoolPost> call = new RestClient().getApiService().createPost(new CreatePostRequest(content));

        call.enqueue(new Callback<SchoolPost>() {
            @Override
            public void onResponse(Call<SchoolPost> call, final Response<SchoolPost> response) {
                ResponseHandler.getInstance().getResponse(response, new ResponseHandlerCallback() {
                    @Override
                    public void onSuccess(Response originalResponse) {
                        listener.onPostSuccessfullyCreated(response.body());
                    }

                    @Override
                    public void onServerFailure() {
                        listener.onServerError();
                    }
                });

            }

            @Override
            public void onFailure(Call<SchoolPost> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    @Override
    public void createPost(final OnAddEditPostFinishedListener listener, String content, Bitmap bitmap, String imagePath, MediaType imageMediaType){
        File f3 = new File(Environment.getExternalStorageDirectory() + "/inpaint/");

        if(!f3.exists())
            f3.mkdirs();

        final File file = new File(Environment.getExternalStorageDirectory() + "/inpaint/temp." + imageMediaType.subtype());

        OutputStream outStream;

        // Image compression
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);

            outStream.flush();
            outStream.close();
        } catch (IOException exception){
            exception.printStackTrace();
        }

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(imageMediaType, file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture_part", file.getName(), requestFile);

        Call<SchoolPost> call = new RestClient().getApiService().createPost(new CreatePostRequest(content), body);

        call.enqueue(new Callback<SchoolPost>() {
            @Override
            public void onResponse(Call<SchoolPost> call, Response<SchoolPost> response) {
                listener.onPostSuccessfullyCreated(response.body());

                file.delete();
            }

            @Override
            public void onFailure(Call<SchoolPost> call, Throwable t) {
                listener.onInternetConnectionProblem();

                file.delete();
            }
        });
    }

    @Override
    public void editPost(final OnAddEditPostFinishedListener listener, int postId, String content) {
        Call<SchoolPost> call = new RestClient().getApiService().editPost(new EditPostRequest(postId, content));

        call.enqueue(new Callback<SchoolPost>() {
            @Override
            public void onResponse(Call<SchoolPost> call, Response<SchoolPost> response) {
                listener.onPostSuccessfullyEdited(response.body());
            }

            @Override
            public void onFailure(Call<SchoolPost> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

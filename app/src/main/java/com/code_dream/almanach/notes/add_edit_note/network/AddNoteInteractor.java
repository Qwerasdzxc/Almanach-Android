package com.code_dream.almanach.notes.add_edit_note.network;

import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.EditNoteRequest;
import com.code_dream.almanach.network.requests.NewNoteRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public class AddNoteInteractor {

    public void saveNote(Note note, final OnNetworkRequestFinished listener) {
        Call<Note> call = new RestClient().getApiService().addNote(new NewNoteRequest(note.getTitle(), note.getDescription()));

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                listener.onNoteSuccessfullyAdded(response.body());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }

    public void editNote(Note note, final OnNetworkRequestFinished listener) {
        Call<Note> call = new RestClient().getApiService().editNote(new EditNoteRequest(note.getId(), note.getTitle(), note.getDescription()));

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                listener.onNoteSuccessfullyEdited(response.body());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

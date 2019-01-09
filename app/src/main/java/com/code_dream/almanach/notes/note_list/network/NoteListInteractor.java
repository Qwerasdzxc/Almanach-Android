package com.code_dream.almanach.notes.note_list.network;

import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.network.RestClient;
import com.code_dream.almanach.network.requests.DeleteNoteRequest;
import com.code_dream.almanach.network.requests.EditNoteRequest;
import com.code_dream.almanach.utility.OfflineDatabase;
import com.code_dream.almanach.utility.Registry;

import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.filters.ObjectFilters;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public class NoteListInteractor {

    public void loadNotes(final OnNetworkRequestFinished listener) {
        Call<List<Note>> call = new RestClient().getApiService().getAllNotes(Registry.NO_DATA_TO_SEND);

        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (OfflineDatabase.instance.notesRepo.size() > 0)
                    OfflineDatabase.instance.notesRepo.remove(ObjectFilters.ALL);

                OfflineDatabase.instance.notesRepo.insert(response.body().toArray(new Note[response.body().size()]));

                listener.onNotesLoaded(response.body(), true);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Cursor<Note> cursor = OfflineDatabase.instance.notesRepo.find(ObjectFilters.ALL);
                ArrayList<Note> noteItems = new ArrayList<>();

                noteItems.addAll(cursor.toList());
                listener.onNotesLoaded(noteItems, false);
            }
        });
    }

    public void deleteNote(final Note note, final  OnNetworkRequestFinished listener) {
        Call<ResponseBody> call = new RestClient().getApiService().deleteNote(new DeleteNoteRequest(note.getId()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                OfflineDatabase.instance.notesRepo.remove(ObjectFilters.eq("id", note.getId()));

                listener.onNoteDeleted();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onInternetConnectionProblem();
            }
        });
    }
}

package com.code_dream.almanach.notes.note_list.network;

import com.code_dream.almanach.models.Note;

import java.util.List;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onNotesLoaded(List<Note> loadedNotes, boolean loadedFromServer);

    void onNoteDeleted();

}

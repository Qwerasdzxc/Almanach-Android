package com.code_dream.almanach.notes.add_edit_note.network;

import com.code_dream.almanach.models.Note;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onNoteSuccessfullyAdded(Note note);

    void onNoteSuccessfullyEdited(Note editedNote);
}

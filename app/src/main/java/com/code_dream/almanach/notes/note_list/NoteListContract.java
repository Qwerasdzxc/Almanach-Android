package com.code_dream.almanach.notes.note_list;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.Note;

import java.util.List;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public interface NoteListContract {

    @UiThread
    interface View {

        void showNotes(List<Note> notes);

        void deleteNote(int noteIndex);

        void showProgressBar(boolean show);

        void setSwipeRefreshLayoutRefreshing(boolean refreshing);

        void openEditNoteActivity(Note note);
    }

    interface UserActionsListener {

        void loadNotes();

        void editNote(Note note);

        void deleteNote(Note note, int noteIndex);
    }
}

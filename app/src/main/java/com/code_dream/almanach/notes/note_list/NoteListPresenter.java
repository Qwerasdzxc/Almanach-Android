package com.code_dream.almanach.notes.note_list;

import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.notes.note_list.network.NoteListInteractor;
import com.code_dream.almanach.notes.note_list.network.OnNetworkRequestFinished;

import java.util.List;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public class NoteListPresenter implements NoteListContract.UserActionsListener, OnNetworkRequestFinished {

    NoteListContract.View view;
    NoteListInteractor interactor;

    // Note currently in the process of deletion.
    private int deletingNoteIndex;

    // Offline or Online mode?
    public boolean onlineMode;

    public NoteListPresenter(NoteListContract.View view) {
        this.view = view;
        this.interactor = new NoteListInteractor();
    }

    @Override
    public void loadNotes() {
        view.showProgressBar(true);

        interactor.loadNotes(this);
    }

    @Override
    public void editNote(Note note) {
        view.openEditNoteActivity(note);
    }

    @Override
    public void deleteNote(Note note, int noteIndex) {
        deletingNoteIndex = noteIndex;

        interactor.deleteNote(note, this);
    }

    @Override
    public void onNotesLoaded(List<Note> loadedNotes, boolean loadedFromServer) {
        view.showNotes(loadedNotes);
        view.setSwipeRefreshLayoutRefreshing(false);
        view.showProgressBar(false);

        this.onlineMode = loadedFromServer;
    }

    @Override
    public void onNoteDeleted() {
        view.deleteNote(deletingNoteIndex);
    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {

    }
}

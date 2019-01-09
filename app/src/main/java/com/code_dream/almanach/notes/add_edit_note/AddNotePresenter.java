package com.code_dream.almanach.notes.add_edit_note;

import android.util.Log;

import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.notes.add_edit_note.network.AddNoteInteractor;
import com.code_dream.almanach.notes.add_edit_note.network.OnNetworkRequestFinished;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public class AddNotePresenter implements AddNoteContract.UserActionsListener, OnNetworkRequestFinished {

    AddNoteContract.View view;
    AddNoteInteractor interactor;

    // Variables holding the Note's original data.
    private String oldNoteTitle;
    private String oldNoteDescription;

    public AddNotePresenter(AddNoteContract.View view) {
        this.view = view;
        this.interactor = new AddNoteInteractor();
    }

    @Override
    public void saveOldNoteData(String title, String description) {
        oldNoteTitle = title;
        oldNoteDescription = description;
    }

    @Override
    public void onTitleTextChanged(String newText) {
        view.enableSaveButton(hasChanges(newText));
    }

    @Override
    public void onDescriptionTextChanged(String newText) {
        // Only enable the save button if we're editing an already
        // existing note and if the user changed the description text.
        view.enableSaveButton((view.isEditingNote() && !oldNoteDescription.equals(view.getDescriptionText())) || (!view.isEditingNote() && !newText.isEmpty()));
    }

    @Override
    public void saveNote() {
        view.showLoadingToast("Saving note...");

        if (!view.isEditingNote())
            interactor.saveNote(new Note(view.getTitleText(), view.getDescriptionText(), null), this);
        else
            interactor.editNote(new Note(view.getTitleText(), view.getDescriptionText(), null, view.getEditingNoteId()), this);
    }

    @Override
    public void onReturnToNotesListActivity() {
        if (hasChanges(view.getTitleText()))
            view.showConfirmationDialog();
        else
            view.finishActivity(null);
    }

    @Override
    public void onNoteSuccessfullyAdded(Note note) {
        view.dismissLoadingToastSuccessfully();
        view.finishActivity(note);
    }

    @Override
    public void onNoteSuccessfullyEdited(Note editedNote) {
        view.dismissLoadingToastSuccessfully();
        view.finishActivity(editedNote);
    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onInternetConnectionProblem() {

    }

    private boolean hasChanges(String titleText) {
        return (!view.isEditingNote() && !titleText.isEmpty()) || (view.isEditingNote() && ((!oldNoteTitle.equals(view.getTitleText())) || !oldNoteDescription.equals(view.getDescriptionText())));
    }
}

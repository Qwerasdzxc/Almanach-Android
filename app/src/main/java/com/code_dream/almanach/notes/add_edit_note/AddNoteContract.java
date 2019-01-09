package com.code_dream.almanach.notes.add_edit_note;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.Note;


/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public interface AddNoteContract {

    @UiThread
    interface View {

        void enableSaveButton(boolean enable);

        void showLoadingToast(String text);

        void dismissLoadingToastSuccessfully();

        void dismissLoadingToastWithError();

        void finishActivity(Note createdNote);

        void showConfirmationDialog();

        boolean isEditingNote();

        String getTitleText();

        String getDescriptionText();

        int getEditingNoteId();
    }

    interface UserActionsListener {

        void saveOldNoteData(String title, String description);

        void onTitleTextChanged(String newText);

        void onDescriptionTextChanged(String newText);

        void saveNote();

        void onReturnToNotesListActivity();
    }
}

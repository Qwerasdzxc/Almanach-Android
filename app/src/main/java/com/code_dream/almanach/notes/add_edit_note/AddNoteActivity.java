package com.code_dream.almanach.notes.add_edit_note;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.utility.Registry;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import net.steamcrafted.loadtoast.LoadToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNoteActivity extends BaseActivity implements AddNoteContract.View {

    @BindView(R.id.add_note_toolbar) Toolbar toolbar;
    @BindView(R.id.add_note_title) EditText title;
    @BindView(R.id.add_note_content) EditText content;
    @BindView(R.id.add_note_save_button) TextView saveButton;

    @Nullable
    @InjectExtra("note")
    Note editingNote;

    LoadToast loadToast;
    AddNotePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_note);
        setupView();
    }

    @Override
    public void setupPresenter() {
        presenter = new AddNotePresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadToast = new LoadToast(this);

        // If we're editing an old note, then we update the layout with it's data.
        if (editingNote != null) {
            title.setText(editingNote.getTitle());
            content.setText(editingNote.getDescription());

            // Save the old data in the Presenter.
            presenter.saveOldNoteData(editingNote.getTitle(), editingNote.getDescription());
        }

        // Notifying the presenter when title text changes.
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onTitleTextChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onDescriptionTextChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @OnClick(R.id.add_note_save_button)
    public void onSaveClick() {
        presenter.saveNote();
    }

    @Override
    public void onBackPressed() {
        presenter.onReturnToNotesListActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            presenter.onReturnToNotesListActivity();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enableSaveButton(boolean enable) {
        if (enable) {
            saveButton.setClickable(true);
            saveButton.setTextColor(getResources().getColor(R.color.colorWhiteText));
        } else {
            saveButton.setClickable(false);
            saveButton.setTextColor(getResources().getColor(R.color.colorWhiteTransparentText));
        }
    }

    @Override
    public String getTitleText() {
        return title.getText().toString();
    }

    @Override
    public String getDescriptionText() {
        return content.getText().toString();
    }

    @Override
    public int getEditingNoteId() {
        return editingNote.getId();
    }

    @Override
    public void showConfirmationDialog() {
        new MaterialDialog.Builder(this)
                .title("Cancel all changes?")
                .content("Are you sure you want to cancel all changes made to this note?")
                .neutralText("Yes")
                .negativeText("No")
                .positiveText("Save changes")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finishActivity(null);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.saveNote();
                    }
                })
                .show();
    }

    @Override
    public void showLoadingToast(String text) {
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.setText(text);
        loadToast.show();
    }

    @Override
    public void dismissLoadingToastSuccessfully() {
        loadToast.success();
    }

    @Override
    public void dismissLoadingToastWithError() {
        loadToast.error();
    }

    @Override
    public void finishActivity(Note createdNote) {
        if (createdNote != null)
            setResult(RESULT_OK, new Intent().putExtra("note", (Parcelable) createdNote));
        else
            setResult(RESULT_CANCELED, new Intent());

        this.finish();
    }

    @Override
    public boolean isEditingNote() {
        return editingNote != null;
    }
}

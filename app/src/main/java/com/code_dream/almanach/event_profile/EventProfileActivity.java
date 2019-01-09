package com.code_dream.almanach.event_profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.network.InternetConnectionRetryCallback;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.Utility;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventProfileActivity extends BaseActivity implements EventProfileContract.View,
                                                                  View.OnClickListener {

    @BindView(R.id.event_profile_toolbar) Toolbar toolbar;
    @BindView(R.id.event_profile_save_button) TextView eventSave;
    @BindView(R.id.event_profile_parent_layout) FrameLayout parentLayout;
    @BindView(R.id.event_profile_title) EditText eventTitle;
    @BindView(R.id.event_profile_subject_name) TextView subjectTitle;
    @BindView(R.id.event_profile_date) TextView eventDate;
    @BindView(R.id.event_profile_date_edit) ImageView eventDateEdit;
    @BindView(R.id.event_profile_note) EditText eventNote;

    @BindViews({R.id.event_profile_grade_A,
                R.id.event_profile_grade_B,
                R.id.event_profile_grade_C,
                R.id.event_profile_grade_D,
                R.id.event_profile_grade_F}) List<ImageView> grades;

    @InjectExtra("selected_event")
    CalendarEvent selectedCalendarEvent;

    private EventProfilePresenter presenter;

    private LoadToast loadToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_profile);
        setupView();

        presenter.setOriginallySelectedGrade(getOriginalCalendarEvent().getGrade());
        presenter.highlightEventGrade();

        this.enableSaveButton(false);
    }

    @Override
    public void setupPresenter() {
        presenter = new EventProfilePresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        loadToast = new LoadToast(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectTitle.setText(selectedCalendarEvent.getSubject());
        eventTitle.setText(selectedCalendarEvent.getTitle());
        eventDate.setText(selectedCalendarEvent.getDate());
        eventNote.setText(selectedCalendarEvent.getDescription());

        for (ImageView image : grades) {
            image.setOnClickListener(this);
        }

        // Notifying the presenter when event title and description text change.
        eventNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onDescriptionTextChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        eventTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onTitleTextChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @Override
    public void onClick(View view) {
        presenter.onGradeSelected(Utility.getGradeByImageView(view));
    }

    @OnClick(R.id.event_profile_delete_event)
    public void onDeleteClick() {
        new MaterialDialog.Builder(this)
                .title("Delete event?")
                .content("Are you sure you want to delete this calendar event?")
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.onDeleteClicked();
                    }
                })
                .show();
    }

    @OnClick(R.id.event_profile_save_button)
    public void onSaveClick(){
        presenter.saveEventChanges(false);
    }

    @OnClick({ R.id.event_profile_date, R.id.event_profile_date_edit })
    public void onDateClick(){
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String newDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                eventDate.setText(newDate);
                presenter.onDateChanged(newDate);
            }
        }, selectedCalendarEvent.getYear(), selectedCalendarEvent.getMonth() - 1, selectedCalendarEvent.getDay()).show();
    }

    @Override
    public void enableSaveButton(boolean enable) {
        if (enable) {
            eventSave.setClickable(true);
            eventSave.setTextColor(getResources().getColor(R.color.colorWhiteText));
        } else {
            eventSave.setClickable(false);
            eventSave.setTextColor(getResources().getColor(R.color.colorWhiteTransparentText));
        }
    }

    @Override
    public void setImageViewSelected(int imageViewID) {
        ImageView imageView = findViewById(imageViewID);

        for (ImageView image : grades) {
            if (imageView.getId() == image.getId())
                image.setColorFilter(ContextCompat.getColor(context, R.color.transparent));
            else
                image.setColorFilter(ContextCompat.getColor(context, R.color.colorWhiteTransparentText));
        }
    }

    @Override
    public void setImageViewDeselected(int imageViewID) {
        ImageView imageView = findViewById(imageViewID);

        imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorWhiteTransparentText));
    }

    @Override
    public void onBackPressed() {
        presenter.onReturnToSubjectActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            presenter.onReturnToSubjectActivity();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishActivity(boolean eventDeleted) {
        if (!eventDeleted)
            setResult(RESULT_OK, new Intent().putExtra("subject_name", selectedCalendarEvent.getSubject()).putExtra("changed_event", (Parcelable) selectedCalendarEvent));
        else
            setResult(RESULT_OK, new Intent().putExtra("subject_name", selectedCalendarEvent.getSubject()).putExtra("deleted_event", (Parcelable) selectedCalendarEvent));

        this.finish();
    }

    @Override
    public void showLoadingToast() {
        loadToast.setText("Saving event...");
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
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
    public void displaySaveConfirmationDialog() {
        new MaterialDialog.Builder(this)
                .title("Cancel all changes?")
                .content("Are you sure you want to cancel all changes made to the event?")
                .neutralText("Yes")
                .negativeText("No")
                .positiveText("Save changes")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finishActivity(false);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.saveEventChanges(true);
                    }
                })
                .show();
    }

    @Override
    public void showInternetConnectionProblemSnackbar() {
        this.showInternetConnectionErrorSnackbar(new InternetConnectionRetryCallback() {
            @Override
            public void onRetry() {
                presenter.onRetryInternetConnection();
            }
        });
    }

    @Override
    public CalendarEvent getOriginalCalendarEvent() {
        return selectedCalendarEvent;
    }

    @Override
    public String getEventDate() {
        return eventDate.getText().toString();
    }

    @Override
    public String getEventDescription() {
        return eventNote.getText().toString();
    }

    @Override
    public String getEventTitle() {
        return eventTitle.getText().toString();
    }
}

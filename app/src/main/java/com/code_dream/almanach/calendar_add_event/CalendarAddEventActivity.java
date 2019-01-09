package com.code_dream.almanach.calendar_add_event;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.utility.Utility;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.calendar_add_event.network.CalendarAddEventInteractor;
import com.code_dream.almanach.utility.AlertReceiver;
import com.code_dream.almanach.utility.Registry;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarAddEventActivity extends BaseActivity implements CalendarAddEventContract.View,
                                                                      View.OnClickListener {

    @BindView(R.id.add_post_activity_toolbar) Toolbar toolbar;
    @BindView(R.id.add_event_title) EditText eventTitleEditText;
    @BindView(R.id.add_event_body) EditText eventBodyEditText;
    @BindView(R.id.add_event_date) TextView dateEditText;
    @BindView(R.id.add_event_date_selection_tv) TextView dateSelectionText;
    @BindView(R.id.add_event_subject_spinner) Spinner subjectSpinner;
    @BindView(R.id.add_event_subject_name) TextView subjectNameText;
    @BindView(R.id.add_event_time_selection_spinner) Spinner timeSelectionSpinner;
    @BindView(R.id.add_event_notification_checkbox) CheckBox notificationsCheckbox;
    @BindView(R.id.add_event_notification_area_layout) LinearLayout notificationsAreaLayout;
    @BindView(R.id.add_event_parent_layout) FrameLayout parentLayout;
    @BindView(R.id.add_event_date_selection_parent) LinearLayout dateSelectionParentView;
    @BindView(R.id.add_event_type_selection_parent) CardView eventTypeSelectionParentView;
    @BindView(R.id.add_event_loading_bar) ProgressBar progressBar;

    @BindViews({R.id.add_event_type_test,
            R.id.add_event_type_homework,
            R.id.add_event_type_presentation,
            R.id.add_event_type_other_assignment}) List<ImageView> eventTypes;

    @BindViews({R.id.add_event_grade_A,
            R.id.add_event_grade_B,
            R.id.add_event_grade_C,
            R.id.add_event_grade_D,
            R.id.add_event_grade_F}) List<ImageView> grades;

    @Nullable
    @InjectExtra("selected_subject")
    String selectedSubjectName;

    @Nullable
    @InjectExtra("selected_event_type")
    EventType selectedEventType;

    @Nullable
    @InjectExtra("selected_date")
    CalendarDay selectedCalendarDay;

    private LoadToast loadToast;

    private CalendarAddEventPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_add_event);
        setupView();

        presenter.loadSubjects();
    }

    @Override
    public void setupPresenter() {
        presenter = new CalendarAddEventPresenter(this, new CalendarAddEventInteractor());
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadToast = new LoadToast(this);

        // Difference in creation mode. If we enter from the Calendar view, then we already have a Date and an Event type.
        // According to that, we make the necessary layout changes.
        if (selectedCalendarDay != null) {
            ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_time_selection_string_array, android.R.layout.simple_spinner_item);

            spinnerTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSelectionSpinner.setAdapter(spinnerTimeAdapter);

            String dateText = selectedCalendarDay.getDay() + "/" + (selectedCalendarDay.getMonth() + 1) + "/" + selectedCalendarDay.getYear();
            dateEditText.setText(dateText);
        }
        // User entered from the Subject profile view, so some Event details are missing and need to be filled.
        else {
            dateSelectionParentView.setVisibility(View.VISIBLE);
            dateEditText.setVisibility(View.GONE);

            subjectNameText.setVisibility(View.VISIBLE);
            subjectSpinner.setVisibility(View.GONE);
            subjectNameText.setText(selectedSubjectName);

            // Set the current date as a default Event date.
            String newDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
            dateSelectionText.setText(newDate);

            eventTypeSelectionParentView.setVisibility(View.VISIBLE);

            for (ImageView image : eventTypes) {
                image.setOnClickListener(this);
            }
        }

        for (ImageView image : grades) {
            image.setOnClickListener(this);
        }

        notificationsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    notificationsAreaLayout.setVisibility(View.VISIBLE);
                else
                    notificationsAreaLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        for (ImageView imageView : eventTypes)
            if (view.getId() == imageView.getId()) {
                presenter.onEventTypeSelected(Utility.getEventTypeByImageView(view));
                return;
            }

            presenter.onGradeSelected(Utility.getGradeByImageView(view));
    }

    @OnClick(R.id.add_event_button)
    public void onAddEventClick(){
        presenter.onAddEventClick();
    }

    @OnClick({ R.id.add_event_date_selection_img, R.id.add_event_date_selection_tv })
    public void onDateSelectionClick() {
        Calendar c = Calendar.getInstance();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String newDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                dateSelectionText.setText(newDate);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public String getSelectedSubject() {
        return selectedSubjectName != null ? selectedSubjectName : subjectSpinner.getSelectedItem().toString();
    }

    @Override
    public String getSelectedDate() {
        if (getSelectedCalendarDay() != null)
            return getSelectedCalendarDay().getDay() + "/" + (getSelectedCalendarDay().getMonth() + 1) + "/" + getSelectedCalendarDay().getYear();
        else
            return dateSelectionText.getText().toString();
    }

    @Nullable
    @Override
    public CalendarDay getSelectedCalendarDay() {
        return selectedCalendarDay;
    }

    @Override
    public int getNotificationDelayInHours() {
        switch (timeSelectionSpinner.getSelectedItemPosition()){
            case 0:
                return 4;
            case 1:
                return 8;
            case 2:
                return 12;
            case 3:
                return 24;
            case 4:
                return 48;
            default:
                return 0;
        }
    }

    @Nullable
    @Override
    public EventType getSelectedEventType() {
        return selectedEventType;
    }

    @Override
    public String getEventTitle() {
        return eventTitleEditText.getText().toString();
    }

    @Override
    public String getEventDescription() {
        return eventBodyEditText.getText().toString();
    }

    @Override
    public boolean shouldSendNotification(){
        return notificationsCheckbox.isChecked();
    }

    @Override
    public void setNotificationAlarm(Long alertTime) {
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    @Override
    public void loadSubjectsSpinner(ArrayList<String> subjectNames) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void showLoadingToast() {
        loadToast.setText("Adding new calendar event...");
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void showLoadingBar(boolean show) {
        if (show){
            progressBar.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadingToastSuccess() {
        loadToast.success();
    }

    @Override
    public void onLoadingToastError() {
        loadToast.error();
    }

    @Override
    public void setEventImageViewSelected(int id) {
        ImageView imageView = findViewById(id);

        for (ImageView image : eventTypes) {
            if (imageView.getId() == image.getId())
                image.setColorFilter(ContextCompat.getColor(context, R.color.transparent));
            else
                image.setColorFilter(ContextCompat.getColor(context, R.color.colorWhiteTransparentText));
        }
    }

    @Override
    public void setGradeImageViewSelected(int id) {
        ImageView imageView = findViewById(id);

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
    public boolean isRequiredDataFilled() {
        // Every event needs to have a title.
        if (getEventTitle().isEmpty())
            return false;

        // If we entered the Activity from the Calendar view,
        // that means that we must have a selected date.
        if (selectedCalendarDay != null)
            return true;

        // If that's not the case, we then check if
        // the placeholder text has  changed.
        return !dateSelectionText.getText().toString().equals("DD/M/YYYY");
    }

    @Override
    public void showRequiredDataErrorToast() {
        Toast.makeText(this, "You need to fill all necessary fields!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void returnToHomeActivity(CalendarEvent calendarEvent) {
        if (selectedCalendarDay == null) {
            setResult(RESULT_OK, new Intent().putExtra("added_event",  (Parcelable) calendarEvent));
        }

        this.finish();
    }
}

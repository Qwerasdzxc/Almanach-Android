package com.code_dream.almanach.subject_profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.code_dream.almanach.calendar_add_event.CalendarAddEventActivity;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.SubjectCategoryAdapter;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.EventProfileActivity;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.SubjectCategory;
import com.code_dream.almanach.models.SubjectCategoryItem;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.Utility;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectProfileActivity extends BaseActivity implements SubjectProfileContract.View,
                                                                    SubjectCategoryAdapter.EventItemClickListener {

    @BindView(R.id.subject_profile_toolbar) Toolbar toolbar;
    @BindView(R.id.subject_profile_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.subject_profile_progress_bar) ProgressBar progressBar;
    @BindView(R.id.subject_profile_parent_menu) LinearLayout parentMenu;
    @BindView(R.id.subject_profile_grade_image) ImageView gradeImage;

    @InjectExtra("subject_name")
    String selectedSubjectName;

    private SubjectProfilePresenter presenter;

    private SubjectCategoryAdapter adapter;
    private ArrayList<SubjectCategory> recyclerViewItems = new ArrayList<>();

    private LoadToast loadToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_profile);
        setupView();

        presenter.loadCalendarEvents();
    }

    @Override
    public void setupPresenter() {
        presenter = new SubjectProfilePresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        loadToast = new LoadToast(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(selectedSubjectName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setItemAnimator(null);

        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        presenter.finishActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.finishActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.subject_profile_fab)
    public void onFabClick(){
        Intent calendarIntent = new Intent(this, CalendarAddEventActivity.class);

        calendarIntent.putExtra("selected_subject", selectedSubjectName);
        startActivityForResult(calendarIntent, Registry.OPEN_ADD_EVENT_ACTIVITY);
    }

    @Override
    public void onEventItemClick(SubjectCategoryItem item) {
        Intent intent = new Intent(this, EventProfileActivity.class);
        intent.putExtra("selected_event", (Parcelable) item.getCalendarEvent());
        startActivityForResult(intent, Registry.OPEN_SUBJECT_PROFILE_ACTIVITY);
    }

    @Override
    public void displayAllCalendarEvents(HashMap<EventType, ArrayList<SubjectCategoryItem>> allCalendarEvents) {
        List<SubjectCategory> recyclerViewList = new ArrayList<>();

        recyclerViewList.add(new SubjectCategory("Tests", allCalendarEvents.get(EventType.TEST)));
        recyclerViewList.add(new SubjectCategory("Homeworks", allCalendarEvents.get(EventType.HOME_WORK)));
        recyclerViewList.add(new SubjectCategory("Presentations", allCalendarEvents.get(EventType.PRESENTATION)));
        recyclerViewList.add(new SubjectCategory("Other assignments", allCalendarEvents.get(EventType.OTHER_ASSIGNMENT)));

        recyclerViewItems.addAll(recyclerViewList);

        adapter = new SubjectCategoryAdapter(recyclerViewItems);
        adapter.setItemClickListener(this);

        recyclerView.setAdapter(adapter);

        // Auto expanding the categories with small num. of items.
        for (SubjectCategory subjectCategory : recyclerViewItems) {
            if (subjectCategory.getItems().size() > 0 && subjectCategory.getItems().size() < Registry.MAXIMUM_ITEMS_FOR_AUTO_EXPANDING)
                adapter.toggleGroup(subjectCategory);
        }
    }

    @Override
    public void displayNoEventsScreen() {
        List<SubjectCategory> recyclerViewList = new ArrayList<>();

        recyclerViewList.add(new SubjectCategory("Tests", new ArrayList<SubjectCategoryItem>()));
        recyclerViewList.add(new SubjectCategory("Homeworks", new ArrayList<SubjectCategoryItem>()));
        recyclerViewList.add(new SubjectCategory("Presentations", new ArrayList<SubjectCategoryItem>()));
        recyclerViewList.add(new SubjectCategory("Other assignments", new ArrayList<SubjectCategoryItem>()));

        recyclerViewItems.addAll(recyclerViewList);

        adapter = new SubjectCategoryAdapter(recyclerViewItems);
        adapter.setItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showFinalGrade(Grades grade) {
        gradeImage.setImageResource(Utility.getImageDrawableByGrade(grade));
    }

    @Override
    public String getSubjectName() {
        return selectedSubjectName;
    }

    @Override
    public void displayLoadingBar(boolean display) {
        if (display) {
            progressBar.setVisibility(View.VISIBLE);
            parentMenu.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            parentMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingToast(String text) {
        loadToast.setText(text);
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void dismissLoadingToastSuccessfully() {
        loadToast.success();
    }

    @Override
    public void dismissLoadingToastWithFailure() {
        loadToast.error();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (adapter != null)
            adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Registry.OPEN_SUBJECT_PROFILE_ACTIVITY && resultCode == RESULT_OK) {
            // Retrieve the subject name from the result.
            selectedSubjectName = data.getStringExtra("subject_name");

            CalendarEvent modifiedCalendarEvent = data.getParcelableExtra("changed_event");

            if (modifiedCalendarEvent != null) {
                for (SubjectCategory subjectCategory : recyclerViewItems)
                    for (int i = 0; i < subjectCategory.getItems().size(); i++)
                        if (subjectCategory.getItems().get(i).getCalendarEvent().getEventId() == modifiedCalendarEvent.getEventId()) {
                            // Replace the original calendar event with the modified one.
                            subjectCategory.getItems().get(i).setCalendarEvent(modifiedCalendarEvent);
                        }
            } else {
                // There is no modified calendar events so we try to find the deleted ones.
                modifiedCalendarEvent = data.getParcelableExtra("deleted_event");

                for (SubjectCategory subjectCategory : recyclerViewItems)
                    for (int i = 0; i < subjectCategory.getItems().size(); i++)
                        if (subjectCategory.getItems().get(i).getCalendarEvent().getEventId() == modifiedCalendarEvent.getEventId()) {

                            // Delete the calendar event from the list.
                            subjectCategory.getItems().remove(i);
                        }
            }

        } else if (requestCode == Registry.OPEN_ADD_EVENT_ACTIVITY && resultCode == RESULT_OK) {
            CalendarEvent newlyCreatedEvent = data.getParcelableExtra("added_event");
            SubjectCategory eventTypeCategory = recyclerViewItems.get(newlyCreatedEvent.getEventType().ordinal());

            eventTypeCategory.getItems().add(new SubjectCategoryItem(newlyCreatedEvent.getTitle(), newlyCreatedEvent));
            adapter.toggleGroup(eventTypeCategory);
        }

        // Update the changed Calendar event in the RecyclerView.
        adapter.notifyDataSetChanged();

        presenter.calculateFinalGrade(recyclerViewItems, true);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finishActivityAfterSubjectDeletion() {
        setResult(Registry.RESULT_SUBJECT_LIST_DELETION, new Intent().putExtra("deleted_subject", selectedSubjectName));
        this.finish();
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("modified_subject", selectedSubjectName);
        intent.putExtra("changed_grade", presenter.calculateFinalGrade(recyclerViewItems, false));

        boolean hasTests = false;
        boolean hasHomeworks = false;
        boolean hasPresentations = false;
        boolean hasOtherAssignments = false;

        for (SubjectCategory category : recyclerViewItems)
            for (SubjectCategoryItem categoryItem : category.getItems()) {
                if (categoryItem.getCalendarEvent().getEventType() == EventType.TEST)
                    hasTests = true;
                else if (categoryItem.getCalendarEvent().getEventType() == EventType.HOME_WORK)
                    hasHomeworks = true;
                else if (categoryItem.getCalendarEvent().getEventType() == EventType.PRESENTATION)
                    hasPresentations = true;
                else if (categoryItem.getCalendarEvent().getEventType() == EventType.OTHER_ASSIGNMENT)
                    hasOtherAssignments = true;
            }

        intent.putExtra("subject_has_tests", hasTests);
        intent.putExtra("subject_has_homeworks", hasHomeworks);
        intent.putExtra("subject_has_presentations", hasPresentations);
        intent.putExtra("subject_has_other_assignments", hasOtherAssignments);

        setResult(Registry.RESULT_SUBJECT_EVENT_DELETION, intent);
        this.finish();
    }
}

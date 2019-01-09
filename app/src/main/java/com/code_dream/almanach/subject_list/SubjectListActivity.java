package com.code_dream.almanach.subject_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.subject_profile.SubjectProfileActivity;
import com.code_dream.almanach.utility.Registry;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.EnumSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectListActivity extends BaseActivity implements SubjectListContract.View,
                                                                 RecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.subject_list_toolbar) Toolbar toolbar;
    @BindView(R.id.subject_list_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.subject_list_loading_bar) ProgressBar progressBar;
    @BindView(R.id.subject_list_fab) FloatingActionMenu fab;
    @BindView(R.id.subject_list_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private SubjectListPresenter presenter;

    private RecyclerViewAdapter adapter;

    private ArrayList<Object> recyclerViewItems = new ArrayList<>();

    private LoadToast loadToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_list);
        setupView();

        presenter.loadAllSubjects();
    }

    @Override
    public void setupPresenter() {
        presenter = new SubjectListPresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subject list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setClosedOnTouchOutside(true);

        loadToast = new LoadToast(this);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadAllSubjects();
            }
        });

        adapter = new RecyclerViewAdapter(recyclerViewItems);
        adapter.setItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.subject_list_fab_menu_item_remove)
    public void onRemoveSubjectClick(){
        ArrayList<String> subjectNames = new ArrayList<>(recyclerViewItems.size());

        // Add all Subject's names to the newly created list.
        for (SubjectListItem subject : (ArrayList<SubjectListItem>)(ArrayList<?>)(recyclerViewItems))
            subjectNames.add(subject.getSubjectName());

        new MaterialDialog.Builder(this)
                .title("Remove subject")
                .items(subjectNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        dialog.dismiss();
                        fab.close(true);

                        presenter.deleteSubject((SubjectListItem) recyclerViewItems.get(which));
                    }
                })
                .show();
    }

    @OnClick(R.id.subject_list_fab_menu_item_add)
    public void onAddSubjectClick(){
        new MaterialDialog.Builder(this)
                .title("Add new subject")
                .content("Enter a name for the new subject:")
                .autoDismiss(false)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                .input("New subject name", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!subjectAlreadyExists(input.toString()) && !input.toString().isEmpty()){
                            presenter.addNewSubject(input.toString());

                            fab.close(true);
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(SubjectListActivity.this, "Subject name invalid or already exists!", Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    @Override
    public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
        if (fab.isOpened()) {
            fab.close(true);
            return;
        }

        Intent intent = new Intent(this, SubjectProfileActivity.class);
        intent.putExtra("subject_name", ((SubjectListItem) recyclerViewItems.get(position)).getSubjectName());
        startActivityForResult(intent, Registry.OPEN_SUBJECT_LIST_ACTIVITY);
    }

    @Override
    public void displaySubjectList(ArrayList<SubjectListItem> loadedSubjects) {
        recyclerViewItems.clear();
        recyclerViewItems.addAll(loadedSubjects);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeSubjectFromList(SubjectListItem subjectToRemove) {
        int itemPosition = recyclerViewItems.indexOf(subjectToRemove);
        recyclerView.scrollToPosition(itemPosition);

        recyclerViewItems.remove(itemPosition);
        adapter.notifyItemRemoved(itemPosition);
    }

    @Override
    public void addSubjectToList(SubjectListItem subjectToAdd) {
        recyclerViewItems.add(subjectToAdd);
        adapter.notifyItemInserted(recyclerViewItems.size() - 1);

        recyclerView.scrollToPosition(recyclerViewItems.size() - 1);
    }

    @Override
    public void displayLoadingBar(boolean display) {
        if (display) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingToast(String text) {
        loadToast.setText(text);
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void showFab(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
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
    public void setSwipeRefreshLayoutRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    private boolean subjectAlreadyExists(String subjectName){
        for (SubjectListItem item : (ArrayList<SubjectListItem>)(ArrayList<?>)(recyclerViewItems))
            if (item.getSubjectName().equals(subjectName))
                return true;

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onResult", "called: " + requestCode + ", result: " + resultCode);
        if (requestCode == Registry.OPEN_SUBJECT_LIST_ACTIVITY && resultCode == Registry.RESULT_SUBJECT_LIST_DELETION) {
            String deletedSubjectName = data.getStringExtra("deleted_subject");
            SubjectListItem itemToDelete = null;

            for (SubjectListItem item : (ArrayList<SubjectListItem>)(ArrayList<?>)(recyclerViewItems)) {
                if (item.getSubjectName().equals(deletedSubjectName))
                    itemToDelete = item;
            }

            recyclerViewItems.remove(itemToDelete);
            adapter.notifyDataSetChanged();
        }
        else if (requestCode == Registry.OPEN_SUBJECT_LIST_ACTIVITY && resultCode == Registry.RESULT_SUBJECT_LIST_GRADE_CHANGED) {
            String modifiedSubjectName = data.getStringExtra("modified_subject");
            Grades changedGrade = (Grades) data.getSerializableExtra("changed_grade");

            for (SubjectListItem item : (ArrayList<SubjectListItem>)(ArrayList<?>)(recyclerViewItems)) {
                if (item.getSubjectName().equals(modifiedSubjectName))
                    item.setGrade(changedGrade);
            }

            adapter.notifyDataSetChanged();
        }
        else if (requestCode == Registry.OPEN_SUBJECT_LIST_ACTIVITY && resultCode == Registry.RESULT_SUBJECT_EVENT_DELETION) {
            String modifiedSubjectName = data.getStringExtra("modified_subject");
            Grades changedGrade = (Grades) data.getSerializableExtra("changed_grade");

            for (SubjectListItem item : (ArrayList<SubjectListItem>)(ArrayList<?>)(recyclerViewItems)) {
                if (item.getSubjectName().equals(modifiedSubjectName)) {
                    EnumSet<EventType> eventTypes = EnumSet.noneOf(EventType.class);

                    if (data.getBooleanExtra("subject_has_tests", false))
                        eventTypes.add(EventType.TEST);
                    if (data.getBooleanExtra("subject_has_homeworks", false))
                        eventTypes.add(EventType.HOME_WORK);
                    if (data.getBooleanExtra("subject_has_presentations", false))
                        eventTypes.add(EventType.PRESENTATION);
                    if (data.getBooleanExtra("subject_has_other_assignments", false))
                        eventTypes.add(EventType.OTHER_ASSIGNMENT);

                    item.setGrade(changedGrade);
                    item.setEventTypes(eventTypes);

                    break;
                }
            }

            adapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

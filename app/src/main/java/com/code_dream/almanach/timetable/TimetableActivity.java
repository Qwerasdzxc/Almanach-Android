package com.code_dream.almanach.timetable;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.code_dream.almanach.timetable.models.NewTimetableItem;
import com.code_dream.almanach.timetable.models.TimetableListItem;
import com.code_dream.almanach.utility.UserInfo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.adapters.item_touch_helper.OnStartDragListener;
import com.code_dream.almanach.adapters.item_touch_helper.SimpleItemTouchHelperCallback;
import com.code_dream.almanach.models.TimetableItem;
import com.code_dream.almanach.subject_profile.SubjectProfileActivity;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.Utility;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimetableActivity extends BaseActivity implements TimetableContract.View,
                                                               OnStartDragListener,
                                                               RecyclerViewAdapter.ItemClickListener,
                                                               RecyclerViewAdapter.OnItemRemovedListener {

    @BindView(R.id.timetable_activity_toolbar) Toolbar toolbar;
    @BindView(R.id.timetable_calendar_view) MaterialCalendarView calendarView;
    @BindView(R.id.timetable_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.timetable_progress_bar) ProgressBar progressBar;
    @BindView(R.id.timetable_title) TextView toolbarTitle;
    @BindView(R.id.timetable_edit_button) TextView editButton;
    @BindView(R.id.timetable_sync_button) ImageView syncButton;

    private ArrayList<Object> recyclerViewItems = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    private TimetablePresenter presenter;

    private ItemTouchHelper itemTouchHelper;
    private SimpleItemTouchHelperCallback touchCallback;

    private Drawable toolbarHomeIcon;
    private LoadToast loadToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);
        setupView();

        presenter.loadTimetable();
    }

    @Override
    public void setupPresenter() {
        presenter = new TimetablePresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarHomeIcon = toolbar.getNavigationIcon();

        loadToast = new LoadToast(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new RecyclerViewAdapter(recyclerViewItems);
        adapter.setStartDragListener(this);
        adapter.setItemClickListener(this);
        adapter.setOnItemRemovedListener(this);

        recyclerView.setAdapter(adapter);

        //Code for dragging the Subjects
        touchCallback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Prevent the user from swiping the Calendar widget
        calendarView.setPagingEnabled(false);
        calendarView.setTopbarVisible(false);

        // Set the current day of the week
        calendarView.setDateSelected(Calendar.getInstance(), true);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                presenter.onDayChanged();
            }
        });
    }

    @OnClick(R.id.timetable_edit_button)
    public void onEditConfirmClick() {
        presenter.changeTimetableMode();
    }

    @OnClick(R.id.timetable_sync_button)
    public void onSyncButtonClick() {
        presenter.onSyncClick();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void callBackButton() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Called when clicked on the Toolbar's top left icon
        if (item.getItemId() == android.R.id.home){
            // If Timetable's in EDIT mode, then we switch to VIEW mode
            if (presenter.isInEditMode()) {
                presenter.onCancelTimetableChangesClick();
                // Returning true so that we don't back out of the Activity
                return true;
            } else {
                // Timetable's in VIEW mode so we exit the Activity
                return false;
            }
        }
        // Leaving other items to Android's original implementation
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
        // Called when clicked on the 'Add subject' button
        if (viewHolder == RecyclerViewAdapter.NewTimetableItemViewHolder.class)
            presenter.getItemsToAdd();
        else if (viewHolder == RecyclerViewAdapter.GenericListItemViewHolder.class)
            startActivity(new Intent(this, SubjectProfileActivity.class).putExtra("subject_name", ((GenericListItem) recyclerViewItems.get(position)).getItemText()));
    }

    @Override
    public void onItemRemoved(int pos) {
        // Called when item gets swiped away
        presenter.onItemRemoved((TimetableListItem) recyclerViewItems.get(pos));
    }

    @Override
    public void displayItemsToAdd(ArrayList<String> itemsToAdd) {
        new MaterialDialog.Builder(this)
                .title("Add new item")
                .items(itemsToAdd)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        dialog.dismiss();

                        presenter.addItemToTimetable(text.toString(), getSelectedTimetableDay(), recyclerViewItems.size() - 1);
                    }
                })
                .show();
    }

    @Override
    public void onItemsLoaded() {
        presenter.showItems();
    }

    @Override
    public void updateAllItems(final GenericListItem[] genericListItems) {
        recyclerViewItems.clear();
        recyclerViewItems.addAll(Arrays.asList(genericListItems));
        recyclerView.swapAdapter(adapter, false);
        recyclerView.setLayoutManager(recyclerView.getLayoutManager());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addItem(TimetableListItem timetableListItem) {
        recyclerViewItems.add(recyclerViewItems.size() - 1, timetableListItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void switchToViewMode() {
        toolbar.setNavigationIcon(toolbarHomeIcon);
        toolbarTitle.setText("Class schedule");
        editButton.setText("EDIT");
    }

    @Override
    public void switchToEditMode() {
        toolbar.setNavigationIcon(R.drawable.ic_button_close);
        toolbarTitle.setText("Edit schedule");
        editButton.setText("CONFIRM");
    }

    @Override
    public void displayProgressBar(boolean display) {
        if (display){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayEditButton(boolean display) {
        if (display) {
            editButton.setVisibility(View.VISIBLE);
            syncButton.setVisibility(View.GONE);
        } else {
            editButton.setVisibility(View.GONE);
            syncButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displaySnackbarFailure() {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Failed loading the timetable.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadTimetable();
                snackbar.dismiss();
            }
        }).show();
    }

    @Override
    public void setDraggingEnabled(boolean enabled) {
        touchCallback.setLongPressEnabled(enabled);
        touchCallback.setSwipeEnabled(enabled);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void addAddNewItemItemToRecyclerView() {
        recyclerViewItems.add(new NewTimetableItem());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeAddNewItemItemFromRecyclerView() {
        if (!recyclerViewItems.isEmpty()) {
            recyclerViewItems.remove(recyclerViewItems.size() - 1);
            adapter.notifyItemRemoved(recyclerViewItems.size());
        }
    }

    @Override
    public TimetableItem.Day getSelectedTimetableDay() {
        return Utility.getWeekDayByDate(Utility.calendarDayToDateTime(calendarView.getSelectedDate()));
    }

    @Override
    public ArrayList<TimetableListItem> getRecyclerViewItems() {
        return (ArrayList<TimetableListItem>)(ArrayList<?>)(recyclerViewItems);
    }

    @Override
    public void showLoadingToast() {
        loadToast.setText("Uploading timetable...");
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
    public void showTimetableCancelConfirmationDialog() {
        new MaterialDialog.Builder(this)
                .title("Cancel all changes?")
                .content("Are you sure you want to cancel all changes made to the timetable?")
                .positiveText("Yes")
                .negativeText("Save changes")
                .neutralText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.cancelTimetableChanges();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.changeTimetableMode();
                    }
                })
                .show();
    }

    @Override
    public void showIntro(){
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.timetable_edit_button), "This is your Timetable", "Click here to add and edit your Timetable subjects.")
                                .dimColor(R.color.colorPrimaryDark)
                                .cancelable(false))
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        sharedPreferences.edit().putBoolean(Registry.SHARED_PREFS_TIMETABLE_TUTORIAL_KEY + UserInfo.CURRENT_USER.getId(), true).apply();
                        presenter.changeTimetableMode();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget) {}

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {}
                }).start();
    }

    @Override
    public boolean didShowIntro() {
        return sharedPreferences.getBoolean(Registry.SHARED_PREFS_TIMETABLE_TUTORIAL_KEY + UserInfo.CURRENT_USER.getId(), false);
    }
}

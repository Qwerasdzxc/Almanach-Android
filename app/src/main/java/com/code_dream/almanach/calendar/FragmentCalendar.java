package com.code_dream.almanach.calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.calendar.models.CalendarEventType;
import com.code_dream.almanach.calendar.models.CalendarEventTypeSmall;
import com.code_dream.almanach.calendar.network.CalendarInteractor;
import com.code_dream.almanach.calendar_add_event.CalendarAddEventActivity;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.EventProfileActivity;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.utility.AlertReceiver;
import com.code_dream.almanach.utility.Registry;

import net.steamcrafted.loadtoast.LoadToast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalendar extends Fragment implements CalendarContract.View,
														  RecyclerViewAdapter.ItemClickListener,
														  RecyclerViewAdapter.ContextMenuClickListener,
														  RecyclerViewAdapter.EventTypeSelectionClickListener {

	@BindView(R.id.calendar_view) MaterialCalendarView calendarView;
	@BindView(R.id.calendar_progress_bar) ProgressBar progressBar;
	@BindView(R.id.calendar_recycler_view) RecyclerView recyclerView;
	@BindView(R.id.calendar_view_layout) LinearLayout calendarViewLayout;
	@BindView(R.id.calendar_offline_mode_view) LinearLayout offlineModeView;

	RecyclerViewAdapter adapter;

	String[] names = {"Add a test", "Add homework", "Add a presentation", "Add other assignment"};

	int[] images = {R.drawable.ic_test, R.drawable.ic_homework, R.drawable.ic_presentation, R.drawable.ic_other_assignment};

	ArrayList<Object> recyclerViewContentList = new ArrayList<>();

	private CalendarPresenter presenter;

	private LoadToast loadToast;

	private boolean oneTimePresenterInit = true;

	public FragmentCalendar() {
		// Required empty public constructor
	}

	@Override
	public void onResume() {
	    if (presenter == null)
	        presenter = new CalendarPresenter(this, new CalendarInteractor());

		super.onResume();

		presenter.loadAllCalendarEvents();
		presenter.onDateSelected();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_calendar, container, false);
	}

	@Override
	public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		// TODO: Handle orientation changes

		if (oneTimePresenterInit) {
			presenter = new CalendarPresenter(this, new CalendarInteractor());
			oneTimePresenterInit = false;
		}

		initialize(view);
	}

	private void initialize(View view){
		loadToast = new LoadToast(getContext());

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

		adapter = new RecyclerViewAdapter(recyclerViewContentList);
		adapter.setItemClickListener(this);
		adapter.setContextMenuClickListener(this);
		adapter.setEventTypeSelectionClickListener(this);

		recyclerView.setAdapter(adapter);

		calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
			@Override
			public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
				presenter.onDateSelected();
			}
		});

		calendarView.setDateSelected(Calendar.getInstance(), true);
	}

	@Override
	public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
		presenter.onRecyclerViewClick(viewHolder, position);
	}

	@Override
	public void onContextMenuClick(ImageView imgContextMenu, Object item) {
		Long alertTime = new GregorianCalendar().getTimeInMillis() + 5 * 1000;

		Intent alertIntent = new Intent(getContext(), AlertReceiver.class);
		AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(getActivity(), 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		presenter.onContextMenuClick(imgContextMenu, (CalendarEvent) item);
	}

	@Override
	public void onAddNewEventClick() {
		Toast.makeText(getContext(), "Please select one of the events...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEventTypeClick(EventType selectedEventType) {
		presenter.onEventTypeClick(selectedEventType);
	}

	@Override
	public void showContextMenu(ImageView imgContextMenu, final CalendarEvent calendarEvent) {
		PopupMenu popup = new PopupMenu(getActivity(), imgContextMenu);
		popup.inflate(R.menu.calendar_event_context_menu);

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				presenter.onContextMenuItemClick(item, calendarEvent);
				return false;
			}
		});
		popup.show();
	}

	@Override
	public void showEventSelectionRecyclerView() {
		recyclerViewContentList.clear();

		for (int i = 0; i < names.length; i++) {
			CalendarEventType calendarEventType = new CalendarEventType(names[i], images[i]);
			recyclerViewContentList.add(calendarEventType);
		}

		adapter.notifyDataSetChanged();
	}

	@Override
	public void showLoadedEventsRecyclerView(ArrayList<CalendarEvent> calendarEventsForSelectedDay) {
		recyclerViewContentList.clear();
		recyclerViewContentList.add(new CalendarEventTypeSmall());
		recyclerViewContentList.addAll(calendarEventsForSelectedDay);

		adapter.notifyDataSetChanged();
	}

	@Override
	public void showOfflineModeView(boolean show) {
		if (show) {
			offlineModeView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else {
			offlineModeView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public CalendarEvent getCalendarEventFromRecyclerView(int position) {
		return (CalendarEvent) recyclerViewContentList.get(position);
	}

	@Override
	public void removeEventFromList(CalendarEvent deletedCalendarEvent) {
		int eventPosition = recyclerViewContentList.indexOf(deletedCalendarEvent);

		recyclerViewContentList.remove(eventPosition);
		adapter.notifyItemRemoved(eventPosition);
		adapter.notifyItemRangeChanged(eventPosition, recyclerViewContentList.size());

		presenter.updateCalendarDecorators();

		loadToast.success();
	}

	@Override
	public DateTime getSelectedDateTime() {
		return new DateTime(calendarView.getSelectedDate().getYear(), calendarView.getSelectedDate().getMonth() + 1, calendarView.getSelectedDate().getDay(), 0, 0);
	}

	@Override
	public void setProgressBarVisibility(int visibility) {
		if (visibility == View.VISIBLE) {
			progressBar.setVisibility(View.VISIBLE);
			calendarViewLayout.setVisibility(View.GONE);
		} else {
			progressBar.setVisibility(View.GONE);
			calendarViewLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showEventDeletingLoadToast() {
		loadToast.setText("Event deleting...");
		loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
		loadToast.show();
	}

	@Override
	public void dismissLoadToastWithError(){
		loadToast.error();
	}

	@Override
	public void startAddEventActivity(EventType selectedEventType) {
		Intent calendarIntent = new Intent(getActivity(), CalendarAddEventActivity.class);
		CalendarDay currentDay = calendarView.getSelectedDate();

		calendarIntent.putExtra("selected_date", currentDay);
		calendarIntent.putExtra("selected_event_type", selectedEventType);
		startActivity(calendarIntent);
	}

	@Override
	public void startEditEventActivity(CalendarEvent calendarEvent) {
		Intent editEventIntent = new Intent(getActivity(), EventProfileActivity.class);
		editEventIntent.putExtra("selected_event", (Parcelable) calendarEvent);

		startActivity(editEventIntent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		presenter.onActivityResult(resultCode, data);

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void addCalendarDecorator(int eventColor, ArrayList<CalendarDay> calendarDays) {
		calendarView.addDecorator(new EventDecorator(eventColor, calendarDays));
	}

	@Override
	public void removeAllCalendarDecorators() {
		calendarView.removeDecorators();
		calendarView.invalidateDecorators();
	}
}

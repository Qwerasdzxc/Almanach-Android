package com.code_dream.almanach.home;


import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code_dream.almanach.network.services.MessageService;
import com.code_dream.almanach.notifications_tab.FragmentNotificationsTab;
import com.code_dream.almanach.utility.UserInfo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.calendar.FragmentCalendar;
import com.code_dream.almanach.chat_tab.FragmentChat;
import com.code_dream.almanach.home.network.HomeInteractor;
import com.code_dream.almanach.middle_drawer.FragmentMiddleDrawer;
import com.code_dream.almanach.R;
import com.code_dream.almanach.settings.SettingsActivity;
import com.code_dream.almanach.adapters.ViewPagerAdapter;
import com.code_dream.almanach.login.LoginActivity;
import com.code_dream.almanach.news_feed.FragmentNewsFeed;
import com.code_dream.almanach.search.SearchableActivity;
import com.code_dream.almanach.utility.Registry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class HomeActivity extends BaseActivity implements HomeContract.View {

	// UI References
	@BindView(R.id.viewPager) ViewPager viewPager;
	@BindView(R.id.tab_layout) TabLayout tabLayout;
	@BindView(R.id.main_toolbar) Toolbar toolbar;
	@BindView(R.id.news_feed_search_view) SearchView searchView;
	@BindView(R.id.search_src_text) EditText searchEditText;

	private ProgressDialog progressDialog;

	private HomePresenter presenter;

//	private Intent messageServiceIntent;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		// Starting the Messaging service if it isn't already active.
//		messageServiceIntent = new Intent(this, MessageService.class);
//
//		if (!isMyServiceRunning(MessageService.class))
//			startService(messageServiceIntent);

		setContentView(R.layout.activity_main);
		setupView();

		if (!sharedPreferences.getBoolean(Registry.SHARED_PREFS_INTRO_KEY + UserInfo.CURRENT_USER.getId(), false))
			showIntro();
	}

	@Override
	protected void onDestroy() {
//		stopService(messageServiceIntent);
		super.onDestroy();
	}

    @Override
    protected void onResume() {
        if (presenter == null)
            setupPresenter();
	    super.onResume();
    }

    @Override
	public void setupPresenter() {
		presenter = new HomePresenter(this, new HomeInteractor());
	}

	@Override
	public void setupView() {
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(Registry.TOOLBAR_NO_TITLE);

		presenter.setupTabLayout(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setOffscreenPageLimit(4);
	}

	@OnFocusChange(R.id.search_src_text)
	public void onSearchFocusChange(boolean hasFocus){
		presenter.onSearchFocusChange(hasFocus);
	}

	@OnClick(R.id.news_feed_search_view)
	public void onSearchViewClick(View view){
		presenter.onSearchViewClick();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		presenter.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setupTabLayout(ViewPagerAdapter viewPagerAdapter) {
		viewPagerAdapter.AddFragments(new FragmentNewsFeed());
		viewPagerAdapter.AddFragments(new FragmentMiddleDrawer());
		viewPagerAdapter.AddFragments(new FragmentNotificationsTab());
		viewPagerAdapter.AddFragments(new FragmentCalendar());

		viewPager.setAdapter(viewPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);

		for (int i = 0; i < tabLayout.getTabCount(); i++) {
			if (i == 0)
				tabLayout.getTabAt(i).setIcon(R.drawable.ic_news2);
			else if (i == 1)
				tabLayout.getTabAt(i).setIcon(R.drawable.ic_notebook);
			else if (i == 2) {
				tabLayout.getTabAt(i).setCustomView(LayoutInflater.from(this).inflate(R.layout.other_badge_layout, null));

//				tabLayout.getTabAt(i).setIcon(R.drawable.ic_bell_2);
			}
			else
				tabLayout.getTabAt(i).setIcon(R.drawable.ic_calendar);

			tabLayout.getTabAt(i).setCustomView(R.layout.other_home_tab_layout);
		}
	}

	@Override
	public void search() {
		startActivity(new Intent(HomeActivity.this, SearchableActivity.class));
		searchEditText.clearFocus();
	}

	@Override
	public void settings() {
		startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
	}

	@Override
	public void showProgressDialog() {
		progressDialog = ProgressDialog.show(this, "Logging out...", "Please wait...", true, false);
	}

	@Override
	public void dismissProgressDialog() {
		progressDialog.dismiss();
	}

	@Override
	public void openLoginActivity() {
		Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
		intent.putExtra("auto_login", false);

		startActivity(intent);
		this.finish();
	}

	@Override
	public void showLogoutErrorToast() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(HomeActivity.this, "Network connection problem. Couldn't logout!", Toast.LENGTH_LONG).show();
			}
		});
	}

	public void showIntro(){
		new TapTargetSequence(this)
				.targets(
						TapTarget.forView(findViewById(R.id.news_feed_centered_view), "Welcome to Almanach", "Here's a little introduction to help you set things up.")
								.dimColor(R.color.colorPrimaryDark)
								.cancelable(false),
						TapTarget.forView(tabLayout.getTabAt(0).getCustomView(), "This is your News feed", "All of your school posts will be shown here.")
								.dimColor(R.color.colorPrimaryDark)
								.cancelable(false),
						TapTarget.forView(tabLayout.getTabAt(1).getCustomView(), "This is your Subjects section", "Here you can add grades and calculate your GPA.")
								.dimColor(R.color.colorPrimaryDark)
								.cancelable(false),
						TapTarget.forView(tabLayout.getTabAt(2).getCustomView(), "This is your Social tab", "Send messages and chat with your friends.")
								.dimColor(R.color.colorPrimaryDark)
								.cancelable(false),
						TapTarget.forView(tabLayout.getTabAt(3).getCustomView(), "This is your Calendar view", "Add and plan all of your school events and homeworks here.")
								.dimColor(R.color.colorPrimaryDark)
								.cancelable(false),
						TapTarget.forView(searchView, "Search for schools and friends", "Type in the name of friends and schools around you to see how they match up to you!")
								.dimColor(R.color.colorPrimaryDark)
								.tintTarget(false)
								.cancelable(true))
				.listener(new TapTargetSequence.Listener() {
					@Override
					public void onSequenceFinish() {
						sharedPreferences.edit().putBoolean(Registry.SHARED_PREFS_INTRO_KEY + UserInfo.CURRENT_USER.getId(), true).apply();
					}

					@Override
					public void onSequenceStep(TapTarget lastTarget) {}

					@Override
					public void onSequenceCanceled(TapTarget lastTarget) {}
				}).start();
	}

	@Override
	public void changeTabBadgeNumber(int number) {
		View badgeTextView = tabLayout.getTabAt(2).getCustomView();

		if (number > 0) {
			badgeTextView.setVisibility(View.VISIBLE);
			//badgeTextView.setText(number);
		} else {
			badgeTextView.setVisibility(View.GONE);
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tabLayout.invalidate();
			}
		});
	}

	//	private boolean isMyServiceRunning(Class<?> serviceClass) {
//		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//			if (serviceClass.getName().equals(service.service.getClassName())) {
//				Log.i ("isMyServiceRunning?", true+"");
//				return true;
//			}
//		}
//		Log.i ("isMyServiceRunning?", false+"");
//		return false;
//	}
}

package com.code_dream.almanach.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.settings.account_preference.AccountPreferenceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements SettingsContract.ActivityView,
															       SettingsFragment.Callback {

	@BindView(R.id.settings_toolbar) Toolbar toolbar;

	private static final String TAG_NESTED = "TAG_NESTED";

	private Bundle savedInstanceState;
	private SettingsPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);
		setupView();

		this.savedInstanceState = savedInstanceState;
	}

	@Override
	public void setupPresenter() {
		presenter = new SettingsPresenter(this);
	}

	@Override
	public void setupView() {
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		presenter.initializeFragment(savedInstanceState);
	}

	@Override
	public void onNestedPreferenceSelected(int key) {
		getFragmentManager().beginTransaction().replace(R.id.content_frame, AccountPreferenceFragment.newInstance(key)).addToBackStack(TAG_NESTED).commit();
	}

	@Override
	public void displayMainSettingsFragment() {
		getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		presenter.onActivityBackButtonPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		presenter.onActivityOptionsItemSelected(item);
		return presenter.onActivityOptionsItemSelected(item);
	}

	@Override
	public boolean superOnOptionsItemSelected(MenuItem item) {
		return onOptionsItemSelected(item);
	}

	@Override
	public void superOnBackButtonPressed() {
		super.onBackPressed();
	}

	@Override
	public void popBackStack() {
		getFragmentManager().popBackStack();
	}

	@Override
	public int getBackStackEntryCount() {
		return getFragmentManager().getBackStackEntryCount();
	}

	@Override
	public void finishActivity() {
		this.finish();
	}
}


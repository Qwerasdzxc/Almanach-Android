package com.code_dream.almanach.settings.account_preference;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

import com.code_dream.almanach.R;
import com.code_dream.almanach.login.LoginActivity;

public class AccountPreferenceFragment extends PreferenceFragment implements AccountPreferenceContract.View{

	public static final int ACCOUNT_SCREEN_KEY = 1;
	private static final String TAG_KEY = "NESTED_KEY";

	private Preference deleteUser;
	private EditTextPreference changeNameEditText;
	private Preference btnDateFilter;
	private ProgressDialog progressDialog;

	private AccountPreferencePresenter presenter;

	// DatePicker date
	private int year, month, day;

	public static AccountPreferenceFragment newInstance(int key) {
		AccountPreferenceFragment fragment = new AccountPreferenceFragment();
		Bundle args = new Bundle();
		args.putInt(TAG_KEY, key);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		presenter = new AccountPreferencePresenter(this);
		initialize();

		presenter.loadPreferences();

		btnDateFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// Use the current date as the default date in the picker
				new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						presenter.updateDobPreference(dayOfMonth, (month + 1), year);
					}
				}, year, month, day).show();
				return false;
			}
		});

		deleteUser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				presenter.deleteUserConfirmation();
				return true;
			}
		});

		changeNameEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				presenter.updateNamePreference((String) newValue);
				return true;
			}
		});
	}

	private void initialize(){
		addPreferencesFromResource(R.xml.acc_preferences);

		deleteUser = findPreference("delete_account_preference");
		changeNameEditText = (EditTextPreference) findPreference("change_name_preference");
		btnDateFilter = findPreference("date_of_birth_preference");
	}


	@Override
	public void returnToLoginActivity() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void setDateOfBirth(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	@Override
	public void showDeleteUserConfirmationDialog() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(getActivity())
						.setTitle("Delete user confirmation")
						.setMessage("Are you sure you want to delete your profile? All your information will be deleted!")
						.setCancelable(false)
						.setPositiveButton("yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								presenter.deleteUser();
							}
						})
						.setNegativeButton("no", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
			}
		});
	}

	@Override
	public void showProgressDialog() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog == null)
					progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Loading preferences, please wait..");
				else
					progressDialog.show();
			}
		});
	}

	@Override
	public void dismissProgressDialog() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
			}
		});
	}

	@Override
	public SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

}

package com.code_dream.almanach.settings;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.code_dream.almanach.settings.account_preference.AccountPreferenceFragment;

import static com.code_dream.almanach.R.xml.preferences;

public class SettingsFragment extends PreferenceFragment implements SettingsContract.FragmentView
{
	private Callback callback;

	private static final String ACCOUNT_PREFERENCE_KEY = "pref_account_settings";

	private SettingsPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(preferences);

		presenter = new SettingsPresenter(this);

		Preference preference = findPreference(ACCOUNT_PREFERENCE_KEY);
		preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				callback.onNestedPreferenceSelected(AccountPreferenceFragment.ACCOUNT_SCREEN_KEY);
				return false;
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if (context instanceof Callback)
			callback = (Callback) context;
		else
			throw new IllegalStateException("Owner must implement Callback interface");
	}

	public interface Callback {
		void onNestedPreferenceSelected(int key);
	}
}

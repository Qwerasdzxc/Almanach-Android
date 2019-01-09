package com.code_dream.almanach.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.StringRes;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.intro.IntroActivity;
import com.code_dream.almanach.home.HomeActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;
import com.code_dream.almanach.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {
	// UI references.
	@BindView(R.id.email) EditText emailEditText;
	@BindView(R.id.password) EditText passwordEditText;
	@BindView(R.id.registerTextView) TextView registerTextView;

	private ProgressDialog progressDialog;
	private LoginPresenter loginPresenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		setupView();

		if (!getSavedEmail().isEmpty())
			emailEditText.setText(getSavedEmail());

		UserInfo.TOKEN = Registry.EMPTY_TOKEN;
		sharedPreferences.edit().putString(Registry.SHARED_PREFS_TOKEN_KEY, Registry.EMPTY_TOKEN).apply();
	}

	@Override
	public void setupPresenter() {
		loginPresenter = new LoginPresenter(this);
	}

	@Override
	public void setupView() {
		ButterKnife.bind(this);

		registerTextView.setText(Html.fromHtml(getString(R.string.register)));
		progressDialog = new ProgressDialog(this);
	}

	//TODO: Remove from production version
	@OnLongClick(R.id.registerTextView)
	public boolean registerLongClick(){
		loginPresenter.onRegisterLongClick();
		return true;
	}

	@OnClick(R.id.email_sign_in_button)
	public void loginClicked(View view) {
		emailEditText.setError(null);
		passwordEditText.setError(null);

		loginPresenter.onLoginButtonClick();
	}

	@OnClick(R.id.registerTextView)
	public void registerClicked(View v) {
		startActivity(new Intent(this, IntroActivity.class));
		this.finish();
	}

	@Override
	public String getEmail() {
		return emailEditText.getText().toString();
	}

	@Override
	public String getPassword() {
		return passwordEditText.getText().toString();
	}

	@Override
	public String getSavedEmail() {
		return sharedPreferences.getString(Registry.SHARED_PREFS_EMAIL_AUTOFILL_KEY, Registry.EMPTY_TOKEN);
	}

	@Override
	public String getSavedFirebaseToken() {
		return sharedPreferences.getString(Registry.SHARED_PREFS_FIREBASE_TOKEN_KEY, Registry.EMPTY_TOKEN);
	}

	@Override
	public void showProgressDialog() {
		progressDialog = ProgressDialog.show(LoginActivity.this, "Authenticating", "Please wait...");
	}

	@Override
	public void showEmptyEmailErrorMessage(@StringRes final int resId) {
		progressDialog.dismiss();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				emailEditText.setError(getString(resId));
			}
		});
	}

	@Override
	public void showEmptyPasswordErrorMessage(@StringRes final int resId) {
		progressDialog.dismiss();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				passwordEditText.setError(getString(resId));
			}
		});
	}

	@Override
	public void showUnsuccessfulAuthenticationMessage(@StringRes final int resId) {
		progressDialog.dismiss();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				emailEditText.setError(getString(resId));
			}
		});
	}

	@Override
	public void startHomeActivity() {
		progressDialog.dismiss();

		startActivity(new Intent(LoginActivity.this, HomeActivity.class));
		this.finish();
	}

	@Override
	public void saveSessionToken(String token) {
		sharedPreferences.edit().putString(Registry.SHARED_PREFS_TOKEN_KEY, token).apply();
	}

	@Override
	public void saveEmailAutoFill(String email) {
		sharedPreferences.edit().putString(Registry.SHARED_PREFS_EMAIL_AUTOFILL_KEY, email).apply();
	}

	@Override
	public void saveId(int id) {
		sharedPreferences.edit().putInt(Registry.SHARED_PREFS_ID_KEY, id).apply();
	}

	@Override
	public void saveFullName(String fullName) {
		sharedPreferences.edit().putString(Registry.SHARED_PREFS_FULL_NAME_KEY, fullName).apply();
	}

	@Override
	public void showTimeoutDialog() {
		progressDialog.dismiss();

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.displayTimeoutDialog(LoginActivity.this);
			}
		});
	}
}
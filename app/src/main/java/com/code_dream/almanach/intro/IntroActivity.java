package com.code_dream.almanach.intro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import com.code_dream.almanach.home.HomeActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.intro.network.IntroInteractor;
import com.code_dream.almanach.intro.slides.intro.SlideIntro;
import com.code_dream.almanach.intro.slides.register.SlideRegister;
import com.code_dream.almanach.intro.slides.school.SlideSchool;
import com.code_dream.almanach.intro.slides.school_location.SlideSchoolLocation;
import com.code_dream.almanach.intro.slides.subjects.SlideSubjects;
import com.code_dream.almanach.login.LoginActivity;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.Utility;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Created by Qwerasdzxc on 01-Jan-17.
 */

public class IntroActivity extends MaterialIntroActivity implements IntroContract.View {

	private IntroPresenter introPresenter;

	private SlideSubjects slideSubjects;
//	private SlideSchool slideSchool;
	private SlideRegister slideRegister;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		introPresenter = new IntroPresenter(this, new IntroInteractor());

		initializeSlides();
	}

	private void initializeSlides()
	{
		enableLastSlideAlphaExitTransition(false);
		getBackButtonTranslationWrapper()
				.setEnterTranslation(new IViewTranslation() {
					@Override
					public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
						view.setAlpha(percentage);
					}
				});

		slideSubjects = new SlideSubjects();
		//slideSchool = new SlideSchool();
		slideRegister = new SlideRegister();

		addSlide(new SlideIntro());
		//addSlide(slideSchool);
		addSlide(slideSubjects);
		addSlide(slideRegister);
	}

	@Override
	public void onRegistrationStart() {
		progressDialog = ProgressDialog.show(this, "Registration in progress", "Please wait...");
	}

	@Override
	public void onBackPressed() {
		introPresenter.onBackClick();
		super.onBackPressed();
	}

	@Override
	public void onFinish() {
		introPresenter.onFinishClick();
	}

	// Preventing lifecycle's finish from running
	// if inputted data is not valid
	@Override
	public void finish() {
		if (introPresenter.canFinish()) {
			startActivity(new Intent(IntroActivity.this, LoginActivity.class));
			super.finish();
		} else if (introPresenter.registrationFinished) {
			super.finish();
		}
	}

	@Override
	public SlideRegister getSlideRegister() {
		return slideRegister;
	}

	@Override
	public String getAllSubjects(){
		return slideRegister.groupSubjects(slideSubjects.getSubjectList());
	}

	@Override
	public String getFullName() {
		return slideRegister.fullNameEditText.getText().toString();
	}

	@Override
	public String getDob() {
		return slideRegister.dobEditText.getText().toString();
	}

	@Override
	public String getEmail() {
		return slideRegister.emailEditText.getText().toString();
	}

	@Override
	public String getPassword() {
		return slideRegister.passEditText.getText().toString();
	}

    @Override
	public String getSavedFirebaseToken() {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(Registry.SHARED_PREFS_FIREBASE_TOKEN_KEY, Registry.EMPTY_TOKEN);
	}

	@Override
	public void showEmailAlreadyExistsMessage() {
		progressDialog.dismiss();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				slideRegister.emailEditText.setError(getString(R.string.error_email_already_exists));
			}
		});
	}

	@Override
	public void startHomeActivity() {
		progressDialog.dismiss();
		startActivity(new Intent(IntroActivity.this, HomeActivity.class));
		IntroActivity.this.finish();
	}

	@Override
	public void showTimeoutDialog() {
		progressDialog.dismiss();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Utility.displayTimeoutDialog(IntroActivity.this);
			}
		});
	}
}

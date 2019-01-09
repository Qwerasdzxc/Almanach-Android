package com.code_dream.almanach.intro.slides.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.code_dream.almanach.R;
import com.code_dream.almanach.utility.Utility;

import org.joda.time.DateTime;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by Qwerasdzxc on 01-Jan-17.
 */

public class SlideRegister extends SlideFragment {

	@BindView(R.id.name_register) public EditText fullNameEditText;
	@BindView(R.id.email_register) public EditText emailEditText;
	@BindView(R.id.dob_register) public EditText dobEditText;
	@BindView(R.id.password_register) public EditText passEditText;
	@BindView(R.id.repeat_password_register) public EditText passRepEditText;
	@BindView(R.id.tosTextView) public TextView tosTextView;

	private boolean isInputValid;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_intro_slide_register, container, false);
		ButterKnife.bind(this, view);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		tosTextView.setText(Html.fromHtml(getString(R.string.terms_of_use)));
	}

	@OnFocusChange(R.id.dob_register)
	public void onDobEditTextFocusChange(boolean hasFocus){
		if (hasFocus){
			DateTime dateTime = DateTime.now();

			new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
					dobEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
				}
			}, dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()).show();
		}
	}

	@OnClick(R.id.dob_register)
	public void onDobEditTextClick(){
		DateTime dateTime = DateTime.now();

		new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				dobEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
			}
		}, dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()).show();
	}


	public boolean canRegister()
	{
		resetEditErrors();

		// Store values at the time of the login attempt.
		String fullName = fullNameEditText.getText().toString();
		String dob = dobEditText.getText().toString();
		String email = emailEditText.getText().toString();
		String password = passEditText.getText().toString();
		String repPassword = passRepEditText.getText().toString();

		// Check for a valid name format.
		if (!Utility.isFullNameValid(fullName)) {
			fullNameEditText.setError(getString(R.string.error_invalid_name));
			isInputValid = false;
		}

		// Check for a valid date of birth format.
		if (!Utility.isDobValid(dob)) {
			dobEditText.setError(getString(R.string.error_invalid_dob));
			isInputValid = false;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			emailEditText.setError(getString(R.string.error_field_required));
			isInputValid = false;
		}
		else if (!Utility.isEmailValid(email)) {
			emailEditText.setError(getString(R.string.error_invalid_email));
			isInputValid = false;
		}

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !Utility.isPasswordValid(password)) {
			passEditText.setError(getString(R.string.error_invalid_password));
			isInputValid = false;
		}

		// Check if both inputted passwords match.
		if (!password.equals(repPassword)) {
			passRepEditText.setError(getString(R.string.error_incorrect_repeated_password));
			isInputValid = false;
		}

		return isInputValid;
	}

	public String groupSubjects(String subjectsList)
	{
		String[] subjects = subjectsList.split("[\\r\\n]+");
		StringBuilder stringBuilder = new StringBuilder();

		for (String subject : subjects)
			stringBuilder.append(subject.trim()).append("|");

		return stringBuilder.toString();
	}

	private void resetEditErrors()
	{
		// Reset errors.
		fullNameEditText.setError(null);
		dobEditText.setError(null);
		emailEditText.setError(null);
		passEditText.setError(null);
		passRepEditText.setError(null);

		isInputValid = true;
	}

	@Override
	public int backgroundColor() {
		return R.color.colorPrimary;
	}

	@Override
	public int buttonsColor() {
		return R.color.transparent;
	}

	@Override
	public boolean canMoveFurther() {
		return true;
	}

	@Override
	public String cantMoveFurtherErrorMessage() {
		return "Error message";
	}

}

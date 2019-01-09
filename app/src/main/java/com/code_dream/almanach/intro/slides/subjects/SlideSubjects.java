package com.code_dream.almanach.intro.slides.subjects;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.code_dream.almanach.R;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by Qwerasdzxc on 01-Jan-17.
 */

public class SlideSubjects extends SlideFragment {

	private EditText subjectListEditText;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_intro_subjects_input, container, false);
		subjectListEditText = (EditText) view.findViewById(R.id.edit_subjects_list);
		return view;
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
		return !subjectListEditText.getText().toString().equals("");
	}

	@Override
	public String cantMoveFurtherErrorMessage() {
		return "Fill in the subjects field!";
	}

	public  String getSubjectList()
	{
		return subjectListEditText.getText().toString();
	}

}

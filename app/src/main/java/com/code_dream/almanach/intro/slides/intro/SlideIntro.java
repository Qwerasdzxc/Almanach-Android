package com.code_dream.almanach.intro.slides.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code_dream.almanach.R;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by Qwerasdzxc on 01-Jan-17.
 */

public class SlideIntro extends SlideFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_intro_slide, container, false);
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

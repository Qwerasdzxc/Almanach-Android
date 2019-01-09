package com.code_dream.almanach.school_profile;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.utility.Registry;

import net.steamcrafted.loadtoast.LoadToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SchoolProfileActivity extends BaseActivity implements SchoolProfileContract.View{

    @BindView(R.id.school_profile_toolbar) Toolbar toolbar;
    @BindView(R.id.school_profile_set_school_tv) TextView setSchoolTv;

    @InjectExtra("school") String school;

    private LoadToast loadToast;

    private SchoolProfilePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_school_profile);
        setupView();
    }

    @Override
    public void setupPresenter() {
        presenter = new SchoolProfilePresenter(this);
    }

    @Override
    public void setupView(){
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(school);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadToast = new LoadToast(this);
    }

    @OnClick(R.id.school_profile_set_school_tv)
    public void onSetSchoolClick(){
        presenter.onSelectSchool();
    }

    @Override
    public String getSchoolName() {
        return school;
    }

    @Override
    public void showLoadingToast() {
        loadToast.setText("Setting school...");
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void dismissLoadingToast() {
        loadToast.success();

        setSchoolTv.setText("This is your school");
    }

    @Override
    public void dismissLoadingToastWithError() {
        loadToast.error();
    }

}

package com.code_dream.almanach.add_edit_post;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.add_edit_post.network.AddEditPostInteractor;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.network.InternetConnectionRetryCallback;
import com.code_dream.almanach.utility.Registry;

import net.steamcrafted.loadtoast.LoadToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditPostActivity extends BaseActivity implements AddEditPostContract.View{

    @BindView(R.id.add_post_post_button) TextView postButtonTextView;
    @BindView(R.id.add_post_title) TextView postTitleTextView;
    @BindView(R.id.add_post_content) EditText contentText;
    @BindView(R.id.add_post_image) ImageView postImage;
    @BindView(R.id.add_post_activity_toolbar) Toolbar toolbar;

    @InjectExtra("post_type") PostType selectedPostType;

    LoadToast loadToast;

    AddEditPostPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_post);
        setupView();
    }

    @Override
    public void setupPresenter() {
        presenter = new AddEditPostPresenter(this, new AddEditPostInteractor());
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadToast = new LoadToast(this);

        presenter.onInitialize();
    }

    @OnClick(R.id.add_post_post_button)
    public void onPostClick(View view) {
        presenter.onPostClick();
    }

    @Override
    public void initializeNewPostActivity(){
        postImage.setImageURI(getImageUri());
    }

    @Override
    public void initializeEditActivity(){
        contentText.setText(getIntent().getStringExtra("post_content"));
        contentText.setHint("EDIT your original post here");
        postButtonTextView.setText("EDIT");
        postTitleTextView.setText("Edit post");
    }

    @Override
    public void setPostButtonClickable(boolean clickable) {
        postButtonTextView.setClickable(clickable);
    }

    @Override
    public void showLoadingToast() {
        loadToast.setText("Uploading post...");
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void dismissLoadToastWithSuccess() {
        loadToast.success();
    }

    @Override
    public void dismissLoadToastWithError() {
        loadToast.error();
    }

    @Override
    public void showInvalidContentError() {
        contentText.setError("Content needs to be 6 characters long.");
    }

    @Override
    public void showInternetConnectionProblemSnackbar() {
        showInternetConnectionErrorSnackbar(new InternetConnectionRetryCallback() {
            @Override
            public void onRetry() {
                presenter.onRetryConnection();
            }
        });
    }

    @Override
    public int getOriginalPostId(){
        return getIntent().getIntExtra("post_id", Registry.DEFAULT_POST_ID);
    }

    @Override
    public PostType getPostType() {
        return selectedPostType;
    }

    @Override
    public String getContent() {
        return contentText.getText().toString();
    }

    @Override
    public Uri getImageUri() {
        return (Uri) getIntent().getParcelableExtra("image_uri");
    }

    @Override
    public ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    @Override
    public void finishActivity(SchoolPost schoolPost) {
        setResult(Activity.RESULT_OK, new Intent().putExtra("school_post", schoolPost));
        this.finish();
    }
}

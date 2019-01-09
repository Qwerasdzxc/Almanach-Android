package com.code_dream.almanach.post_details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.ReactionType;
import com.code_dream.almanach.post_details.network.PostDetailsInteractor;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;
import com.code_dream.almanach.utility.Utility;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Qwerasdzxc on 3/11/17.
 */

public class PostDetailsActivity extends BaseActivity implements PostDetailsContract.View,
                                                                 RecyclerViewAdapter.ContextMenuClickListener,
                                                                 RecyclerViewAdapter.ReactionClickListener {

    @BindView(R.id.post_details_activity_toolbar) Toolbar toolbar;
    @BindView(R.id.post_details_comment_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.post_details_scroll_view) NestedScrollView scrollView;
    @BindView(R.id.post_details_school_name) TextView postSchoolNameTV;
    @BindView(R.id.post_details_date) TextView postDateTV;
    @BindView(R.id.post_details_user_name) TextView postUserNameTV;
    @BindView(R.id.post_details_content) TextView postContentTV;
    @BindView(R.id.post_details_image) ImageView postImageIV;
    @BindView(R.id.post_details_upvote_tv) TextView postUpvoteTV;
    @BindView(R.id.post_details_downvote_tv) TextView postDownvoteTV;
    @BindView(R.id.post_details_reaction_counter) TextView postReactionCounter;
    @BindView(R.id.post_details_reaction_separator) View postReactionSeparator;
    @BindView(R.id.post_details_new_comment_user_name) TextView commentUserNameTV;
    @BindView(R.id.post_details_new_comment_content) EditText commentContentET;

    @InjectExtra("school_post") SchoolPost selectedSchoolPost;

    RecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    LoadToast loadToast;

    ArrayList<Object> recyclerViewContentList = new ArrayList<>();

    PostDetailsPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_details);
        setupView();

        presenter.loadAllComments(getSchoolPost().getId());
    }

    @Override
    public void setupPresenter() {
        presenter = new PostDetailsPresenter(this, new PostDetailsInteractor());
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Registry.TOOLBAR_NO_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadToast = new LoadToast(this);

        postSchoolNameTV.setText(getSchoolPost().getSchool());
        postDateTV.setText(getSchoolPost().getPostDate());
        postUserNameTV.setText(getSchoolPost().getUser());
        postContentTV.setText(getSchoolPost().getContent());

        commentUserNameTV.setText(UserInfo.CURRENT_USER.getName());

        if (!getSchoolPost().getImageUrl().isEmpty()){
            byte[] imageByteArray = getIntent().getByteArrayExtra("school_post_image");
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

            postImageIV.setVisibility(View.VISIBLE);
            postImageIV.setImageBitmap(imageBitmap);
        }

        presenter.setupReactions();

        layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter(recyclerViewContentList);
        adapter.setContextMenuClickListener(this);
        adapter.setReactionClickListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, new Intent().putExtra("school_post", getSchoolPost()));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_OK, new Intent().putExtra("school_post", getSchoolPost()));
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.post_details_upvote_tv)
    public void onPostUpvoteClick(){
        presenter.onPostUpvoteClick(getSchoolPost());
    }

    @OnClick(R.id.post_details_downvote_tv)
    public void onPostDownvoteClick(){
        presenter.onPostDownvoteClick(getSchoolPost());
    }

    @Override
    public void onReactionClick(ReactionType reactionType, Object item) {
        if (reactionType == ReactionType.LIKE)
            presenter.onCommentUpvoteClick((SchoolPostComment) item);
        else if (reactionType == ReactionType.DISLIKE)
            presenter.onCommentDownvoteClick((SchoolPostComment) item);
    }

    @Override
    public void onContextMenuClick(ImageView contextMenuIV, Object item) {
        presenter.showContextMenu(contextMenuIV, (SchoolPostComment) item);
    }

    @OnClick(R.id.post_details_new_comment_submit_tv)
    public void onCommentSubmitClick(){
        presenter.onCommentSubmitClick(commentContentET.getText().toString());
    }

    @Override
    public void showPostOwnerContextMenu(ImageView imgContextMenu, final SchoolPostComment schoolPostComment) {
        PopupMenu popup = new PopupMenu(this, imgContextMenu);
        popup.inflate(R.menu.post_context_menu_mine);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_post:
                        presenter.editComment(schoolPostComment);
                        break;
                    case R.id.action_delete_post:
                        presenter.deleteComment(schoolPostComment);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void showPostOthersContextMenu(ImageView imgContextMenu, final SchoolPostComment schoolPostComment) {
        PopupMenu popup = new PopupMenu(this, imgContextMenu);
        popup.inflate(R.menu.post_context_menu_others);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_report_post:
                        presenter.reportComment(schoolPostComment);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public SchoolPost getSchoolPost() {
        return selectedSchoolPost;
    }

    @Override
    public void updateReactionCount(int reactionCount) {
        postReactionCounter.setText(String.valueOf(reactionCount));
        displayReactionCount(reactionCount != 0);
    }

    @Override
    public void displayReactionCount(boolean display) {
        if (display){
            postReactionCounter.setVisibility(View.VISIBLE);
            postReactionSeparator.setVisibility(View.GONE);
        } else {
            postReactionSeparator.setVisibility(View.VISIBLE);
            postReactionCounter.setVisibility(View.GONE);
        }
    }

    @Override
    public void boldReaction(ReactionType reactionType) {
        if (reactionType == ReactionType.LIKE)
            Utility.modifyTextView(postUpvoteTV, true, 18, R.color.colorPrimary);
        else
            Utility.modifyTextView(postDownvoteTV, true, 18, R.color.color_success);
    }

    @Override
    public void showLoadingToast(String text) {
        loadToast.setText(text);
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void dismissLoadToastSuccessfully() {
        loadToast.success();
    }

    @Override
    public void dismissLoadToastWithError() {
        loadToast.error();
    }

    @Override
    public void clearReactionBolding(ReactionType reactionType) {
        Utility.clearTextViewModifications(reactionType == ReactionType.LIKE ? postUpvoteTV : postDownvoteTV);
    }

    @Override
    public void updateRecyclerView(ArrayList<SchoolPostComment> schoolPostComments) {
        recyclerViewContentList.addAll(schoolPostComments);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addNewCommentToRecyclerView(SchoolPostComment schoolPostComment) {
        commentContentET.setText("");
        recyclerViewContentList.add(schoolPostComment);

        //TODO: Optimize
        adapter.notifyDataSetChanged();

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void removeCommentFromRecyclerView(SchoolPostComment schoolPostComment) {
        int itemIndex = recyclerViewContentList.indexOf(schoolPostComment);
        recyclerViewContentList.remove(itemIndex);
        adapter.notifyItemRemoved(itemIndex);
    }

    @Override
    public int getLastCommentId() {
        return  ! recyclerViewContentList.isEmpty()
                ? ((SchoolPostComment) recyclerViewContentList.get(recyclerViewContentList.size() - 1)).getCommentId()
                : Registry.DEFAULT_POST_ID;
    }
}

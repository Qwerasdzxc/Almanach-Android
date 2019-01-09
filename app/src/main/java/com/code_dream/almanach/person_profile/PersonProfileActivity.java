package com.code_dream.almanach.person_profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.add_edit_post.AddEditPostActivity;
import com.code_dream.almanach.add_edit_post.PostType;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.ReactionType;
import com.code_dream.almanach.person_profile.models.PersonProfileEmptyDatasetItem;
import com.code_dream.almanach.person_profile.models.PersonProfileHeaderItem;
import com.code_dream.almanach.utility.Registry;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.ViewPagerAdapter;
import com.code_dream.almanach.chat.ChatActivity;
import com.code_dream.almanach.chat_tab.FragmentChat;
import com.code_dream.almanach.middle_drawer.FragmentMiddleDrawer;
import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.PersonSearchResult;
import com.code_dream.almanach.network.services.MessageService;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonProfileActivity extends BaseActivity implements PersonProfileContract.View,
        RecyclerViewAdapter.SchoolPostClickListener,
        RecyclerViewAdapter.ReactionClickListener,
        RecyclerViewAdapter.FriendRequestClickListener,
        RecyclerViewAdapter.ContextMenuClickListener {

    @BindView(R.id.person_profile_nested_scrollview) NestedScrollView nestedScrollView;
    @BindView(R.id.person_profile_toolbar) Toolbar toolbar;
    @BindView(R.id.person_profile_parent_menu) LinearLayout parentView;
    @BindView(R.id.person_profile_progress_bar) ProgressBar progressBar;
    @BindView(R.id.person_profile_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.person_profile_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    @InjectExtra("personSearchResult")
    PersonSearchResult personSearchResult;

    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter adapter;

    LoadToast loadToast;

    ArrayList<Object> schoolPosts = new ArrayList<>();

    PersonProfilePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_person_profile);
        setupView();

        presenter.loadPersonProfile();
    }

    @Override
    public void setupPresenter() {
        presenter = new PersonProfilePresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        loadToast = new LoadToast(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(personSearchResult.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView.setFillViewport(true);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RecyclerViewAdapter(schoolPosts);

        adapter.setSchoolPostClickListener(this);
        adapter.setReactionClickListener(this);
        adapter.setFriendRequestClickListener(this);
        adapter.setContextMenuClickListener(this);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary));

        adapter.setLoadMoreListener(new RecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        presenter.loadMorePosts();
                    }
                });
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrolled();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshAllPosts();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void startChatActivity(ChatRoom chatRoom) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_room", chatRoom);

        Intent serviceIntent = new Intent(this, MessageService.class);

        startService(serviceIntent);
        startActivity(intent);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            parentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            parentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSwipeRefreshLayoutRefreshing(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    @Override
    public void showPosts(ArrayList<Object> schoolPosts) {
        this.schoolPosts.clear();
        this.schoolPosts.add(presenter.getPersonProfileHeaderItem());
        this.schoolPosts.addAll(schoolPosts);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void addNewSchoolPostsToRecyclerView(ArrayList<Object> schoolPosts) {
        this.schoolPosts.addAll(schoolPosts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addProgressBarItemToRecyclerView() {
        schoolPosts.add(new ProgressBar(this));

        adapter.notifyItemInserted(schoolPosts.size() - 1);
    }

    @Override
    public void removeProgressBarItemFromRecyclerView() {
        if (!schoolPosts.isEmpty()) {
            schoolPosts.remove(schoolPosts.size() - 1);
            adapter.notifyItemRemoved(schoolPosts.size());
        }
    }

    @Override
    public int getPersonId() {
        return personSearchResult.getId();
    }

    @Override
    public void onContextMenuClick(ImageView contextMenuIV, Object item) {
        presenter.showContextMenu(contextMenuIV, (SchoolPost) item);
    }

    @Override
    public void showPostOwnerContextMenu(ImageView imgContextMenu, final SchoolPost schoolPost) {
        PopupMenu popup = new PopupMenu(this, imgContextMenu);
        popup.inflate(R.menu.post_context_menu_mine);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_post:
                        presenter.editPost(schoolPost, schoolPosts.indexOf(schoolPost));
                        break;
                    case R.id.action_delete_post:
                        presenter.deletePost(schoolPost);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void showPostOthersContextMenu(ImageView imgContextMenu, final SchoolPost schoolPost) {
        PopupMenu popup = new PopupMenu(this, imgContextMenu);
        popup.inflate(R.menu.post_context_menu_others);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_report_post:
                        presenter.reportPost(schoolPost);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void startAddEditActivity(int id, String content) {
        Intent addEditPostIntent = new Intent(this, AddEditPostActivity.class);
        addEditPostIntent.putExtra("post_type", PostType.NEW);
        startActivityForResult(addEditPostIntent, Registry.OPEN_ADD_POST_ACTIVITY);
    }

    @Override
    public void showPostReportingToast() {
        loadToast.setText("Reporting post...");
        loadToast.setTranslationY(Registry.LOAD_TOAST_Y_OFFSET);
        loadToast.show();
    }

    @Override
    public void showPostDeletingToast() {
        loadToast.setText("Deleting post...");
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
    public void removePostFromList(SchoolPost selectedSchoolPost) {
        int postPosition = schoolPosts.indexOf(selectedSchoolPost);
        schoolPosts.remove(postPosition);
        adapter.notifyItemRemoved(postPosition);
        adapter.notifyItemRangeChanged(postPosition, schoolPosts.size());
    }

    @Override
    public void onFriendRequestClick() {
        presenter.onAddFriendClick();
    }

    @Override
    public void onReactionClick(ReactionType reactionType, Object item) {
        if (reactionType == ReactionType.LIKE)
            presenter.onUpvoteClick((SchoolPost) item);
        else if (reactionType == ReactionType.DISLIKE)
            presenter.onDownvoteClick((SchoolPost) item);
    }

    @Override
    public void onSchoolPostClick(ImageView imageView, int position) {

    }

    @Override
    public void setMoreAdapterDataAvailable() {
        adapter.setMoreDataAvailable(true);
    }

    @Override
    public void notifyAdapterNoMorePostsToLoad() {
        adapter.setMoreDataAvailable(false);
        adapter.notifyDataChanged();
    }

    @Override
    public void addPersonHeaderItem(PersonProfileHeaderItem item) {
        schoolPosts.add(0, item);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void updatePersonHeaderItem(Person.FriendType friendType) {
        ((PersonProfileHeaderItem) schoolPosts.get(0)).setFriendType(friendType);
        adapter.notifyItemChanged(0);
    }

    @Override
    public void addEmptyDatasetItem() {
        schoolPosts.add(1, new PersonProfileEmptyDatasetItem());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemInserted(1);
            }
        });
    }

    @Override
    public int layoutManagerGetItemCount() {
        return linearLayoutManager.getItemCount();
    }

    @Override
    public int layoutManagerFindLastVisibleItemPosition() {
        return linearLayoutManager.findLastVisibleItemPosition();
    }

    @Override
    public int getLastPostId() {
        return ((SchoolPost) schoolPosts.get(schoolPosts.size() - 1)).getId();
    }

    @Override
    public boolean isRecyclerViewEmpty() {
        return schoolPosts.size() <= 1;
    }
}

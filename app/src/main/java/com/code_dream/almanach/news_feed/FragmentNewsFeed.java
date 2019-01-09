package com.code_dream.almanach.news_feed;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.add_edit_post.AddEditPostActivity;
import com.code_dream.almanach.add_edit_post.PostType;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.post_details.PostDetailsActivity;
import com.code_dream.almanach.utility.Registry;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment implements NewsFeedContract.View,
														  RecyclerViewAdapter.SchoolPostClickListener,
														  RecyclerViewAdapter.ReactionClickListener,
													      RecyclerViewAdapter.AddPostClickListener,
														  RecyclerViewAdapter.AddPhotoClickListener,
		                                                  RecyclerViewAdapter.ContextMenuClickListener {

	@BindView(R.id.news_feed_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
	@BindView(R.id.news_feed_recycler_view) RecyclerView recyclerView;
	@BindView(R.id.news_feed_progress_bar) ProgressBar progressBar;
	@BindView(R.id.news_feed_empty_data_set_view) LinearLayout emptyDataSetView;
	@BindView(R.id.bottom_sheet) View bottomSheet;
	@BindView(R.id.tint_view) View tintView;

    // Image URI of the picture we take from the Camera Activity
	Uri cameraImageUri;

	RecyclerViewAdapter adapter;
	LinearLayoutManager layoutManager;
	LoadToast loadToast;
	BottomSheetBehavior bottomSheetBehavior;

	ArrayList<Object> schoolPostsArrayList = new ArrayList<>();

	NewsFeedPresenter presenter;

	private boolean oneTimePresenterInit = true;

	public FragmentNewsFeed() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
		ButterKnife.bind(this, view);

		return view;
	}

	@Override
	public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// TODO: Handle orientation changes
		if (oneTimePresenterInit) {
			presenter = new NewsFeedPresenter(this);
			oneTimePresenterInit = false;
		}

		initialize();

		presenter.loadAllPostsFirstTime();
	}

    @Override
    public void onResume() {
        if (presenter == null)
            presenter = new NewsFeedPresenter(this);
	    super.onResume();
    }

    private void initialize() {
		layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		loadToast = new LoadToast(getActivity());

		adapter = new RecyclerViewAdapter(schoolPostsArrayList);

		adapter.setSchoolPostClickListener(this);
		adapter.setReactionClickListener(this);
		adapter.setAddPostClickListener(this);
		adapter.setAddPhotoClickListener(this);
		adapter.setContextMenuClickListener(this);

		bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

		tintView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                dismissBottomSheet();
			}
		});

		swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent),
				ContextCompat.getColor(getActivity(), R.color.colorPrimary));

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
				presenter.refreshAllPosts(false);
			}
		});

		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onSchoolPostClick(ImageView imageView, int position) {
		if (imageView.getVisibility() == View.VISIBLE) {

			imageView.buildDrawingCache();

			Bitmap b = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			imageView.layout(0, 0, imageView.getWidth(), imageView.getHeight());
			imageView.draw(c);

			presenter.onPostClick((SchoolPost) schoolPostsArrayList.get(position), position, b);
		} else {
			presenter.onPostClick((SchoolPost) schoolPostsArrayList.get(position), position, null);
		}
	}

	@Override
	public void onContextMenuClick(ImageView imgContextMenu, Object item) {
		presenter.showContextMenu(imgContextMenu, (SchoolPost) item);
	}

	@Override
	public void onAddPostClick() {
		presenter.onAddNewPostClick();
	}

	@Override
	public void onAddPhotoClick() {
		presenter.onAddPhotoClick();
	}

	@Override
	public void onReactionClick(ReactionType reactionType, Object item) {
		if (reactionType == ReactionType.LIKE)
			presenter.onUpvoteClick((SchoolPost) item);
		else if (reactionType == ReactionType.DISLIKE)
			presenter.onDownvoteClick((SchoolPost) item);
	}

	@OnClick(R.id.news_feed_bottom_sheet_take_picture_tv)
	public void onBottomSheetTakePictureClick(){
		presenter.onNewPictureRequested();
	}

	@OnClick(R.id.news_feed_bottom_sheet_gallery_tv)
	public void onBottomSheetGalleryPictureClick(){
		presenter.onGalleryPictureRequested();
	}

	@Override
	public void showPostOwnerContextMenu(ImageView imgContextMenu, final SchoolPost schoolPost) {
		PopupMenu popup = new PopupMenu(getActivity(), imgContextMenu);
		popup.inflate(R.menu.post_context_menu_mine);

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_edit_post:
						presenter.editPost(schoolPost, schoolPostsArrayList.indexOf(schoolPost));
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
		PopupMenu popup = new PopupMenu(getActivity(), imgContextMenu);
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
	public void addNewSchoolPostsToRecyclerView(final ArrayList<Object> newSchoolPosts) {
		for (int i = 0; i < newSchoolPosts.size(); i ++) {
			schoolPostsArrayList.add(newSchoolPosts.get(i));
			adapter.notifyItemInserted(schoolPostsArrayList.size());
		}

		adapter.notifyDataChanged();
	}

	@Override
	public void notifyAdapterNoMorePostsToLoad() {
		adapter.setMoreDataAvailable(false);
		adapter.notifyDataChanged();
	}

	@Override
	public void addProgressBarItemToRecyclerView() {
		schoolPostsArrayList.add(new ProgressBar(getActivity()));

		adapter.notifyItemInserted(schoolPostsArrayList.size() - 1);
	}

	@Override
	public void removeProgressBarItemFromRecyclerView() {
		if (!schoolPostsArrayList.isEmpty()) {
			schoolPostsArrayList.remove(schoolPostsArrayList.size() - 1);
			adapter.notifyItemRemoved(schoolPostsArrayList.size());
		}
	}

	@Override
	public void setSwipeRefreshLayoutRefreshing(final boolean refreshing) {
		swipeRefreshLayout.setRefreshing(refreshing);
	}

	@Override
	public void swapSchoolPostsInAdapter(final ArrayList<Object> schoolPosts) {
		adapter.swap(schoolPosts, true);
	}

	@Override
	public void setProgressBarVisibility(final int visibility) {
		progressBar.setVisibility(visibility);
	}

	@Override
	public void showBottomSheet() {
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		tintView.setVisibility(View.VISIBLE);
	}

	@Override
	public void dismissBottomSheet() {
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
		tintView.setVisibility(View.GONE);
	}

	@Override
	public void showEmptyDataSetView(boolean show) {
		if (show) {
			emptyDataSetView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else {
			emptyDataSetView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onNewPhotoRequested() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			boolean hasCameraPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
			boolean hasExternalStoragePermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

			if (!hasCameraPermission && !hasExternalStoragePermission)
				requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Registry.REQUEST_CAMERA_AND_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
			else if (!hasCameraPermission)
				requestPermissions(new String[]{Manifest.permission.CAMERA}, Registry.REQUEST_CAMERA_PERMISSION_CODE);
			else if (!hasExternalStoragePermission)
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Registry.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
			else
				openCameraActivity();
		} else
			openCameraActivity();
	}

	@Override
	public void onImageFromGalleryRequested() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			boolean hasExternalStoragePermission = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

			if (!hasExternalStoragePermission)
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Registry.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_CODE);
			else
				openImageSelectionActivity();
		} else
			openImageSelectionActivity();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		boolean firstPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

		if (requestCode == Registry.REQUEST_CAMERA_AND_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
			if (firstPermissionGranted && grantResults[1] == PackageManager.PERMISSION_GRANTED)
				openCameraActivity();
			else
				dismissBottomSheet();
		else if (requestCode == Registry.REQUEST_CAMERA_PERMISSION_CODE)
			if (firstPermissionGranted)
				openCameraActivity();
			else
				dismissBottomSheet();
		else if (requestCode == Registry.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
			if (firstPermissionGranted)
				openCameraActivity();
			else
				dismissBottomSheet();
		else if (requestCode == Registry.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_CODE)
			if (firstPermissionGranted)
				presenter.onGalleryPictureRequested();
			else
				dismissBottomSheet();
	}

	@Override
	public void startAddEditActivity() {
		Intent addEditPostIntent = new Intent(getActivity(), AddEditPostActivity.class);
		addEditPostIntent.putExtra("post_type", PostType.NEW);
		startActivityForResult(addEditPostIntent, Registry.OPEN_ADD_POST_ACTIVITY);
	}

	@Override
	public void startAddEditActivity(Uri imageUri) {
		this.dismissBottomSheet();

		Intent addEditPostIntent = new Intent(getActivity(), AddEditPostActivity.class);
		addEditPostIntent.putExtra("post_type", PostType.NEW);
		addEditPostIntent.putExtra("image_uri", imageUri);
		startActivityForResult(addEditPostIntent, Registry.OPEN_ADD_POST_ACTIVITY);
	}

	@Override
	public void startAddEditActivity(int postId, String postContent) {
		Intent addEditPostIntent = new Intent(getActivity(), AddEditPostActivity.class);
		addEditPostIntent.putExtra("post_type", PostType.EDIT);
		addEditPostIntent.putExtra("post_id", postId);
		addEditPostIntent.putExtra("post_content", postContent);
		startActivityForResult(addEditPostIntent, Registry.OPEN_EDIT_POST_ACTIVITY);
	}

	@Override
	public void openPostDetailsActivity(SchoolPost selectedSchoolPost, byte[] selectedPostImage) {
		startActivityForResult(new Intent(getContext(), PostDetailsActivity.class)
							   .putExtra("school_post", selectedSchoolPost).putExtra("school_post_image", selectedPostImage), Registry.OPEN_POST_DETAILS_ACTIVITY);
	}

	@Override
	public void openCameraActivity() {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "image_title");
		values.put(MediaStore.Images.Media.DESCRIPTION, "image_description");
		cameraImageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
		startActivityForResult(intent, Registry.OPEN_CAMERA_ACTIVITY);
	}

	@Override
	public void openImageSelectionActivity() {
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Image for uploading");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, Registry.OPEN_IMAGE_SELECTION_ACTIVITY);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK)
			if (requestCode == Registry.OPEN_CAMERA_ACTIVITY)
				startAddEditActivity(cameraImageUri);
			else if (requestCode == Registry.OPEN_IMAGE_SELECTION_ACTIVITY )
				startAddEditActivity(data.getData());
			else if (requestCode == Registry.OPEN_POST_DETAILS_ACTIVITY)
				presenter.updatePostDetails((SchoolPost) data.getParcelableExtra("school_post"));
			else if (requestCode == Registry.OPEN_ADD_POST_ACTIVITY)
				presenter.addCreatedPost((SchoolPost) data.getParcelableExtra("school_post"));
			else if (requestCode == Registry.OPEN_EDIT_POST_ACTIVITY)
				presenter.updatePostDetails((SchoolPost) data.getParcelableExtra("school_post"));
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
	public void addCreatedPost(SchoolPost createdSchoolPost) {
		schoolPostsArrayList.add(1, createdSchoolPost);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void updatePostDetails(SchoolPost updatedSchoolPost, int originalPostPosition) {
		schoolPostsArrayList.set(originalPostPosition, updatedSchoolPost);
		adapter.notifyItemChanged(originalPostPosition);
	}

	@Override
	public void removePostFromList(final SchoolPost schoolPostToRemove) {
		int postPosition = schoolPostsArrayList.indexOf(schoolPostToRemove);
		schoolPostsArrayList.remove(postPosition);
		adapter.notifyItemRemoved(postPosition);
		adapter.notifyItemRangeChanged(postPosition, schoolPostsArrayList.size());
	}

	@Override
	public boolean isAdapterBinding() {
		return adapter.isBinding;
	}

	@Override
	public void setMoreAdapterDataAvailable() {
		adapter.setMoreDataAvailable(true);
	}

	@Override
	public int layoutManagerGetItemCount() {
		return layoutManager.getItemCount();
	}

	@Override
	public int layoutManagerFindLastVisibleItemPosition() {
		return layoutManager.findLastVisibleItemPosition();
	}

	@Override
	public int getLastPostId() {
		return ((SchoolPost) schoolPostsArrayList.get(schoolPostsArrayList.size() - 1)).getId();
	}

	@Override
	public int getPostId(SchoolPost schoolPost)
	{
		return schoolPost.getId();
	}

	@Override
	public boolean isRecyclerViewEmpty() {
		return schoolPostsArrayList.size() <= 1;
	}

    @Override
	public void showServerErrorSnackbar() {
		final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), Registry.NO_INTERNET_CONNECTION_OFFLINE_MODE, Snackbar.LENGTH_LONG);
		snackbar.setAction("RECONNECT", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.refreshAllPosts(true);
				snackbar.dismiss();
			}
		}).show();
	}
}

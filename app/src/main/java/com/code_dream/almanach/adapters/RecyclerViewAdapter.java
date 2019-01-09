package com.code_dream.almanach.adapters;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.code_dream.almanach.calendar.models.CalendarEventType;
import com.code_dream.almanach.R;
import com.code_dream.almanach.add_edit_post.AddEditPostActivity;
import com.code_dream.almanach.calendar.models.CalendarEventTypeSmall;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.adapters.item_touch_helper.ItemTouchHelperAdapter;
import com.code_dream.almanach.adapters.item_touch_helper.ItemTouchHelperViewHolder;
import com.code_dream.almanach.adapters.item_touch_helper.OnStartDragListener;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.GenericListTitleHeader;
import com.code_dream.almanach.models.Message;
import com.code_dream.almanach.models.Notification;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.PersonSearchResult;
import com.code_dream.almanach.models.School;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.news_feed.ReactionType;
import com.code_dream.almanach.person_profile.models.PersonProfileEmptyDatasetItem;
import com.code_dream.almanach.person_profile.models.PersonProfileHeaderItem;
import com.code_dream.almanach.post_details.SchoolPostComment;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.timetable.models.NewTimetableItem;
import com.code_dream.almanach.timetable.models.TimetableListItem;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;
import com.code_dream.almanach.utility.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Qwerasdzxc on 26-Dec-16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

	public interface ContextMenuClickListener {
		void onContextMenuClick(ImageView contextMenuIV, Object item);
	}

	public interface ReactionClickListener {
		void onReactionClick(ReactionType reactionType, Object item);
	}

	public interface FriendRequestClickListener {
		void onFriendRequestClick();
	}

	public interface AddPostClickListener {
		void onAddPostClick();
	}

	public interface AddPhotoClickListener {
		void onAddPhotoClick();
	}

	public interface EventTypeSelectionClickListener {
		void onAddNewEventClick();
		void onEventTypeClick(EventType selectedEventType);
	}

	public interface ItemClickListener {
		void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position);
	}

	public interface SchoolPostClickListener {
		void onSchoolPostClick(ImageView imageView, int position);
	}

	public interface OnLoadMoreListener {
		void onLoadMore();
	}

	public interface OnItemRemovedListener {
		void onItemRemoved(int pos);
	}

	private ContextMenuClickListener contextMenuClickListener;
	private ReactionClickListener reactionClickListener;
	private FriendRequestClickListener friendRequestClickListener;
	private EventTypeSelectionClickListener eventTypeSelectionClickListener;
	private AddPostClickListener addPostClickListener;
	private AddPhotoClickListener addPhotoClickListener;
	private ItemClickListener itemClickListener;
	private SchoolPostClickListener schoolPostClickListener;
	private OnLoadMoreListener loadMoreListener;
	private OnStartDragListener startDragListener;
	private OnItemRemovedListener onItemRemovedListener;

	private boolean isLoading = false, isMoreDataAvailable = true;
	public boolean isBinding;

	private final int SEARCH_RESULT_SCHOOL_ITEM = 0;
	private final int SEARCH_RESULT_PERSON_ITEM = 1;
	private final int SCHOOL_POST_ITEM = 2;
	private final int SCHOOL_POST_NEW_POST_ITEM = 3;
	private final int SCHOOL_POST_COMMENT_ITEM = 4;
	private final int PROGRESS_BAR_ITEM = 5;
	private final int CALENDAR_EVENT_TYPE_SELECTION_SMALL_ITEM = 6;
	private final int CALENDAR_EVENT_TYPE_SELECTION_BIG_ITEM = 7;
	private final int CALENDAR_LOADED_EVENTS_ITEM = 8;
	private final int MIDDLE_DRAWER_ITEM = 9;
	private final int MIDDLE_DRAWER_TITLE_HEADER_ITEM = 10;
	private final int SUBJECT_TIMETABLE_ITEM = 11;
	private final int NEW_TIMETABLE_ITEM_ITEM = 12;
	private final int SUBJECT_LIST_ITEM = 13;
	private final int CHAT_RECEIVED_MESSAGE_ITEM = 14;
	private final int CHAT_SENT_MESSAGE_ITEM = 15;
	private final int CHAT_ROOM_ITEM = 16;
	private final int PERSON_PROFILE_HEADER_ITEM = 17;
	private final int PERSON_PROFILE_EMPTY_DATASET_ITEM = 18;
	private final int NOTIFICATION_ITEM = 19;

	private String searchString;

	private ArrayList<Object> contentArrayList = new ArrayList<>();

	public RecyclerViewAdapter(ArrayList<Object> contentArrayList) {
		this.contentArrayList = contentArrayList;
	}

	public void setContextMenuClickListener(ContextMenuClickListener contextMenuClickListener){
		this.contextMenuClickListener = contextMenuClickListener;
	}

	public void setReactionClickListener(ReactionClickListener reactionClickListener){
		this.reactionClickListener = reactionClickListener;
	}

	public void setFriendRequestClickListener(FriendRequestClickListener friendRequestClickListener) {
		this.friendRequestClickListener = friendRequestClickListener;
	}

	public void setAddPostClickListener(AddPostClickListener addPostClickListener){
		this.addPostClickListener = addPostClickListener;
	}

	public void setAddPhotoClickListener(AddPhotoClickListener addPhotoClickListener){
		this.addPhotoClickListener = addPhotoClickListener;
	}

	public void setEventTypeSelectionClickListener(EventTypeSelectionClickListener eventTypeSelectionClickListener){
		this.eventTypeSelectionClickListener = eventTypeSelectionClickListener;
	}

	public void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public void setSchoolPostClickListener(SchoolPostClickListener schoolPostClickListener) {
		this.schoolPostClickListener = schoolPostClickListener;
	}

	public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

	public void setStartDragListener(OnStartDragListener startDragListener){
		this.startDragListener = startDragListener;
	}

	public void setOnItemRemovedListener(OnItemRemovedListener onItemRemovedListener){
		this.onItemRemovedListener = onItemRemovedListener;
	}

	@Override
	public int getItemViewType(int position) {
		Object currentItem = contentArrayList.get(position);

		if (currentItem instanceof School)
			return SEARCH_RESULT_SCHOOL_ITEM;
		else if (currentItem instanceof PersonSearchResult)
			return SEARCH_RESULT_PERSON_ITEM;
		else if (currentItem instanceof SchoolPost)
			return SCHOOL_POST_ITEM;
		else if (currentItem instanceof AddEditPostActivity)	// TODO: Replace class with something more efficient
			return SCHOOL_POST_NEW_POST_ITEM;
		else if (currentItem instanceof SchoolPostComment)
			return SCHOOL_POST_COMMENT_ITEM;
		else if (currentItem instanceof ProgressBar)
			return PROGRESS_BAR_ITEM;
		else if (currentItem instanceof CalendarEventType)
			return CALENDAR_EVENT_TYPE_SELECTION_BIG_ITEM;
		else if (currentItem instanceof CalendarEventTypeSmall)
			return CALENDAR_EVENT_TYPE_SELECTION_SMALL_ITEM;
		else if (currentItem instanceof CalendarEvent)
			return CALENDAR_LOADED_EVENTS_ITEM;
		else if (currentItem instanceof TimetableListItem)
			return SUBJECT_TIMETABLE_ITEM;
		else if (currentItem instanceof GenericListItem)
			return MIDDLE_DRAWER_ITEM;
		else if (currentItem instanceof GenericListTitleHeader)
			return MIDDLE_DRAWER_TITLE_HEADER_ITEM;
		else if (currentItem instanceof NewTimetableItem)
			return NEW_TIMETABLE_ITEM_ITEM;
		else if (currentItem instanceof SubjectListItem)
			return SUBJECT_LIST_ITEM;
		else if (currentItem instanceof Message) {
			if (((Message) currentItem).isOwner())
				return CHAT_SENT_MESSAGE_ITEM;
			else
				return CHAT_RECEIVED_MESSAGE_ITEM;
		} else if (currentItem instanceof ChatRoom)
			return CHAT_ROOM_ITEM;
		else if (currentItem instanceof PersonProfileHeaderItem)
			return PERSON_PROFILE_HEADER_ITEM;
		else if (currentItem instanceof PersonProfileEmptyDatasetItem)
			return PERSON_PROFILE_EMPTY_DATASET_ITEM;
		else if (currentItem instanceof Notification)
			return NOTIFICATION_ITEM;

		return -1;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case SEARCH_RESULT_SCHOOL_ITEM:
				return new SchoolViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));

			case SEARCH_RESULT_PERSON_ITEM:
				return new PersonViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));

			case SCHOOL_POST_ITEM:
				return new SchoolPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_feed_post, parent, false));

			case SCHOOL_POST_COMMENT_ITEM:
				return new SchoolPostCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post_details_comment, parent, false));

			case PROGRESS_BAR_ITEM:
				return new ProgressBarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_feed_progress_bar, parent, false));

			case SCHOOL_POST_NEW_POST_ITEM:
				return new NewPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_feed_new_post, parent, false));

			case CALENDAR_EVENT_TYPE_SELECTION_BIG_ITEM:
				return new CalendarEventTypeBigViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calendar_event_type_selection_big, parent, false));

			case CALENDAR_EVENT_TYPE_SELECTION_SMALL_ITEM:
				return new CalendarEventTypeSmallViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calendar_event_type_selection_small, parent, false));

			case CALENDAR_LOADED_EVENTS_ITEM:
				return new CalendarLoadedEventsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calendar_event_loaded_event, parent, false));

			case MIDDLE_DRAWER_ITEM:
				return new GenericListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_generic, parent, false));

			case MIDDLE_DRAWER_TITLE_HEADER_ITEM:
				return new MiddleDrawerTitleHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_generic_title_header, parent, false));

			case SUBJECT_TIMETABLE_ITEM:
				return new SubjectTimeTableItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_timetable_subject, parent, false));

			case NEW_TIMETABLE_ITEM_ITEM:
				return new NewTimetableItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_timetable_new, parent, false));

			case SUBJECT_LIST_ITEM:
				return new SubjectListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subject_card, parent, false));

			case CHAT_RECEIVED_MESSAGE_ITEM:
				return new ReceivedMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_received, parent, false));

			case CHAT_SENT_MESSAGE_ITEM:
				return new SentMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_sent, parent, false));

			case CHAT_ROOM_ITEM:
				return new GenericListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_generic, parent, false));

			case PERSON_PROFILE_HEADER_ITEM:
				return new PersonProfileHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_person_profile_header, parent, false));

			case PERSON_PROFILE_EMPTY_DATASET_ITEM:
				return new PersonProfileEmptyDatasetViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empty_dataset, parent, false));

			case NOTIFICATION_ITEM:
				return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false));

			// Necessary default return statement. Never called.
			default: return null;
		}
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		isBinding = true;

		// If current scroll position is appropriate and data is not already loading
		// then we notify the view that it can request more data from the Server
		if(position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null){
			isLoading = true;
			loadMoreListener.onLoadMore();
		}

		// Separating each of the ViewTypes so they can perform their own work
		switch (holder.getItemViewType()) {
			case SEARCH_RESULT_SCHOOL_ITEM:
				SchoolViewHolder schoolViewHolder = (SchoolViewHolder) holder;
				School school = (School) contentArrayList.get(position);

				String schoolName = Utility.deAccentString(school.getName().toLowerCase(Locale.getDefault()));

				int startPos = 0;
				int endPos = 0;

				if (schoolName.contains(searchString)) {
					startPos = schoolName.indexOf(searchString);
					endPos = startPos + searchString.length();
				}
				else if (searchString.contains("dj")){
					startPos = schoolName.indexOf(searchString.substring(0, searchString.length()-1));
					endPos = startPos + searchString.length() - 1;
				}
				else if (schoolName.contains("\"") && !searchString.contains("\"")){
					int quotesNum = schoolName.length() - schoolName.replace("\"", "").length();
					schoolName = schoolName.replace("\"", "");
					startPos = schoolName.indexOf(searchString);
					endPos = startPos + searchString.length() + quotesNum;
				}

				Spannable spanText = Spannable.Factory.getInstance().newSpannable(school.getName());

				//TODO: Fix me :(
				try {
					spanText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					schoolViewHolder.nameText.setText(spanText, TextView.BufferType.SPANNABLE);
				} catch (IndexOutOfBoundsException exception) {
					exception.printStackTrace();
					schoolViewHolder.nameText.setText(schoolName);
				}
				break;

			case SEARCH_RESULT_PERSON_ITEM:
				PersonViewHolder personViewHolder = (PersonViewHolder) holder;
				PersonSearchResult personSearchResult = (PersonSearchResult) contentArrayList.get(position);

				personViewHolder.nameText.setText(personSearchResult.getName());
				break;

			case SCHOOL_POST_ITEM:
				SchoolPostViewHolder schoolPostViewHolder = (SchoolPostViewHolder) holder;
				SchoolPost schoolPost= (SchoolPost) contentArrayList.get(position);

				// Setting up Posts's views
				schoolPostViewHolder.schoolNameText.setText(schoolPost.getSchool());
				schoolPostViewHolder.userNameText.setText(schoolPost.getUser());
				schoolPostViewHolder.dateText.setText(schoolPost.getPostDate());
				schoolPostViewHolder.contentText.setText(schoolPost.getContent());

				// Check if post has any Upvotes or Downvotes
				if (schoolPost.getReactionCount() != 0) {
					// We show the Reaction counter TextView
					schoolPostViewHolder.reactionCounterText.setVisibility(View.VISIBLE);
					schoolPostViewHolder.reactionSeparatorView.setVisibility(View.GONE);
					schoolPostViewHolder.reactionCounterText.setText(schoolPost.getReactionCount().toString());
				} else {
					// We hide the Reaction counter TextView
					schoolPostViewHolder.reactionCounterText.setVisibility(View.GONE);
					schoolPostViewHolder.reactionSeparatorView.setVisibility(View.VISIBLE);
				}

				// If Post is upvoted by the User then we apply its visual modification
				if (schoolPost.isUpvoted()){
					Utility.modifyTextView(schoolPostViewHolder.upvoteText, true, 18, R.color.colorPrimary);
					Utility.clearTextViewModifications(schoolPostViewHolder.downvoteText);
				}
				// Same for downvote
				else if (schoolPost.isDownvoted()){
					Utility.modifyTextView(schoolPostViewHolder.downvoteText, true, 18, R.color.color_success);
					Utility.clearTextViewModifications(schoolPostViewHolder.upvoteText);
				}
				// If the Post is neither Upvoted or Downvoted then we clear all the modifications
				else {
					Utility.clearTextViewModifications(schoolPostViewHolder.upvoteText, schoolPostViewHolder.downvoteText);
				}

				// Check if current post has an Image URL
				if (!schoolPost.getImageUrl().isEmpty()){
					// If it has one then we prepare the image area
					schoolPostViewHolder.postImage.setVisibility(View.VISIBLE);

					// Then we set it into the Post ImageView with Glide
					Glide
							.with(schoolPostViewHolder.postImage.getContext())
							.load(schoolPost.getImageUrl())
							// Showing loading GIF while the Image is being downloaded
							.thumbnail(Glide.with(schoolPostViewHolder.postImage.getContext()).load(R.drawable.small_spinner))
							.crossFade()
							.diskCacheStrategy(DiskCacheStrategy.SOURCE)
							.override(Registry.GLIDE_DEFAULT_WIDTH, Registry.GLIDE_DEFAULT_HEIGHT)
							.into(schoolPostViewHolder.postImage);

				} else {
					// In case we don't have the URL, we clear the ImageView
					Glide.clear(schoolPostViewHolder.postImage);
					schoolPostViewHolder.postImage.setVisibility(View.GONE);
				}

				// Make links and phone numbers in TextView clickable
				Linkify.addLinks(schoolPostViewHolder.contentText, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
				break;

			case SCHOOL_POST_COMMENT_ITEM:
				SchoolPostCommentViewHolder schoolPostCommentViewHolder  = (SchoolPostCommentViewHolder) holder;
				SchoolPostComment schoolPostComment = (SchoolPostComment) contentArrayList.get(position);

				// Setting up Posts's views
				schoolPostCommentViewHolder.userNameText.setText(schoolPostComment.getUser());
				schoolPostCommentViewHolder.dateText.setText(schoolPostComment.getCommentDate());
				schoolPostCommentViewHolder.contentText.setText(schoolPostComment.getContent());

				// Check if post has any Upvotes or Downvotes
				if (schoolPostComment.getReactionCount() != 0) {
					// We show the Reaction counter TextView
					schoolPostCommentViewHolder.reactionCounterText.setVisibility(View.VISIBLE);
					schoolPostCommentViewHolder.reactionSeparatorView.setVisibility(View.GONE);
					schoolPostCommentViewHolder.reactionCounterText.setText(schoolPostComment.getReactionCount().toString());
				} else {
					// We hide the Reaction counter TextView
					schoolPostCommentViewHolder.reactionCounterText.setVisibility(View.GONE);
					schoolPostCommentViewHolder.reactionSeparatorView.setVisibility(View.VISIBLE);
				}

				// If Post is upvoted by the User then we apply its visual modification
				if (schoolPostComment.isUpvoted()){
					Utility.modifyTextView(schoolPostCommentViewHolder.upvoteText, true, 16, R.color.colorPrimary);
					Utility.clearTextViewModifications(schoolPostCommentViewHolder.downvoteText);
				}
				// Same for downvote
				else if (schoolPostComment.isDownvoted()){
					Utility.modifyTextView(schoolPostCommentViewHolder.downvoteText, true, 16, R.color.color_success);
					Utility.clearTextViewModifications(schoolPostCommentViewHolder.upvoteText);
				}
				// If the Post is neither Upvoted or Downvoted then we clear all the modifications
				else
					Utility.clearTextViewModifications(schoolPostCommentViewHolder.upvoteText, schoolPostCommentViewHolder.downvoteText);

				// Make links and phone numbers in TextView clickable
				Linkify.addLinks(schoolPostCommentViewHolder.contentText, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
				break;

			case PROGRESS_BAR_ITEM:
				// Activate the ProgressBar's loading spinner
				((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
				break;

			case SCHOOL_POST_NEW_POST_ITEM:
				((NewPostViewHolder) holder).userNameTv.setText(UserInfo.CURRENT_USER.getName());
				break;

			case CALENDAR_EVENT_TYPE_SELECTION_BIG_ITEM:
				CalendarEventTypeBigViewHolder calendarEventTypeBigViewHolder = (CalendarEventTypeBigViewHolder) holder;
				CalendarEventType calendarEventType = (CalendarEventType) contentArrayList.get(position);

				calendarEventTypeBigViewHolder.eventTypeText.setText(calendarEventType.getEventTypeName());
				calendarEventTypeBigViewHolder.eventTypeImage.setImageResource(calendarEventType.getImageResource());

				break;

			case CALENDAR_LOADED_EVENTS_ITEM:
				CalendarLoadedEventsViewHolder calendarLoadedEventsViewHolder = (CalendarLoadedEventsViewHolder) holder;
				CalendarEvent calendarEvent = (CalendarEvent) contentArrayList.get(position);

				calendarLoadedEventsViewHolder.eventSubjectText.setText(calendarEvent.getSubject());
				calendarLoadedEventsViewHolder.dateText.setText(calendarEvent.getDate());
				calendarLoadedEventsViewHolder.eventTitleText.setText(calendarEvent.getTitle());
				calendarLoadedEventsViewHolder.eventImage.setImageResource(calendarEvent.getImageResource());

				break;

			case MIDDLE_DRAWER_ITEM:
				GenericListItemViewHolder genericListItemViewHolder = (GenericListItemViewHolder) holder;
				GenericListItem genericListItem = (GenericListItem) contentArrayList.get(position);

				genericListItemViewHolder.itemText.setText(genericListItem.getItemText());
				genericListItemViewHolder.itemImage.setImageResource(genericListItem.getImageResource());


				if (genericListItem.showOrder()) {
					genericListItemViewHolder.counterParentView.setVisibility(View.VISIBLE);
					genericListItemViewHolder.counterText.setText((position + 1) + ".");
				}

				break;

			case MIDDLE_DRAWER_TITLE_HEADER_ITEM:
				MiddleDrawerTitleHeaderViewHolder middleDrawerTitleHeaderViewHolder = (MiddleDrawerTitleHeaderViewHolder) holder;
				GenericListTitleHeader genericListTitleHeader = (GenericListTitleHeader) contentArrayList.get(position);

				middleDrawerTitleHeaderViewHolder.headerText.setText(genericListTitleHeader.getHeaderText());

				break;

			case SUBJECT_TIMETABLE_ITEM:
				final SubjectTimeTableItemViewHolder subjectTimeTableItemViewHolder = (SubjectTimeTableItemViewHolder) holder;
				TimetableListItem timetableListItem = (TimetableListItem) contentArrayList.get(position);

				subjectTimeTableItemViewHolder.itemText.setText(timetableListItem.getItemText());
				subjectTimeTableItemViewHolder.itemImage.setImageResource(timetableListItem.getImageResource());

				subjectTimeTableItemViewHolder.dragHandle.setOnTouchListener(new View.OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
							startDragListener.onStartDrag(subjectTimeTableItemViewHolder);
						}
						return false;
					}
				});

				break;

			case SUBJECT_LIST_ITEM:
				final SubjectListItemViewHolder subjectListItemViewHolder = (SubjectListItemViewHolder) holder;
				SubjectListItem subjectListItem = (SubjectListItem) contentArrayList.get(position);

				subjectListItemViewHolder.subjectTitle.setText(subjectListItem.getSubjectName());

				if (subjectListItem.getEventTypes().isEmpty()) {
					subjectListItemViewHolder.numOfAssignments.setText("No assignments for this subject.");
					subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_denied);
				}
				else
					subjectListItemViewHolder.numOfAssignments.setText("Assignments");

				if (subjectListItem.getEventTypes().contains(EventType.TEST))
					subjectListItemViewHolder.testAssignment.setVisibility(View.VISIBLE);
				else
					subjectListItemViewHolder.testAssignment.setVisibility(View.GONE);

				if (subjectListItem.getEventTypes().contains(EventType.HOME_WORK))
					subjectListItemViewHolder.homeworkAssignment.setVisibility(View.VISIBLE);
				else
					subjectListItemViewHolder.homeworkAssignment.setVisibility(View.GONE);

				if (subjectListItem.getEventTypes().contains(EventType.PRESENTATION))
					subjectListItemViewHolder.presentationAssignment.setVisibility(View.VISIBLE);
				else
					subjectListItemViewHolder.presentationAssignment.setVisibility(View.GONE);

				if (subjectListItem.getEventTypes().contains(EventType.OTHER_ASSIGNMENT))
					subjectListItemViewHolder.otherAssignmentAssignment.setVisibility(View.VISIBLE);
				else
					subjectListItemViewHolder.otherAssignmentAssignment.setVisibility(View.GONE);

				switch (subjectListItem.getGrade()) {
					case GRADE_NONE:
						subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_denied);
						break;
					case GRADE_A:
						subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_grade_a);
						break;
					case GRADE_B:
						subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_grade_b);
						break;
					case GRADE_C:
						subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_grade_c);
						break;
					case GRADE_D:
						subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_grade_d);
						break;
					case GRADE_F:
						subjectListItemViewHolder.gradeImage.setImageResource(R.drawable.ic_grade_f);
						break;
				}

				break;

			case CHAT_RECEIVED_MESSAGE_ITEM:
				final ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
				receivedMessageHolder.bind((Message) contentArrayList.get(position));

				break;

			case CHAT_SENT_MESSAGE_ITEM:
				final SentMessageHolder sentMessageHolder = (SentMessageHolder) holder;
				sentMessageHolder.bind((Message) contentArrayList.get(position));

				break;

			case CHAT_ROOM_ITEM:
				final GenericListItemViewHolder genericListItemVH = (GenericListItemViewHolder) holder;
				genericListItemVH.itemText.setText(((ChatRoom) contentArrayList.get(position)).getPerson().getName());
				genericListItemVH.itemImage.setImageResource(R.drawable.ic_2_people);

				break;

			case PERSON_PROFILE_HEADER_ITEM:
				final PersonProfileHeaderViewHolder personProfileHeaderVH = (PersonProfileHeaderViewHolder) holder;
				personProfileHeaderVH.schoolNameTV.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
				personProfileHeaderVH.schoolNameTV.setTextSize(14);
				personProfileHeaderVH.schoolNameTV.setText(((PersonProfileHeaderItem) contentArrayList.get(position)).getSchoolName());

				Person.FriendType friendType = ((PersonProfileHeaderItem) contentArrayList.get(position)).getFriendType();

				if (friendType == Person.FriendType.FRIENDS) {
					personProfileHeaderVH.friendsTV.setText("Friends");
					personProfileHeaderVH.friendsTV.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
					personProfileHeaderVH.friendsTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_2_people, 0, 0);
				} else if (friendType == Person.FriendType.NOT_FRIENDS){
					personProfileHeaderVH.friendsTV.setText("Add Friend");
					personProfileHeaderVH.friendsTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_plus_green, 0, 0);
				} else if (friendType == Person.FriendType.FRIEND_REQUEST_SENT) {
					personProfileHeaderVH.friendsTV.setText("Request sent");
					personProfileHeaderVH.friendsTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_2_people, 0, 0);
				} else if (friendType == Person.FriendType.FRIEND_REQUEST_RECEIVED){
					personProfileHeaderVH.friendsTV.setText("Request received");
					personProfileHeaderVH.friendsTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_plus_green, 0, 0);
				}

				break;

			case NOTIFICATION_ITEM:
				final NotificationViewHolder notificationVH = (NotificationViewHolder) holder;
				Notification notification = (Notification) contentArrayList.get(position);
				notificationVH.titleText.setText(notification.getTitle());
				notificationVH.timeText.setText(notification.getTime());

				break;
		}

		isBinding = false;
	}

	// Called when dragging and the Item has changed it's original position
	@Override
	public boolean onItemMove(int fromPosition, int toPosition) {
		Collections.swap(contentArrayList, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);

		return true;
	}

	// Called when item gets swiped away
	@Override
	public void onItemDismiss(int position) {
		onItemRemovedListener.onItemRemoved(position);

		contentArrayList.remove(position);
		notifyItemRemoved(position);
	}

	// TODO: Remove
	// Swapping the adapter's content
	public void swap(ArrayList<Object> newContent, boolean firstItemIsAddPost){
		contentArrayList.clear();

		if (firstItemIsAddPost)
			contentArrayList.add(new AddEditPostActivity());

		contentArrayList.addAll(newContent);
		notifyDataSetChanged();
	}

	// TODO: Remove
	@Override
	public int getItemCount() {
		return contentArrayList == null ? 0 : contentArrayList.size();
	}

	public void boldSearchText(String searchString) {
		this.searchString = searchString;
	}

	public class SchoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(android.R.id.text1) TextView nameText;

		public SchoolViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}
		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, SchoolViewHolder.class, getAdapterPosition());
		}
	}

	public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(android.R.id.text1) TextView nameText;

		public PersonViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}
		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, PersonViewHolder.class, getAdapterPosition());
		}
	}

	public class SchoolPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.school_post_name) TextView schoolNameText;
		@BindView(R.id.school_post_user_name) TextView userNameText;
		@BindView(R.id.school_post_date) TextView dateText;
		@BindView(R.id.school_post_content) TextView contentText;
		@BindView(R.id.school_post_upvote_tv) TextView upvoteText;
		@BindView(R.id.school_post_downvote_tv) TextView downvoteText;
		@BindView(R.id.school_post_reaction_counter) TextView reactionCounterText;
		@BindView(R.id.school_post_reaction_separator) View reactionSeparatorView;
		@BindView(R.id.school_post_image) ImageView postImage;
		@BindView(R.id.school_post_context_menu) ImageView contextMenuImage;

		public SchoolPostViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			contextMenuImage.setOnClickListener(this);
			upvoteText.setOnClickListener(this);
			downvoteText.setOnClickListener(this);
			itemView.setOnClickListener(this);
			postImage.setDrawingCacheEnabled(true);
		}
		@Override
		public void onClick(View view) {
			int itemPosition = getAdapterPosition();

			if (schoolPostClickListener != null && view == itemView)
				schoolPostClickListener.onSchoolPostClick(postImage, itemPosition);

			else if (contextMenuClickListener != null && view == contextMenuImage)
				contextMenuClickListener.onContextMenuClick(contextMenuImage, contentArrayList.get(itemPosition));

			else if (reactionClickListener != null && (view == upvoteText || view == downvoteText)) {
				SchoolPost selectedPost = (SchoolPost) contentArrayList.get(itemPosition);
				reactionClickListener.onReactionClick(view == upvoteText ? ReactionType.LIKE : ReactionType.DISLIKE, selectedPost);

				notifyItemChanged(getAdapterPosition());
			}
		}
	}

	public class SchoolPostCommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		@BindView(R.id.post_details_comment_user_name) TextView userNameText;
		@BindView(R.id.post_details_comment_date) TextView dateText;
		@BindView(R.id.post_details_comment_content) TextView contentText;
		@BindView(R.id.post_details_comment_upvote_tv) TextView upvoteText;
		@BindView(R.id.post_details_comment_downvote_tv) TextView downvoteText;
		@BindView(R.id.post_details_comment_reaction_counter) TextView reactionCounterText;
		@BindView(R.id.post_details_comment_reaction_separator) View reactionSeparatorView;
		@BindView(R.id.post_details_comment_context_menu) ImageView contextMenuImage;

		public SchoolPostCommentViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			contextMenuImage.setOnClickListener(this);
			upvoteText.setOnClickListener(this);
			downvoteText.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			int itemPosition = getAdapterPosition();

			if (contextMenuClickListener != null && view == contextMenuImage)
				contextMenuClickListener.onContextMenuClick(contextMenuImage, contentArrayList.get(itemPosition));

			else if (reactionClickListener != null && (view == upvoteText || view == downvoteText)) {
				SchoolPostComment selectedComment = (SchoolPostComment) contentArrayList.get(itemPosition);
				reactionClickListener.onReactionClick(view == upvoteText ? ReactionType.LIKE : ReactionType.DISLIKE, selectedComment);

				notifyItemChanged(getAdapterPosition());
			}
		}
	}

	public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.recycler_view_progress_bar_item) ProgressBar progressBar;

		public ProgressBarViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	public class NewPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.add_post_user_name) TextView userNameTv;
		@BindView(R.id.add_post_text_click) TextView addPostTv;
		@BindView(R.id.add_post_photo) TextView addPhotoTv;

		public NewPostViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			addPostTv.setOnClickListener(this);
			addPhotoTv.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (view == addPostTv && addPostClickListener != null)
				addPostClickListener.onAddPostClick();
			else if (view == addPhotoTv && addPhotoClickListener != null)
				addPhotoClickListener.onAddPhotoClick();
		}
	}

	public class CalendarEventTypeBigViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.calendar_event_type_text) TextView eventTypeText;
		@BindView(R.id.calendar_event_type_img) ImageView eventTypeImage;

		public CalendarEventTypeBigViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, CalendarEventTypeBigViewHolder.class, getAdapterPosition());
		}
	}

	public class CalendarEventTypeSmallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.calendar_event_add_new_event_tv) TextView eventTypeTitleText;
		@BindView(R.id.calendar_event_add_test_img) ImageView eventTypeTestText;
		@BindView(R.id.calendar_event_add_homework_img) ImageView eventTypeHomeworkText;
		@BindView(R.id.calendar_event_add_presentation_img) ImageView eventTypePresentationText;
		@BindView(R.id.calendar_event_add_assignment_img) ImageView eventTypeOtherAssignmentText;

		public CalendarEventTypeSmallViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			eventTypeTitleText.setOnClickListener(this);

			itemView.setOnClickListener(this);
			eventTypeTestText.setOnClickListener(this);
			eventTypeHomeworkText.setOnClickListener(this);
			eventTypePresentationText.setOnClickListener(this);
			eventTypeOtherAssignmentText.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (eventTypeSelectionClickListener == null)
				return;

			if (view == eventTypeTestText)
				eventTypeSelectionClickListener.onEventTypeClick(EventType.TEST);
			else if (view == eventTypeHomeworkText)
				eventTypeSelectionClickListener.onEventTypeClick(EventType.HOME_WORK);
			else if (view == eventTypePresentationText)
				eventTypeSelectionClickListener.onEventTypeClick(EventType.PRESENTATION);
			else if (view == eventTypeOtherAssignmentText)
				eventTypeSelectionClickListener.onEventTypeClick(EventType.OTHER_ASSIGNMENT);
			else
				eventTypeSelectionClickListener.onAddNewEventClick();
		}
	}

	public class CalendarLoadedEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.card_event_subject) TextView eventSubjectText;
		@BindView(R.id.card_event_added_date) TextView dateText;
		@BindView(R.id.card_event_title) TextView eventTitleText;
		@BindView(R.id.card_event_img) ImageView eventImage;
		@BindView(R.id.card_event_context_menu) ImageView contextMenuImage;

		public CalendarLoadedEventsViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			contextMenuImage.setOnClickListener(this);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null && view == itemView)
				itemClickListener.onRecyclerViewItemClick(view, CalendarLoadedEventsViewHolder.class, getAdapterPosition());

			else if (contextMenuClickListener != null && view == contextMenuImage) {
				contextMenuClickListener.onContextMenuClick(contextMenuImage, contentArrayList.get(getAdapterPosition()));
			}
		}
	}

	public class GenericListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.middle_drawer_item_tv) TextView itemText;
		@BindView(R.id.middle_drawer_item_img) ImageView itemImage;
		@BindView(R.id.list_item_counter_parent) LinearLayout counterParentView;
		@BindView(R.id.list_item_counter) TextView counterText;

		public GenericListItemViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, GenericListItemViewHolder.class, getAdapterPosition());
		}
	}

	public class SubjectTimeTableItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
		@BindView(R.id.subject_timetable_item_tv) TextView itemText;
		@BindView(R.id.subject_timetable_item_img) ImageView itemImage;
		@BindView(R.id.subject_timetable_handle) ImageView dragHandle;

		public SubjectTimeTableItemViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

		@Override
		public void onItemSelected() {

		}

		@Override
		public void onItemClear() {

		}
	}

	public class MiddleDrawerTitleHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.middle_drawer_header_tv) TextView headerText;

		public MiddleDrawerTitleHeaderViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, CalendarEventTypeBigViewHolder.class, getAdapterPosition());
		}
	}

	public class NewTimetableItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.timetable_add_subject_button) TextView addButton;

		public NewTimetableItemViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, NewTimetableItemViewHolder.class, getAdapterPosition());
		}
	}

	public class SubjectListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.subject_list_subject_title) TextView subjectTitle;
		@BindView(R.id.subject_list_num_of_assignments) TextView numOfAssignments;
		@BindView(R.id.subject_list_assignment_test) ImageView testAssignment;
		@BindView(R.id.subject_list_assignment_homework) ImageView homeworkAssignment;
		@BindView(R.id.subject_list_assignment_presentation) ImageView presentationAssignment;
		@BindView(R.id.subject_list_assignment_other_assignment) ImageView otherAssignmentAssignment;
		@BindView(R.id.subject_list_grade_image) ImageView gradeImage;

		public SubjectListItemViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, SubjectListItemViewHolder.class, getAdapterPosition());
		}
	}

	public class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView messageText, timeText, nameText;

		ReceivedMessageHolder(View itemView) {
			super(itemView);

			itemView.setOnClickListener(this);

			messageText = itemView.findViewById(R.id.text_message_body);
			timeText = itemView.findViewById(R.id.text_message_time);
			nameText = itemView.findViewById(R.id.text_message_name);
		}

		void bind(Message message) {
			messageText.setText(message.getMessage());

			Linkify.addLinks(messageText, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);

			// Format the stored timestamp into a readable String using method.
			timeText.setText(message.getCreatedAt());
			nameText.setText(message.getPerson());
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, ReceivedMessageHolder.class, getAdapterPosition());
		}
	}

	public class SentMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView messageText, timeText;

		SentMessageHolder(View itemView) {
			super(itemView);

			itemView.setOnClickListener(this);

			messageText = itemView.findViewById(R.id.text_message_body);
			timeText = itemView.findViewById(R.id.text_message_time);
		}

		void bind(Message message) {
			messageText.setText(message.getMessage());

			Linkify.addLinks(messageText, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);

			// Format the stored timestamp into a readable String using method.
			timeText.setText(message.getCreatedAt());
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, SentMessageHolder.class, getAdapterPosition());
		}
	}

	public class PersonProfileHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.person_profile_friends_tv) TextView friendsTV;
		@BindView(R.id.person_profile_school_name_tv) TextView schoolNameTV;

		public PersonProfileHeaderViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
			friendsTV.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (reactionClickListener != null && view == friendsTV)
				friendRequestClickListener.onFriendRequestClick();
			else if (itemClickListener != null)
				itemClickListener.onRecyclerViewItemClick(view, NewTimetableItemViewHolder.class, getAdapterPosition());
		}
	}

	public class PersonProfileEmptyDatasetViewHolder extends RecyclerView.ViewHolder {

		public PersonProfileEmptyDatasetViewHolder(View view) {
			super(view);
		}
	}

	public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.notification_title_tv) TextView titleText;
		@BindView(R.id.notification_item_img) ImageView image;
		@BindView(R.id.notification_time_tv) TextView timeText;

		public NotificationViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if (itemClickListener != null) itemClickListener.onRecyclerViewItemClick(view, GenericListItemViewHolder.class, getAdapterPosition());
		}
	}

	public void setMoreDataAvailable(boolean moreDataAvailable) {
		isMoreDataAvailable = moreDataAvailable;
	}

	/*
		notifyDataSetChanged is final method so we can't override it
		call adapter.notifyDataChanged(); after update the list
    */
	public void notifyDataChanged(){
		notifyDataSetChanged();
		isLoading = false;
	}
}

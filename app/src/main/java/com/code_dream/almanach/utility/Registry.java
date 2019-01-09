package com.code_dream.almanach.utility;

import android.graphics.Color;

import com.code_dream.almanach.R;

/**
 * Created by Qwerasdzxc on 21-Dec-16.
 */

public final class Registry {

	// TOOLBAR
	public static final String TOOLBAR_NO_TITLE = "";

	// DJANGO_BASE_SERVER_URL
	public final static String BASE_SERVER_URL = "______________";
    public final static String DEV_BASE_SERVER_URL = "______________";
    public final static String WEB_SOCKET_URL = "______________";
    public final static String DEV_WEB_SOCKET_URL = "______________";

    //GOOGLE MAPS API URL
    public static final String GEOCODE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?";

	// RESPONSE_CODES
	public static final int CODE_BAD_LOGIN = 203;
	public static final int CODE_BAD_REGISTER_USER_ALREADY_EXISTS = 303;
	public static final int CODE_SUCCESSFUL_RESPONSE = 200;

	// LOAD_TOAST
	public static final int LOAD_TOAST_Y_OFFSET = 50;

	// INTERNAL_CODES
	public static final int LOAD_ALL_POSTS_CODE = 0;
	public static final int ACTIVITY_RESULT_OK = 1;

	// STRING_SHORTCUTS
	public static final String NO_DATA_TO_SEND = "";
	public static final String EMPTY_TOKEN = "";

	// FAST_LOGIN
	public static final String DEV_EMAIL = "______________";
	public static final String DEV_PASS = "______________";

	// SHARED_PREFERENCES
	public static final String SHARED_PREFS_TOKEN_KEY = "session_token";
	public static final String SHARED_PREFS_ID_KEY = "person_id";
	public static final String SHARED_PREFS_FULL_NAME_KEY = "full_name";
	public static final String SHARED_PREFS_INTRO_KEY = "intro_finished_";
	public static final String SHARED_PREFS_TIMETABLE_TUTORIAL_KEY = "timetable_tutorial_";
    public static final String SHARED_PREFS_FIREBASE_TOKEN_KEY = "firebase_token";
    public static final String SHARED_PREFS_EMAIL_AUTOFILL_KEY = "email_autofill";

	// SCHOOL_POSTS
	public static final int DEFAULT_POST_ID = 0;

	// CALENDAR_EVENT
	public static final int TEST_EVENT_COLOR = Color.CYAN;
	public static final int HOMEWORK_EVENT_COLOR = Color.BLUE;
	public static final int PRESENTATION_EVENT_COLOR = Color.DKGRAY;
	public static final int OTHER_ASSIGNMENT_EVENT_COLOR = Color.argb(255,250,240,25);

	// MIDDLE_DRAWER_LIST_ITEMS
	public static final String MIDDLE_DRAWER_TIMETABLE_HEADER_TITLE = "Timetable";
	public static final String MIDDLE_DRAWER_SUBJECTS_HEADER_TITLE = "Subjects";

	public static final String[] MIDDLE_DRAWER_TIMETABLE_ITEMS = {"Timetable"};
	public static final String[] MIDDLE_DRAWER_SUBJECTS_ITEMS = {"Subjects", "Notes"};

	public static final int[] MIDDLE_DRAWER_TIMETABLE_IMAGES = {R.drawable.ic_other_assignment};
	public static final int[] MIDDLE_DRAWER_SUBJECTS_IMAGES = {R.drawable.ic_homework, R.drawable.ic_clipboard};

	public static final int MIDDLE_DRAWER_MY_TIMETABLE_POSITION = 1;
	public static final int MIDDLE_DRAWER_MY_SUBJECTS_POSITION = 3;
	public static final int MIDDLE_DRAWER_NOTES_POSITION = 4;

	// ACTIVITY_REQUEST_CODES
	public static final int REQUEST_CAMERA_PERMISSION_CODE = 0;
	public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
	public static final int REQUEST_CAMERA_AND_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 2;
	public static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_CODE = 3;
	public static final int OPEN_CAMERA_ACTIVITY = 4;
	public static final int OPEN_IMAGE_SELECTION_ACTIVITY = 5;
	public static final int OPEN_POST_DETAILS_ACTIVITY = 6;
	public static final int OPEN_ADD_POST_ACTIVITY = 7;
	public static final int OPEN_EDIT_POST_ACTIVITY = 8;
	public static final int OPEN_SUBJECT_PROFILE_ACTIVITY = 9;
	public static final int OPEN_SUBJECT_LIST_ACTIVITY = 10;
	public static final int OPEN_ADD_EVENT_ACTIVITY = 11;
	public static final int OPEN_ADD_EDIT_NOTE_ACTIVITY = 12;

	// ACTIVITY_RESULT_CODES
	public static final int RESULT_SUBJECT_LIST_DELETION = 0;
	public static final int RESULT_SUBJECT_LIST_GRADE_CHANGED = 1;
	public static final int RESULT_SUBJECT_EVENT_DELETION = 2;

	// GLIDE_SETTINGS
	public static final int GLIDE_DEFAULT_WIDTH = 1400;
	public static final int GLIDE_DEFAULT_HEIGHT = 1400;

	// GRADE_IMAGE_VIEW
	public static final int GRADE_NONE_ID = 0;

	// ADD_EDIT_POST ACTIVITY
	public static final int ADD_EDIT_POST_MIN_CONTENT_LENGTH = 0;

	// RECYCLERVIEW
	public static final int MAXIMUM_ITEMS_FOR_AUTO_EXPANDING = 5;

	// INTRO_SCHOOL
	public static final String INTRO_ADD_SCHOOL_SUGGESTION_TEXT = "Add new school...";
    //Indicates if the user chose an existing school or is adding a new one
    public static Boolean addingNewSchool = false;
    //True if the user is about to go to the SchoolLocation slide
    public static Boolean userFinishedChoosing = false;
    public static String schoolName = "";

    //MESSAGES
    public static final String SERVER_ERROR_OCCURED = "Server error occured!";
	public static final String NO_INTERNET_CONNECTION_OFFLINE_MODE = "No Internet connection. You're offline!";
	public static final String MESSAGES_BROADCAST_NAME = "com.skolskipomocnik.app.ChatActivity";
}

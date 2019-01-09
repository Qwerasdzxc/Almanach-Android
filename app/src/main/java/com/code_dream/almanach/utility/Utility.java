package com.code_dream.almanach.utility;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.code_dream.almanach.R;
import com.code_dream.almanach.calendar_add_event.EventType;
import com.code_dream.almanach.event_profile.Grades;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.TimetableItem;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Qwerasdzxc on 21-Dec-16.
 */

public final class Utility
{
	public  interface DialogResponse {
		void positive();
		void negative();
	}

	public static boolean isEmailValid(String email) {
		return email.contains("@");
	}

	public static boolean isFullNameValid(String name) {
		return name.length() > 2;
	}

	public static boolean isDobValid(String dob) {
		return dob.matches("\\d{1,2}/\\d{1,2}/\\d{4}");
	}

	public static boolean isPasswordValid(String password) {
		return password.length() > 4;
	}

	public static String deAccentString(String str) {
		if (str.contains("dj"))
			str = str.replace("dj", "đ");

		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static int getEventTypeColor(EventType eventType){
		switch (eventType){
			case TEST:
				return Registry.TEST_EVENT_COLOR;
			case HOME_WORK:
				return Registry.HOMEWORK_EVENT_COLOR;
			case PRESENTATION:
				return Registry.PRESENTATION_EVENT_COLOR;
			case OTHER_ASSIGNMENT:
				return Registry.OTHER_ASSIGNMENT_EVENT_COLOR;
			default:
				return Registry.TEST_EVENT_COLOR;
		}
	}

	public static void displayDialog(Context context, String title, String message, final DialogResponse dialogResponse) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialogResponse.positive();
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialogResponse.negative();
					}
				})
				.show();
	}

	public static void displayTimeoutDialog(Context context) {
		displayDialog(context, "Error!", "Can't establish the connection with the server.", new DialogResponse() {
			@Override
			public void positive() {}

			@Override
			public void negative() {}
		});
	}

	public static boolean checkIfDatesAreSame(DateTime calendarEvent, DateTime dateTime){
		return calendarEvent.getDayOfMonth() == dateTime.getDayOfMonth() && calendarEvent.getMonthOfYear() == dateTime.getMonthOfYear() && calendarEvent.getYear() == dateTime.getYear();
	}

	public static boolean checkIfDatesAreSame(CalendarEvent calendarEvent, DateTime dateTime){
		return calendarEvent.getDay() == dateTime.getDayOfMonth() && calendarEvent.getMonth() == dateTime.getMonthOfYear() && calendarEvent.getYear() == dateTime.getYear();
	}

	public static DateTime calendarDayToDateTime(CalendarDay calendarDay){
		return new DateTime(calendarDay.getYear(), calendarDay.getMonth() + 1, calendarDay.getDay(), 0, 0);
	}

	public static TimetableItem.Day getWeekDayByDate(DateTime dateTime){
		switch (dateTime.getDayOfWeek()){
			case 1:
				return TimetableItem.Day.MONDAY;
			case 2:
				return TimetableItem.Day.TUESDAY;
			case 3:
				return TimetableItem.Day.WEDNESDAY;
			case 4:
				return TimetableItem.Day.THURSDAY;
			case 5:
				return TimetableItem.Day.FRIDAY;
			case 6:
				return TimetableItem.Day.SATURDAY;
			case 7:
				return TimetableItem.Day.SUNDAY;

			default: return TimetableItem.Day.ALL_SUBJECTS;

			//TODO: Add non-school activities
		}
	}

	public static Grades getGradeByImageView(View view) {
		ImageView selectedImageView = (ImageView) view;

		switch (selectedImageView.getId()) {
			case R.id.event_profile_grade_A:
			case R.id.add_event_grade_A:
				return Grades.GRADE_A;
			case R.id.event_profile_grade_B:
			case R.id.add_event_grade_B:
				return Grades.GRADE_B;
			case R.id.event_profile_grade_C:
			case R.id.add_event_grade_C:
				return Grades.GRADE_C;
			case R.id.event_profile_grade_D:
			case R.id.add_event_grade_D:
				return Grades.GRADE_D;
			case R.id.event_profile_grade_F:
			case R.id.add_event_grade_F:
				return Grades.GRADE_F;

			default:
				return Grades.GRADE_NONE;
		}


	}

	public static EventType getEventTypeByImageView(View view) {
		ImageView selectedImageView = (ImageView) view;

		switch (selectedImageView.getId()) {
			case R.id.add_event_type_test:
				return EventType.TEST;
			case R.id.add_event_type_homework:
				return EventType.HOME_WORK;
			case R.id.add_event_type_presentation:
				return EventType.PRESENTATION;
			case R.id.add_event_type_other_assignment:
				return EventType.OTHER_ASSIGNMENT;

			default:
				return EventType.TEST;
		}
	}

	public static int getImageViewIDByGrade(Grades grade, boolean newEvent) {
		switch (grade) {
			case GRADE_A:
				if (newEvent)
					return R.id.add_event_grade_A;
				else
					return R.id.event_profile_grade_A;
			case GRADE_B:
				if (newEvent)
					return R.id.add_event_grade_B;
				else
					return R.id.event_profile_grade_B;
			case GRADE_C:
				if (newEvent)
					return R.id.add_event_grade_C;
				else
					return R.id.event_profile_grade_C;
			case GRADE_D:
				if (newEvent)
					return R.id.add_event_grade_D;
				else
					return R.id.event_profile_grade_D;
			case GRADE_F:
				if (newEvent)
					return R.id.add_event_grade_F;
				else
					return R.id.event_profile_grade_F;

			default:
				return Registry.GRADE_NONE_ID;
		}
	}

	public static int getImageViewIDByEvent(EventType eventType) {
		switch (eventType) {
			case TEST:
				return R.id.add_event_type_test;
			case HOME_WORK:
				return R.id.add_event_type_homework;
			case PRESENTATION:
				return R.id.add_event_type_presentation;
			case OTHER_ASSIGNMENT:
				return R.id.add_event_type_other_assignment;

			default:
				return Registry.GRADE_NONE_ID;
		}
	}

	public static int getImageDrawableByGrade(Grades grade) {
		switch (grade) {
			case GRADE_A:
				return R.drawable.ic_grade_a;
			case GRADE_B:
				return R.drawable.ic_grade_b;
			case GRADE_C:
				return R.drawable.ic_grade_c;
			case GRADE_D:
				return R.drawable.ic_grade_d;
			case GRADE_F:
				return R.drawable.ic_grade_f;

			default:
				return R.drawable.ic_denied;
		}
	}

	public static Grades getGradeByNumber(int grade) {
		return Grades.values()[grade];
	}

	public static Grades calculateFinalGrade(ArrayList<Grades> allGrades) {
		int gradeNone = 0;
		int gradesSum = 0;

		for (Grades grade : allGrades) {

			if (grade == Grades.GRADE_NONE) {
				gradeNone++;
				continue;
			}
			gradesSum += grade.ordinal();
		}

		float finalGrade = (float) gradesSum / (allGrades.size() - gradeNone);

		return getGradeByNumber(Math.round(finalGrade));
	}

	public static void modifyTextView(TextView textView, boolean boldText, int textSize, @ColorRes int textColor){
		textView.setTypeface(null, boldText ? Typeface.BOLD : Typeface.NORMAL);
		textView.setTextSize(textSize);
		textView.setTextColor(textView.getContext().getResources().getColor(textColor));
	}

	public static void clearTextViewModifications(TextView... textViews){
		for (TextView textView : textViews) {
			modifyTextView(textView, false, 15, android.R.color.tab_indicator_text);
		}
	}

	public static Bitmap getBitmapByUri(Uri uri, ContentResolver contentResolver) {
		try {
			return MediaStore.Images.Media.getBitmap(contentResolver, uri);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static LatLng StringToLatLng(String coordinates){
        if (coordinates == null || coordinates.isEmpty())
            return null;

        String[] latLng = coordinates.split(",");

        double latitude = Double.parseDouble(latLng[0]);
        double longitude = Double.parseDouble(latLng[1]);

        return new LatLng(latitude, longitude);
    }

    //TODO:Maybe make this better?
    public static String buildGeocodingUrl(String schoolName, String apiKey) {
        //The url requires plus signs instead of spaces
        schoolName = schoolName.replace(" ", "+");

        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + schoolName + "&key=" + apiKey;
    }

//	public static String getUriPath(Uri uri, ContentResolver contentResolver) {
//		Log.v("uri", "uri: " + uri.toString());
//		String[] projection = { MediaStore.Images.Media.DATA };
//		Cursor cursor = contentResolver.query(uri, projection, null, null, null);
//
//		if (cursor == null)
//			return null;
//
//		Log.v("cursor", "not null...");
//
//		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		cursor.moveToFirst();
//
//		Log.v("index", ":" + column_index);
//
//		String s = cursor.getString(column_index);
//		cursor.close();
//
//		Log.v("cursor", "string: " + s);
//
//		return s;
//	}
}

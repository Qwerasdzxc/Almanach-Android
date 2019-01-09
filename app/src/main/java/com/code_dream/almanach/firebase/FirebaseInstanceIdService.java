package com.code_dream.almanach.firebase;

import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;

/**
 * Created by Qwerasdzxc on 21-Dec-16.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

	@Override
	public void onTokenRefresh() {
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(Registry.SHARED_PREFS_FIREBASE_TOKEN_KEY, refreshedToken).apply();

		Log.w("Firebase", "Token refreshed:" + refreshedToken);

		FirebaseMessaging.getInstance().subscribeToTopic("news");
		UserInfo.FIREBASE_TOKEN = refreshedToken;
	}
}

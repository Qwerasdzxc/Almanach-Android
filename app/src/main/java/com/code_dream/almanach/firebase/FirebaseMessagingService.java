package com.code_dream.almanach.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.code_dream.almanach.R;
import com.code_dream.almanach.login.LoginActivity;

/**
 * Created by Qwerasdzxc on 21-Dec-16.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage){
		if (remoteMessage != null) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
			notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
			notificationBuilder.setContentText(remoteMessage.getNotification().getBody());

			notificationBuilder.setAutoCancel(true);
			notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
			notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
			notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(0, notificationBuilder.build());

			//notificationBuilder.setContentTitle(new JSONObject(remoteMessage.getData()).optString("user", "Invalid user"));
		}
	}
}

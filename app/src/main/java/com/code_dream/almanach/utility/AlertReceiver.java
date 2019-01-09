package com.code_dream.almanach.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.code_dream.almanach.R;
import com.code_dream.almanach.splash_screen.SplashScreenActivity;

/**
 * Created by Qwerasdzxc on 2/26/17.
 */

public class AlertReceiver extends BroadcastReceiver {

    public AlertReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: Change this according to the real Calendar event
        createNotification(context, "New Alert!", "You have a calendar event coming soon.");
    }

    public void createNotification(Context context, String notificationTitle, String notificationBody){

        PendingIntent notifyIntent = PendingIntent.getActivity(context, 0, new Intent(context, SplashScreenActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(notificationTitle);
        notificationBuilder.setContentText(notificationBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setLights(context.getResources().getColor(android.R.color.holo_blue_light), 500, 500);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        notificationBuilder.setContentIntent(notifyIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}

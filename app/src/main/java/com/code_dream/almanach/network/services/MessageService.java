package com.code_dream.almanach.network.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.code_dream.almanach.R;
import com.code_dream.almanach.chat.ChatActivity;
import com.code_dream.almanach.login.LoginActivity;
import com.code_dream.almanach.utility.OfflineDatabase;
import com.google.gson.Gson;
import com.code_dream.almanach.models.Message;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.network.requests.TokenConfirmationRequest;
import com.code_dream.almanach.utility.Registry;
import com.code_dream.almanach.utility.UserInfo;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Qwerasdzxc on 9/22/17.
 */

public class MessageService extends Service {

    private final MessageServiceInterface serviceInterface = new MessageServiceInterface();

    private Intent broadcastIntent = new Intent(Registry.MESSAGES_BROADCAST_NAME);
    private LocalBroadcastManager broadcaster;
    private MessageClientCallbacks listener;

    private WebSocketClient webSocketClient;
    private Person currentPerson;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.e("onStartCommand", "service started command");

        currentPerson = UserInfo.CURRENT_USER;

//        if (currentPerson != null && !isMessagingClientStarted())
            startMessagingClient();

        broadcaster = LocalBroadcastManager.getInstance(this);

        return START_STICKY;
    }

    public void startMessagingClient() {
        URI uri;
        try {
            uri = new URI(Registry.WEB_SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Log.e("starting", "Message Client starting...");

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("onOpen", "handshake successful :)");
                sendTokenConfirmation();

                broadcastIntent.putExtra("success", true);
                broadcaster.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onMessage(String data) {
                Log.e("onMessage", "Message received: " + data);
                Message receivedMessage = new Gson().fromJson(data, Message.class);
                if (data != null && !data.isEmpty())
                    if (listener != null)
                        listener.onMessageReceived(receivedMessage);

                showPushNotification(receivedMessage);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.e("onClose", "service closed successfully...");
                webSocketClient.connect();
            }

            @Override
            public void onError(Exception e) {
                Log.e("onError", "handshake un-successful :(");
                broadcastIntent.putExtra("success", false);
                broadcaster.sendBroadcast(broadcastIntent);
            }
        };

        webSocketClient.connect();
    }

    private boolean isMessagingClientStarted() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("onBind", "service bound");
        return serviceInterface;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.w("onTaskRemoved", "called");
        super.onTaskRemoved(rootIntent);

        listener = null;
        webSocketClient.close();

        Intent broadcastIntent = new Intent("com.code_dream.almanach.network.services.RestartServiceSensor");
        sendBroadcast(broadcastIntent);
    }

    public void sendMessage(Message message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(new Gson().toJson(message));
        }
    }

    private void sendTokenConfirmation() {
        Log.e("sendTokenConf", "client: " + (webSocketClient == null) + ", is open: " + webSocketClient.isOpen());
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(new Gson().toJson(new TokenConfirmationRequest(PreferenceManager.getDefaultSharedPreferences(this).getString(Registry.SHARED_PREFS_TOKEN_KEY, Registry.EMPTY_TOKEN))));
        }
    }

    private void showPushNotification(Message receivedMessage) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final int NOTIFICATION_ID = 1;
        final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(receivedMessage.getPerson())
                .setContentText(receivedMessage.getMessage());

        Log.w("showPushNot", "Now showing notification!");

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void addMessageClientListener(MessageClientCallbacks listener) {
        this.listener = listener;
    }

    public interface MessageClientCallbacks {

        void onMessageReceived(Message message);
    }

    public class MessageServiceInterface extends Binder {

        public void sendMessage(Message message) {
            MessageService.this.sendMessage(message);
        }

        public MessageService getServiceInstance() {
            return MessageService.this;
        }

        public boolean isClientActive() {
            return MessageService.this.isMessagingClientStarted();
        }
    }
}

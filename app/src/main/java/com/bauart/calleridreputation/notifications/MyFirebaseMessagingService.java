package com.bauart.calleridreputation.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bauart.calleridreputation.MainActivity;
import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.json.defaultResponse.DefaultResponse;
import com.bauart.calleridreputation.server.Server;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public static final int NOTIFICATION_ID = 5453;
    public static final String CHANNEL_SUBSCRIPTIONS = "Phone flagged";

    private NotificationChannel channelSubs;

    private NotificationCompat.Builder builder;

    public void createNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannel(channelSubs, CHANNEL_SUBSCRIPTIONS, CHANNEL_SUBSCRIPTIONS, getString(R.string.new_phone_was_flagged));
        }
    }

    private void createChannel(NotificationChannel channel, String id, String name, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            channel.setLightColor(Color.CYAN);
            channel.enableLights(true);
            channel.enableVibration(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        createNotificationChannels();
        try {
            String channel = remoteMessage.getNotification().getChannelId();
            sendMyNotification(remoteMessage.getNotification().getBody(), channel, remoteMessage.getData());
        } catch (NullPointerException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private void sendMyNotification(String message, String channel, Map<String, String> data)  {
        Intent intent = new Intent(this, MainActivity.class);
        Log.e(TAG, data.toString());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[] {300, 500, 200, 400, 100, 300})
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Server.saveFirebaseToken(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                //
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                //
            }
        }, s);
        super.onNewToken(s);
    }
}

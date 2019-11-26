package com.allen.learn.android.tutorial.broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.allen.learn.android.tutorial.R;
import com.allen.learn.android.tutorial.notification.NotificationActivity;

public class MyReplyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence messageText = getMessageText(intent);
        if (messageText!=null){
            String log = messageText.toString();
            Log.d(TAG, log);
            Toast.makeText(context, log, Toast.LENGTH_LONG).show();

            Notification repliedNotification = new Notification.Builder(context, NotificationActivity.channel_id)
                    .setSmallIcon(R.drawable.ic_menu_send)
                    .setContentText("reply")
                    .build();

// Issue the new notification.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(NotificationActivity.notificationId, repliedNotification);
        }
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NotificationActivity.KEY_TEXT_REPLY);
        }
        return null;
    }
}

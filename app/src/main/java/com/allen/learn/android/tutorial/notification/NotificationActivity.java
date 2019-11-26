package com.allen.learn.android.tutorial.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.allen.learn.android.tutorial.R;
import com.allen.learn.android.tutorial.broadcast.MyBroadcastReceiver;
import com.allen.learn.android.tutorial.broadcast.MyReplyBroadcastReceiver;
import com.allen.learn.android.tutorial.broadcast.SendLocalBroadcastActivity;

public class NotificationActivity extends AppCompatActivity {

    public static final String channel_id = "allen_channel_id";
    private String channel_name = "allen_channel_name";
    private String channel_desc = "allen_channel_desc";
    private String textTitle = "this is a text title";
    private String textContent = "this is a text content";
    public static final int notificationId = 565161;

    public static final String KEY_TEXT_REPLY = "key_text_reply";

    private Button basicNotificationBtn;
    private Button startActivityNotificationBtn;
    private Button actionButtonNotificationBtn;
    private Button replyButtonNotificationBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // 安卓8 以上必须先创建 notification channel
        this.createNotificationChannel();

        //basic notification
        basicNotificationBtn = findViewById(R.id.basicNotificationBtn);
        basicNotificationBtn.setOnClickListener(v->{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.ic_menu_share)
                    .setContentTitle(textTitle)
                    .setContentText(textContent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(notificationId, builder.build());
        });

        //start action notification
        startActivityNotificationBtn = findViewById(R.id.startActivityNotificationBtn);
        startActivityNotificationBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, SendLocalBroadcastActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.ic_menu_share)
                    .setContentTitle(textTitle)
                    .setContentText(textContent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        });

        // action button notification send broadcast
        actionButtonNotificationBtn = findViewById(R.id.actionButtonNotificationBtn);
        actionButtonNotificationBtn.setOnClickListener(v->{
            Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
            snoozeIntent.setAction("allen.notification.test");
            PendingIntent snoozePendingIntent =
                    PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.ic_menu_share)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(snoozePendingIntent)
                    .addAction(R.drawable.ic_menu_send, "test",
                            snoozePendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        });

        replyButtonNotificationBtn = findViewById(R.id.replyButtonNotificationBtn);
        replyButtonNotificationBtn.setOnClickListener(v->{
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel("label")
                    .build();

            Intent snoozeIntent = new Intent(this, MyReplyBroadcastReceiver.class);
            snoozeIntent.setAction("allen.notification.reply");

            PendingIntent replyPendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(),
                            1233,
                            snoozeIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_menu_send,
                            "action label", replyPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();

            NotificationCompat.Builder newMessageNotification = new NotificationCompat.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.ic_menu_share)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(action);

// Issue the notification.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, newMessageNotification.build());
        });







    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = channel_desc;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

package com.haru.pushtest;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.haru.push.Push;
import com.haru.push.PushReceiver;

public class PushNotiReceiver extends PushReceiver {

    @Override
    public void onMessage(Context context, Push push) {
        super.onMessage(context, push);

        // push message arrived
        Toast.makeText(context, push.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Notification onNotification(Context context, Push push) {
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class),
                        PendingIntent.FLAG_ONE_SHOT);

        return new NotificationCompat.Builder(context)
                .setContentTitle(push.getTitle())
                .setContentText(push.getMessage())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setTicker(push.getTitle())
                .build();
    }
}

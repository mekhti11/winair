package com.hititcs.dcs.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hititcs.dcs.R;
import com.hititcs.dcs.view.login.LoginActivity;
import timber.log.Timber;

public class BoardingDcsFirebaseService extends FirebaseMessagingService {

  @Override
  public void onNewToken(String s) {
    Timber.d("Firebase token %s", s);
    super.onNewToken(s);
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    Timber.d("Notification message " + remoteMessage.getFrom());
    if (remoteMessage.getData().size() > 0) {
      Timber.d("Data");
    }

    if (remoteMessage.getNotification() != null) {
      Timber.d("Notification");
      sendNotification(remoteMessage.getNotification().getBody());
    }
  }

  private void sendNotification(String messageBody) {
    try {
      Timber.d("New Notification");
      Intent intent = new Intent(this, LoginActivity.class);
      TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
      stackBuilder.addNextIntentWithParentStack(intent);

      PendingIntent resultPendingIntent =
          stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

      String channelId = "test";
      Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      NotificationCompat.Builder notificationBuilder =
          new NotificationCompat.Builder(this, channelId)
              .setSmallIcon(R.drawable.ic_notifications_black_24dp)
              .setContentTitle(getString(R.string.app_name))
              .setContentText(messageBody)
              .setAutoCancel(true)
              .setSound(defaultSoundUri)
              .setContentIntent(resultPendingIntent);

      NotificationManager notificationManager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(channelId,
            "Notification",
            NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
      }

      notificationManager.notify(0, notificationBuilder.build());
    } catch (Exception e) {
      FirebaseCrashlytics.getInstance().log("Firebase start service exception ");
      Timber.e(e);
    }
  }
}

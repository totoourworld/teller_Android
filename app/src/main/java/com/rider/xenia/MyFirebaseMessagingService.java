package com.rider.xenia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.util.NotificationUtils;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private NotificationManager mNotificationManager;
    private Controller controller;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        controller = (Controller) getApplicationContext();
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Map<String, String> data = (Map<String, String>) remoteMessage.getData();

        Log.e(TAG, "Notification Body: " + remoteMessage.getData().toString());

        String message = null;
        String tripStatus  = null;
        String tripId  = null;
        if (data.size() > 0) {
            message = data.get("price");
            tripStatus = data.get("trip_status");
            controller.pref.setTripStatus(tripStatus);
            tripId = data.get("trip_id");
        }

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        controller.setPushMessage(message);
        controller.setRemoteMessageData(data);
        if (Controller.isActivityVisible()) {
            // controller.setIspush(false);
            Intent intnt = new Intent("some_custom_id");
            // intnt.putExtras("",mymessage);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intnt);

        } else {

            controller.setPush(true);




            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);



            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(message);

            /*android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.appicon_driver)
                    .setContentTitle("HireMe")
                    .setContentText(message)
                    .setStyle(inboxStyle)
                    .setTicker("HireMe")
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true);*/
            //  mBuilder.setAutoCancel(true);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon( R.drawable.ic_stat_drive_eta)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setTicker(getString(R.string.app_name)).setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.ic_stat_drive_eta);
                mBuilder.setLargeIcon(largeIcon);
                mBuilder.setColor(getColor(R.color.theme));
            } else {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setLargeIcon(largeIcon);
            }
            mBuilder.setLights(Color.RED, 3000, 3000);
            mBuilder.setSound(uri);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.getNotification().flags = Notification.DEFAULT_LIGHTS|Notification.FLAG_AUTO_CANCEL;





// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, MainScreenActivity.class);

// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainScreenActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);

            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            final PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,

                            resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
             mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(10010, mBuilder.build());

        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mNotificationManager!=null){
                    mNotificationManager.cancel(10010);
                    timer.cancel();
                }


            }
        }, 60000, 30000);
    }


}

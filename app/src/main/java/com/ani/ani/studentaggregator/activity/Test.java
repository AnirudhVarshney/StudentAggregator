package com.devionlabs.ray.studentaggregator.activity;

import android.app.*;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.model.Attendance;
import com.devionlabs.ray.studentaggregator.model.Fee;

/**
 * A login screen that offers login via email/password.
 */
public class Test extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        context = Test.this;
        //showFeeNotification();
        //showAttendanceNotification();

        //test();
        show();
    }

    private void startNotification() {
        Intent notificationIntent = new Intent(context, Attendance.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.smallicon)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.fee))
                .setTicker("as")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("testtitle")
                .setContentText("test1");
        Notification n = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        }

        nm.notify(5, n);
    }

    private void show() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(android.app.Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Attendance");
        notificationBuilder.setContentText("test was marked present");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("test was marked present");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ActivityMenu.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notificationBuilder.build());
    }


    private void showFeeNotification() {

        Context context = Test.this;
        LocalDB localDB = new LocalDB(context);
        Fee fee = localDB.getNextPayment(localDB.getStudent().getId());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(android.app.Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Pending Fee");

        String studentName = localDB.getStudent().getName();
        notificationBuilder.setContentText(studentName + " fee is Due : Amount = " + fee.getAmount() + " INR");
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Pending Fee");


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Fee.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(10, notificationBuilder.build());
    }

    private void showAttendanceNotification() {

        String studentName = "test";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Attendance");

        notificationBuilder.setContentText(studentName + " was marked absent");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.absent2);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker(studentName + " was marked absent");


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Attendance.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(8, notificationBuilder.build());

    }//showAttendanceNotification function
}


package com.devionlabs.ray.studentaggregator.other;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.activity.ActivityMenu;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.helper.MyUtils;
import com.devionlabs.ray.studentaggregator.model.Attendance;
import com.devionlabs.ray.studentaggregator.model.Fee;

/**
 * Created by uma on 6/16/16.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private LocalDB localDB;
    private Context context;
    private static final int NOTIFICATION_ID_FEE = 010;
    Fee fee;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Alarmmmmmmed", Toast.LENGTH_LONG).show();
        Log.d(MyUtils.LOG_TAG, "Alarm receiver - Alarm Triggered");


        this.context = context;
        localDB = new LocalDB(context);


        fee = localDB.getNextPayment(localDB.getStudent().getId());
        show();

        /*
        Calendar calendarToday = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate;
        Date todaysDate = new Date();
        String dueTime = fee.getTime();

        try {
            inputDate = fmt.parse(dueTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        inputDate.setDate(inputDate.getDate() - 4);
        Log.d(MyUtils.LOG_TAG, "Alarm receiver - inputDate=" + inputDate);
        Log.d(MyUtils.LOG_TAG, "Alarm receiver - todaysDate=" + todaysDate);

        if (todaysDate.compareTo(inputDate) > 0) {
            show();
        }*/
    }

    private void show() {
        String studentName = localDB.getStudent().getName();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(android.app.Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Pending Fee");
        notificationBuilder.setContentText(studentName + " fee is Due : Amount = " + fee.getAmount() + " INR");
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Pending Fee");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ActivityMenu.class);
        // Adds the Intent that starts the Activity to the top of the stack
        Intent intent = new Intent(context, Attendance.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent contentIntent2 = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Attendance.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(contentIntent2);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notificationBuilder.build());
    }
}

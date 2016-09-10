package com.devionlabs.ray.studentaggregator.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.JSONParser;
import com.devionlabs.ray.studentaggregator.model.Attendance;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

//Created by Ray on 3/28/2016.
public class PushNotificationService extends GcmListenerService {

    private final static String LOG_TAG = "mylog";
    private DBHelper dbHelper;
    private Context con;


    GregorianCalendar startDate;
    GregorianCalendar endDate;
    DBHelper db;

    private static final int NOTIFICATION_ID_ATTENDANCE = 001;
    private static final int NOTIFICATION_ID_ABSENCE_REASON = 002;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        con = getApplicationContext();
        dbHelper = new DBHelper(con);
        String message = data.getString("message");
        Log.d(LOG_TAG, "PushNotificationService - server message" + message);
        JSONParser parser = new JSONParser();

        int type = parser.parseNotificationType(message);

        switch (type) {
            case JSONParser.NOTIFICATION_TYPE_ATTENDANCE:

                ArrayList<Attendance> attendanceList = parser.parseAttendance(message);

                // Un-Successful parsing
                if (attendanceList == null) {
                    String errorDescription = parser.getErrorDescription();
                    Log.d(LOG_TAG, "PushNotificationService - errorDescription = " + errorDescription);
                    Toast.makeText(con, errorDescription, Toast.LENGTH_LONG).show();
                }
                //Successful parsing
                else {
                    Attendance attendance = attendanceList.get(0);
                    showAttendanceNotification(attendance);
                    DBHelper db = new DBHelper(con);
                    db.addAttendance(attendance);
                }
                break;
        }


    }//onMessageReceived function

    private void showAttendanceNotification(Attendance attendance) {

        String studentName = dbHelper.getStudentName(attendance.getId());
        if (studentName == null) {
            return;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Attendance");

        //Present
        if (attendance.getStatus().equals("1")) {
            notificationBuilder.setContentText(studentName + " was marked present");
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.present2);
            notificationBuilder.setLargeIcon(bm);
            notificationBuilder.setTicker(studentName + " was marked present");
        }
        //Absent
        else {
            notificationBuilder.setContentText(studentName + " was marked absent");
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.absent2);
            notificationBuilder.setLargeIcon(bm);
            notificationBuilder.setTicker(studentName + " was marked absent");
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Attendance.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_ATTENDANCE, notificationBuilder.build());

        /*
        if (checkForPreviousAbsence(attendance.getDatetime())) {
            Log.d(LOG_TAG, "student was absent before");
            Log.d(LOG_TAG, "startDate = " + startDate.get(Calendar.DATE));
            Log.d(LOG_TAG, "endDate = " + endDate.get(Calendar.DATE));
            showAbsenceReasonNotification(studentName);
        } else {
            Log.d(LOG_TAG, "student was present before");
        }
        */


    }//showAttendanceNotification function

    private Boolean checkForPreviousAbsence(String stringDate) {
        //String stringDate = "2016-05-09 00:00:00";
        int year = Integer.parseInt(stringDate.substring(0, 4));
        int month = Integer.parseInt(stringDate.substring(5, 7));
        int date = Integer.parseInt(stringDate.substring(8, 10));

        //Log.d(LOG_TAG, "year=" + year + " month=" + month + " day=" + date);
        GregorianCalendar day = new GregorianCalendar();
        //month - 1 because GregorianCalendar month starts from 0 i.e jan=0
        day.set(year, month - 1, date, 0, 0, 0);

        //Log.d(LOG_TAG, "Current Present date = " + day.toString());
        day.add(Calendar.DATE, -1);
        //Log.d(LOG_TAG, "Start checking from = " + day.get(Calendar.DATE));
        //Day of week sun=1 mon=2 tue=3 wed=4 thu=5 fri=6 sat=7
        int dayOfWeek = day.get(Calendar.DAY_OF_WEEK);

        db = new DBHelper(con);
        //Log.d(LOG_TAG, "dayofweek=" + dayOfWeek);
        while (dayOfWeek == 1 || dayOfWeek == 7 || db.isHoliday(day)) {
            //Log.d(LOG_TAG, "1 skipping date " + day.get(Calendar.DATE));
            day.add(Calendar.DATE, -1);
            dayOfWeek = day.get(Calendar.DAY_OF_WEEK);
        }

        endDate = new GregorianCalendar();
        endDate.setTimeInMillis(day.getTimeInMillis());
        //Log.d(LOG_TAG, "1 end date=" + endDate.get(Calendar.DATE));
        //Log.d(LOG_TAG, "day=" + day);
        if (db.isPresent(day)) {
            //Log.d(LOG_TAG, "student was present earlier");
            return false;
        }
        day.add(Calendar.DATE, -1);

        if (!db.isPresent(day)) {
            //Log.d(LOG_TAG, "skipping date " + day.get(Calendar.DATE));
            day.add(Calendar.DATE, -1);
        }
        day.add(Calendar.DATE, 1);
        if (isAbsent(day)) {
            startDate = day;
            return true;
        }

        dayOfWeek = day.get(Calendar.DAY_OF_WEEK);
        while (dayOfWeek == 0 || db.isHoliday(day)) {
            day.add(Calendar.DATE, 1);
        }
        startDate = new GregorianCalendar();
        startDate.setTimeInMillis(day.getTimeInMillis());

        return true;
    }

    private Boolean isAbsent(GregorianCalendar day) {
        int dayOfWeek = day.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 0 || db.isHoliday(day) || db.isPresent(day)) {
            return false;
        }
        return true;
    }

    private void showAbsenceReasonNotification(String studentName) {


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Absence Reason");
        notificationBuilder.setContentText(studentName + " was absent before can you tell us why");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.absent2);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Reason for absence");


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Attendance.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID_ABSENCE_REASON, notificationBuilder.build());

    }//showAbsenceReasonNotification function

}//PushNotificationService class



/*    private void showNotification(String eventtext, Context ctx) {


        Log.d(TAG, "showNotification starting work ...");
        long[] vibratePattern = {9, 1, 9, 1};
        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(PushNotificationService.this);
        builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Student Aggregator Notification");
        builder.setContentText(eventtext);
        builder.setSmallIcon(R.drawable.s_noti);
        builder.setOngoing(true);
        //builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.solemn));
        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.setVibrate(vibratePattern);
        myNotication = builder.build();
        notif.notify(11, myNotication);
        Log.d(TAG, "showNotification ending work ...");
    }*/

package com.devionlabs.ray.studentaggregator.other;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by uma on 6/16/16.
 */
public class SampleBootReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "mylog";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, AlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);

            Calendar calendarToday = Calendar.getInstance();
            Calendar calendarTomorrow = Calendar.getInstance();
            calendarTomorrow.set(calendarTomorrow.get(Calendar.YEAR),
                    calendarTomorrow.get(Calendar.MONTH), (calendarTomorrow.get(Calendar.DATE) + 1), 9, 0, 0);

            long morningAt9 = calendarTomorrow.getTimeInMillis() - calendarToday.getTimeInMillis();
            int seconds = (int) (morningAt9 / 1000) % 60;
            int minutes = (int) ((morningAt9 / (1000 * 60)) % 60);
            int hours = (int) ((morningAt9 / (1000 * 60 * 60)) % 24);

            // Set the alarm for a particular time.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarToday.getTimeInMillis(),
                    morningAt9, alarmIntent);

            Log.d(LOG_TAG, "SampleBootReceiver - Alarm scheduled after : seconds=" + seconds + " minutes=" + minutes + " hours=" + hours);

        }
    }
}

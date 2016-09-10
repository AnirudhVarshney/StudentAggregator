package com.devionlabs.ray.studentaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2 * 1000;
    private static final String LOG_TAG = "mylog";
    private static final String PROJECT_NUMBER = "804375968805";

    LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        localDB = new LocalDB(SplashScreen.this);

/*
        //just for testing
        Intent i = new Intent(SplashScreen.this, ActivityMenu.class);
        startActivity(i);
        finish();
*/

        splashAndGoNext();
    }

    public void splashAndGoNext() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i;
                if (!localDB.isUserLoggedIn()) {
                    Log.d(LOG_TAG, "SplashScreen - No user is currently logged in");
                    i = new Intent(SplashScreen.this, Login.class);
                } else {
                    //now the user is logged in
                    Log.d(LOG_TAG, "SplashScreen - User is Logged in");

                    //No student was added before
                    if (!localDB.isStudentAdded()) {
                        Log.d(LOG_TAG, "SplashScreen - no student was added before");
                        i = new Intent(SplashScreen.this, AddStudent.class);
                    }
                    //All good user can proceed
                    else {
                        Log.d(LOG_TAG, "SplashScreen - Students were added before");
                        i = new Intent(SplashScreen.this, ActivityMenu.class);
                    }
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}//end----------------------------------------------------------------------------------------------


        /*
        //localDB.setGCMID("1234567890");

        if (localDB.getGCMID() == null) {
            //Log.d(LOG_TAG,"get GCM ID");
            getGCMId();
        } else {
            //Log.d(LOG_TAG,"Go next");
            splashAndGoNext();
        }
        */

//Attendance attendance = new Attendance("1", "1", "1", "2016-07-15");
//showAttendanceNotification(attendance);


/*

   private void showAttendanceNotification(Attendance attendance) {

        DBHelper dbHelper = new DBHelper(SplashScreen.this);
        String studentName = dbHelper.getStudentName(attendance.getId());
        if (studentName == null) {
            return;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.smallicon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(android.app.Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("Attendance");


        notificationBuilder.setContentText(studentName + " was marked present");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.present2);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker(studentName + " was marked present");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AttendanceActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notificationBuilder.build());

    }//showAttendanceNotification function

    public void getGCMId() {
        if (!ServerUtilities.hasActiveInternetConnection(SplashScreen.this)) {
            Toast.makeText(SplashScreen.this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {

            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d(LOG_TAG, "SplashScreen - GCM Registration id" + registrationId);
                localDB.setGCMID(registrationId);

                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                Fabric.with(SplashScreen.this, new Crashlytics());
                Log.d(LOG_TAG, "SplashScreen - Unable to register with GCM");
            }
        });
    }

* */

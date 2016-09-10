package com.devionlabs.ray.studentaggregator.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.GetUserCallback;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.JSONParser;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.helper.ServerHelper;
import com.devionlabs.ray.studentaggregator.model.Attendance;
import com.devionlabs.ray.studentaggregator.model.Fee;
import com.devionlabs.ray.studentaggregator.model.Holiday;

import java.util.ArrayList;

/**
 * Created by Ray on 4/26/2016.
 */
public class Initialize extends AppCompatActivity {

    private ServerHelper serverHelperForAttendance = null;
    private ServerHelper serverHelperForHoliday = null;
    private ServerHelper serverHelperForFee = null;
    private ServerHelper serverHelperForNextFee = null;
    private static final String LOG_TAG = "mylog";
    private String studentId;
    private DBHelper dbHelper;
    Boolean hasAttendance, hasHoliday, hasFee, cancel, hasNextPayment;

    TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        dbHelper = new DBHelper(Initialize.this);
        cancel = hasAttendance = hasHoliday = hasFee = hasNextPayment = false;

        Log.d("mylog", "Initialize - Initializing Application");

        studentId = new LocalDB(Initialize.this).getStudent().getId();

        Log.d(LOG_TAG, "Initialize - Request all data for Student ID = " + studentId);

        getAttendance();
        getHoliday();
        getFee();
        getNextPayment();
        //Todo getEvents
    }


    public void getAttendance() {

        String AttendanceURL = "http://www.studentaggregator.org/getattendance.php";

        ContentValues postParamsForAttendance = new ContentValues();
        postParamsForAttendance.put("id", studentId);

        serverHelperForAttendance = new ServerHelper(AttendanceURL, postParamsForAttendance, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForAttendance.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    Log.d(LOG_TAG, "Initialize - done - errorDescription = " + errorDescription);
                    Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
                    cancel = true;
                    return;
                }
                parseReplyForAttendance(reply);
            }
        });
        serverHelperForAttendance.execute((Void) null);
    }//getAttendance

    private void parseReplyForAttendance(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Attendance> attendanceList = parser.parseAttendance(reply);

        // Un-Successful parsing
        if (attendanceList == null) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "Initialize - parseReplyForAttendance - errorDescription = " + errorDescription);
            Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        Log.d(LOG_TAG, "Initialize - Attendance Added");
        dbHelper.addAttendance(attendanceList);
        hasAttendance = true;
        onCompleteInitializing();
    }//parseReplyForAttendance

    public void getHoliday() {

        String HolidayURL = "http://www.studentaggregator.org/getholiday.php";

        ContentValues postParamsForHoliday = new ContentValues();
        postParamsForHoliday.put("id", studentId);

        serverHelperForHoliday = new ServerHelper(HolidayURL, postParamsForHoliday, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForHoliday.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    Log.d(LOG_TAG, "Initialize - done - errorDescription = " + errorDescription);
                    Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
                    cancel = true;
                    return;
                }
                parseReplyForHoliday(reply);
            }
        });
        serverHelperForHoliday.execute((Void) null);
    }//getHoliday

    private void parseReplyForHoliday(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Holiday> holidayList = parser.parseHoliday(reply);

        // Un-Successful parsing
        if (holidayList == null) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "Initialize - parseReplyForHoliday - errorDescription = " + errorDescription);
            Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        Log.d(LOG_TAG, "Initialize - Holiday Added");
        dbHelper.addHoliday(holidayList);
        hasHoliday = true;
        onCompleteInitializing();
    }//parseReplyForHoliday

    public void getFee() {

        String FeeURL = "http://www.studentaggregator.org/getfee.php";

        ContentValues postParamsForFee = new ContentValues();
        postParamsForFee.put("id", studentId);

        serverHelperForFee = new ServerHelper(FeeURL, postParamsForFee, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForFee.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    Log.d(LOG_TAG, "Initialize - done - errorDescription = " + errorDescription);
                    Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
                    cancel = true;
                    return;
                }
                parseReplyForFee(reply);
            }
        });
        serverHelperForFee.execute((Void) null);
    }//getFee

    private void parseReplyForFee(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Fee> feesList = parser.parseFee(reply);

        // Un-Successful parsing
        if (feesList == null) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "Initialize - parseReplyForAttendance - errorDescription = " + errorDescription);
            Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        Log.d(LOG_TAG, "Initialize - Fee Added");
        dbHelper.addFee(feesList);
        hasFee = true;
        onCompleteInitializing();
    }//parseReplyForFee

    public void getNextPayment() {

        String nextFeeURL = "http://www.studentaggregator.org/getnextpayment.php";

        ContentValues postParamsForFee = new ContentValues();
        postParamsForFee.put("id", studentId);

        serverHelperForNextFee = new ServerHelper(nextFeeURL, postParamsForFee, new GetUserCallback() {
            @Override
            public void done() {
                String reply = serverHelperForNextFee.getReply();
                if (reply == null) {
                    String errorDescription = getResources().getString(R.string.no_internet_warning);
                    Log.d(LOG_TAG, "Initialize - errorDescription = " + errorDescription);
                    Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
                    cancel = true;
                    return;
                }
                parseReplyForNextPayment(reply);
            }
        });
        serverHelperForNextFee.execute((Void) null);
    }//getFee

    private void parseReplyForNextPayment(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        ArrayList<Fee> feesList = parser.parseFee(reply);

        // Un-Successful parsing
        if (feesList == null) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "Initialize - parseReplyForAttendance - errorDescription = " + errorDescription);
            Toast.makeText(Initialize.this, errorDescription, Toast.LENGTH_LONG).show();
            cancel = true;
            return;
        }
        Log.d(LOG_TAG, "Initialize - Next Payment Added");

        LocalDB localdb = new LocalDB(Initialize.this);
        localdb.saveNextPayment(feesList.get(0));
        hasNextPayment = true;
        onCompleteInitializing();
    }//parseReplyForFee


    public void onCompleteInitializing() {
        /*if (!hasAttendance) {
            Toast.makeText(Initialize.this, "Unable to read Attendance", Toast.LENGTH_LONG).show();
        } else if (!hasHoliday) {
            Toast.makeText(Initialize.this, "Unable to read Holiday", Toast.LENGTH_LONG).show();
        } else if (!hasFee) {
            Toast.makeText(Initialize.this, "Unable to read Fee", Toast.LENGTH_LONG).show();
        }*/
        if (cancel) {
            Log.d(LOG_TAG, "Initialize - Error in initializing : hasAttendance=" + hasAttendance +
                    " hasFee=" + hasFee + " hasHoliday=" + hasHoliday);
            //reset student bcoz of error
            LocalDB localDB = new LocalDB(Initialize.this);
            localDB.clear();
            dbHelper.clear();

            textViewMessage = (TextView) findViewById(R.id.textViewMessage);
            textViewMessage.setText("Oops something went wrong, Please try again");
            Intent i = new Intent(Initialize.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        } else if (hasAttendance && hasHoliday && hasFee && hasNextPayment) {
            Log.d(LOG_TAG, "Initialize - Everything is initialized successfully");
            Intent i = new Intent(Initialize.this, ActivityMenu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }


}

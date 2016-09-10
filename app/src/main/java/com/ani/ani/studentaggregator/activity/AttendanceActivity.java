package com.devionlabs.ray.studentaggregator.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.model.Holiday;
import com.devionlabs.ray.studentaggregator.model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Ray on 4/20/2016.
 */

public class AttendanceActivity extends AppCompatActivity {

    private final static String LOG_TAG = "mylog";
    final Context context = AttendanceActivity.this;
    int finalDayofmonth;

    //UI refrences
    private ProgressBar progressBar;
    private ImageButton imageButtonPrevMonth;
    private ImageButton imageButtonNextMonth;
    private TextView textViewSelectedDate;

    //class variables
    private GregorianCalendar selectedDate;
    private GregorianCalendar sessionStartDate;
    private GregorianCalendar sessionEndDate;
    GregorianCalendar gcNow;

    private String[] monthName = new String[]{"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar);

        //initialize fields
        selectedDate = new GregorianCalendar();
        selectedDate.set(Calendar.DATE, 1);
        gcNow = new GregorianCalendar();

        sessionStartDate = new GregorianCalendar();
        sessionStartDate.set(2016, 4 - 1, 1, 0, 0, 0);
        sessionEndDate = new GregorianCalendar();
        sessionEndDate.set(2017, 3 - 1, 31, 0, 0, 0);


        linkUIComponents();
        seOnClickListeners();
        showCalendarForMonth();
    }


    private void linkUIComponents() {
        imageButtonPrevMonth = (ImageButton) findViewById(R.id.imageButtonPrevMonth);
        imageButtonNextMonth = (ImageButton) findViewById(R.id.imageButtonNextMonth);
        textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setProgress(85);
        }
    }


    private void seOnClickListeners() {
        imageButtonNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar temp = new GregorianCalendar();
                temp.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DATE), 0, 0, 0);
                if (!(temp.get(Calendar.YEAR) <= sessionEndDate.get(Calendar.YEAR) && temp.get(Calendar.MONTH) - 1 == sessionEndDate.get(Calendar.MONTH))) {
                    selectedDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH) + 1);
                    showCalendarForMonth();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Academic Session ends in March " + sessionEndDate.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageButtonPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar temp = new GregorianCalendar();
                temp.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) - 1, selectedDate.get(Calendar.DATE), 0, 0, 0);
                if (!(temp.get(Calendar.YEAR) <= sessionStartDate.get(Calendar.YEAR) && temp.get(Calendar.MONTH) + 1 == sessionStartDate.get(Calendar.MONTH))) {
                    selectedDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH) - 1);
                    showCalendarForMonth();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Academic Session starts in April " + sessionStartDate.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /*---------------------------------showCalendarForMonth---------------------------------------*/
    private void showCalendarForMonth() {

        //Set textViewSelectedDate text
        String stringSelectedDate = monthName[selectedDate.get(Calendar.MONTH)] + " " + selectedDate.get(Calendar.YEAR);
        textViewSelectedDate.setText(stringSelectedDate);
        setupInitialCalendar();
        if (gcNow.get(Calendar.MONTH) >= selectedDate.get(Calendar.MONTH) && gcNow.get(Calendar.YEAR) >= selectedDate.get(Calendar.YEAR)) {
            setupAttendance();
        }
        setupWeekend();
        //todo
        setupHolidays();

    }//showCalendarForMonth

    private void setupHolidays() {

        DBHelper db = new DBHelper(context);

        ArrayList<Holiday> holidaylist;
        Holiday holiday;
        holidaylist = db.getHolidaysForMonth(selectedDate);
        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 tue=3 wed=4 thu=5 fri=6 sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so now mon = 0 ... and sun = 6
        dayofmonth -= 1;

        if (holidaylist == null) {
            return;
        }

        int noofholidays = holidaylist.size();
        Resources res = getResources();

        for (int i = 0; i < noofholidays; i++) {

            holiday = holidaylist.get(i);

            int my = Integer.parseInt(holiday.getDate().charAt(8) + "" + holiday.getDate().charAt(9));
            final String hDescription = holiday.getDescription();
            int place = dayofmonth + my - 1;


            Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));
            if (b != null) {
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHoliday));
                b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                        dialog.setTitle("Holiday");
                        dialog.setMessage("" + hDescription);
                        dialog.setCancelable(true);
                        dialog.show();
                    }
                });
            }//if b!= null
        }//end for loop

    }//setupHolidays

    private void setupWeekend() {

        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 ... and sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so mon = 0 ... and sun = 6
        dayofmonth -= 1;

        int lastdate = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);


        int place, i;
        Resources res = getResources();
        for (i = 0; i < lastdate; i++) {
            place = dayofmonth + i;
            GregorianCalendar gc = new GregorianCalendar();
            gc.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), 0, 0, 0);
            gc.set(Calendar.DATE, i + 1);
            if (gc.get(Calendar.DAY_OF_WEEK) == 1 || gc.get(Calendar.DAY_OF_WEEK) == 7) {
                Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));

                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreyBackground));
                    b.setTextColor(ContextCompat.getColor(context, R.color.colorSoftText));
                    b.setOnClickListener(null);
                }
            }
        }

    }


    /*---------------------------------setInitialCalendar----------------------------------------*/
    public void setupInitialCalendar() {

        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 ... and sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so mon = 0 ... and sun = 6
        dayofmonth -= 2;

        int lastdate = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        //Log.d("mylog", "Last date of Required month = " + lastdate);
        //Log.d("mylog", "First day of month starts on = " + dayofmonth);

        int total = 0, count = 1, i;
        Resources res = getResources();

        //skip blank days of week
        for (i = total; i < dayofmonth; i++) {
            Button b = (Button) findViewById(res.getIdentifier("b" + (total + 1), "id", getPackageName()));
            //android says so :p
            if (b != null) {
                b.setText("");
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setOnClickListener(null);
            }
            total++;
        }//end 1st for loop

        //initialize normal days
        for (i = total; count <= lastdate; i++) {
            Button b = (Button) findViewById(res.getIdentifier("b" + (total + 1), "id", getPackageName()));
            String date = "" + count++;
            //android says so :p
            if (b != null) {
                b.setText(date);
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setTextColor(ContextCompat.getColor(context, R.color.colorSoftText));
                b.setOnClickListener(null);
            }
            total++;
        }//end 2nd for loop

        //skip left blank days
        for (i = 0; i < 42 - total; i++) {
            Button b = (Button) findViewById(res.getIdentifier("b" + (total + 1 + i), "id", getPackageName()));
            if (b != null) {
                b.setText("");
                b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                b.setOnClickListener(null);
            }
        }//end 3rd for loop

    }//setInitialCalendar


    /*---------------------------------setupAttendance--------------------------------------------*/
    private void setupAttendance() {

        DBHelper db = new DBHelper(context);
        int lastdate = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        boolean[] done = new boolean[lastdate + 1];
        final String[] arrayDescription = new String[lastdate + 1];

        int x;
        for (x = 0; x < lastdate + 1; x++) {
            done[x] = false;
            arrayDescription[x] = "Unknown";
        }
        ArrayList<com.devionlabs.ray.studentaggregator.model.Attendance> attendanceList;
        com.devionlabs.ray.studentaggregator.model.Attendance attendance;
        Student student = new LocalDB(context).getStudent();
        attendanceList = db.getAttendanceForMonth(student.getId(), selectedDate);

        Resources res = getResources();
        int dayofmonth = selectedDate.get(Calendar.DAY_OF_WEEK);
        //dayofmonth will skip blank days of first week
        //sun=1 mon=2 ... and sat = 7 but our first day is monday and should not be skipped
        //for this we need to make mon=0 and sun=7
        dayofmonth = dayofmonth == 1 ? 8 : dayofmonth;
        //so mon = 0 ... and sun = 6
        dayofmonth -= 2;

        //show absent if list is null
        if (attendanceList == null) {
            int place = dayofmonth;
            for (x = 1; x < lastdate + 1; x++) {
                place++;
                //Log.d(LOG_TAG, x + " " + done[x]);
                Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));

                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAbsent));

                    b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    final int finalX = x;
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ContextThemeWrapper themedContext;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
                            } else {
                                themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Light_NoTitleBar);
                            }
                            AlertDialog.Builder dialog = new AlertDialog.Builder(themedContext);
                            dialog.setTitle("Absence Reason");
                            dialog.setMessage("" + arrayDescription[finalX]);
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    });
                }
            }//end for
            return;
        }

        //if attendence is there then
        int noOfAttendance = attendanceList.size();

        //loop runs through all attendance onw by one
        for (int i = 0; i < noOfAttendance; i++) {

            attendance = attendanceList.get(i);

            Log.d(LOG_TAG, "attendance" + i + "=" + attendance.toString());
            final String hstatus = attendance.getStatus();
            final String hdescription = attendance.getDesc();
            final String hdatetime = attendance.getDatetime();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date inputDate;

            try {
                inputDate = fmt.parse(hdatetime);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(inputDate);

            done[cal.get(Calendar.DATE)] = hstatus.equals("1");
            if (!done[cal.get(Calendar.DATE)]) {
                arrayDescription[cal.get(Calendar.DATE)] = hdescription;
            }
        }//end of for loop

        //check for this month
        if (gcNow.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) && gcNow.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)) {
            lastdate = gcNow.get(Calendar.DATE);
            Log.d(LOG_TAG, "lastdate=" + lastdate);
        }


        int place = dayofmonth;
        for (x = 1; x < lastdate + 1; x++) {
            place++;
            //Log.d(LOG_TAG, x + " " + done[x]);
            final Button b = (Button) findViewById(res.getIdentifier("b" + (place), "id", getPackageName()));
            if (done[x]) {
                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPresent));
                    b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                }
            } else {
                if (b != null) {
                    b.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAbsent));
                    b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    final int finalX = x;
                    finalDayofmonth = place;
                    Log.d(LOG_TAG, "finalDayofmonth" + finalDayofmonth);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ContextThemeWrapper themedContext;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
                            } else {
                                themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Light_NoTitleBar);
                            }

                            if (!arrayDescription[finalX].equals("Unknown")) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(themedContext);
                                dialog.setTitle("Absence Reason");
                                dialog.setMessage("" + arrayDescription[finalX]);
                                dialog.setCancelable(true);
                                dialog.show();
                            }
                            //if reason is unknown
                            else {
                                final Dialog dialog = new Dialog(new ContextThemeWrapper(context,
                                        android.R.style.Theme_Holo_Light_Dialog_MinWidth));
                                dialog.setContentView(R.layout.dialog_absence_unknown);
                                dialog.setTitle("Absence reason");

                                View buttonRectification = dialog.findViewById(R.id.buttonRectification);
                                View buttonApplication = dialog.findViewById(R.id.buttonApplication);

                                buttonRectification.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        final Dialog dialog2 = new Dialog(new ContextThemeWrapper(context,
                                                android.R.style.Theme_Holo_Light_Dialog_MinWidth));
                                        dialog2.setContentView(R.layout.dialog_simple);
                                        dialog2.setTitle("Attendance Rectification");

                                        TextView textViewContent = (TextView) dialog2.findViewById(R.id.textViewContent);
                                        View buttonCancel = dialog2.findViewById(R.id.buttonCancel);
                                        View buttonSend = dialog2.findViewById(R.id.buttonSend);

                                        LocalDB localDB = new LocalDB(context);
                                        Student student = localDB.getStudent();
                                        textViewContent.setText(student.getName() + " was present.\nPlease check and resolve his attendance");
                                        dialog2.show();
                                        buttonCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog2.dismiss();
                                            }
                                        });
                                        buttonSend.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog2.dismiss();
                                                Toast.makeText(context, "We have received your request", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                buttonApplication.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent i = new Intent(context, Absence.class);
                                        String date = b.getText().toString();
                                        i.putExtra("date", "" + date);
                                        i.putExtra("month", "" + (selectedDate.get(Calendar.MONTH) + 1));
                                        i.putExtra("year", "" + selectedDate.get(Calendar.YEAR));
                                        Log.d(LOG_TAG, "Attendance - " + date + "-" + selectedDate.get(Calendar.MONTH) + "-" + selectedDate.get(Calendar.YEAR));
                                        //Toast.makeText(context,""+ finalDayofmonth,Toast.LENGTH_LONG).show();
                                        startActivity(i);
                                    }
                                });

                                dialog.show();
                            }//else
                        }
                    });
                }
            }
        }//end for
    }//setupAttendance()

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            //todo check other id also
            Intent i = new Intent(context, ActivityMenu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCalendarForMonth();
    }

}//end Activity
package com.devionlabs.ray.studentaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.model.Student;

import java.util.ArrayList;

/**
 * Created by Ray on 11-Jun-16.
 */
public class Absence extends AppCompatActivity {

    private String LOG_TAG = "mylog";
    private RadioGroup radioGroupReason;
    private EditText editTextReason;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        String mdatetime = null;
        Bundle extras = getIntent().getExtras();

        String date = extras.getString("date");
        String month = extras.getString("month");
        String year = extras.getString("year");
        if (date.length() == 1) {
            date = "0" + date;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }

        mdatetime = year + "-" + month + "-" + date + " 00:00:00";
        Log.d(LOG_TAG, "date=" + mdatetime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar);

        BindUIComponents();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        radioGroupReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch (v.getId()) {
                    case R.id.radioButton6:
                        editTextReason.setVisibility(View.VISIBLE);
                        break;
                    default:
                        editTextReason.setVisibility(View.GONE);
                        break;
                }
            }
        });

        radioGroupReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton6:
                        editTextReason.setVisibility(View.VISIBLE);
                        break;
                    default:
                        editTextReason.setVisibility(View.GONE);
                        break;
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = radioGroupReason.getCheckedRadioButtonId();
                String reason = editTextReason.getText().toString();

                switch (id) {
                    case R.id.radioButton1:
                        reason = "Health issue";
                        break;
                    case R.id.radioButton2:
                        reason = "Family Function";
                        break;
                    case R.id.radioButton3:
                        reason = "Rainy day";
                        break;
                    case R.id.radioButton4:
                        reason = "Natural Disaster";
                        break;
                    case R.id.radioButton5:
                        reason = "Out of station";
                        break;
                    case R.id.radioButton6:
                        reason = editTextReason.getText().toString();
                        break;
                }

                Toast.makeText(Absence.this, "We have received your request", Toast.LENGTH_LONG).show();
                ArrayList<com.devionlabs.ray.studentaggregator.model.Attendance> attendanceList = new ArrayList<com.devionlabs.ray.studentaggregator.model.Attendance>();


                LocalDB localDB = new LocalDB(Absence.this);
                Student student = localDB.getStudent();
                String mid = student.getId();
                String mstatus = "0";
                String mdesc = reason;
                String mdatetime = null;
                //2016-04-17 08:55:00

                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    mdatetime = null;
                } else {
                    String date = extras.getString("date");
                    String month = extras.getString("month");
                    String year = extras.getString("year");
                    if (date.length() == 1) {
                        date = "0" + date;
                    }
                    if (month.length() == 1) {
                        month = "0" + month;
                    }

                    mdatetime = year + "-" + month + "-" + date + " 00:00:00";

                    com.devionlabs.ray.studentaggregator.model.Attendance attendance = new com.devionlabs.ray.studentaggregator.model.Attendance(mid, mstatus, mdesc, mdatetime);
                    Log.d(LOG_TAG, "Absense attendance=" + attendance);
                    attendanceList.add(attendance);
                    DBHelper db = new DBHelper(Absence.this);
                    db.addAttendance(attendance);
                }
                Log.d(LOG_TAG, mdatetime);
                finish();
            }
        });
    }

    private void BindUIComponents() {
        radioGroupReason = (RadioGroup) findViewById(R.id.radioGroupReason);
        editTextReason = (EditText) findViewById(R.id.editTextReason);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            //todo check other id also
            Intent i = new Intent(Absence.this, ActivityMenu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }
}

package com.devionlabs.ray.studentaggregator.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.GetUserCallback;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.JSONParser;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.helper.ServerHelper;
import com.devionlabs.ray.studentaggregator.model.Student;
import com.devionlabs.ray.studentaggregator.model.User;

import java.util.GregorianCalendar;

/**
 * Created by Ray on 11-Jun-16.
 */
public class StudentVerification extends AppCompatActivity {

    private static String LOG_TAG = "mylog";
    private ServerHelper serverHelper;
    Student student;
    User user;
    LocalDB localDB;

    // UI references
    private EditText editTextCode;
    private Button buttonSubmit;
    private TextView textViewNumber;
    private LinearLayout linearLayoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_verification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        bindUIComponents();
        setOnClickListeners();

        localDB = new LocalDB(StudentVerification.this);
        student = localDB.getTempStudent();
        user = localDB.getUser();


        String contact = "+91 - " + student.getContact();
        textViewNumber.setText(contact);
    }

    private void setOnClickListeners() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    editTextCode.setError(getString(R.string.error_field_required));
                    editTextCode.requestFocus();
                    return;
                }
                if (!isCodeValid(code)) {
                    editTextCode.setError(getString(R.string.error_invalid_code));
                    editTextCode.requestFocus();
                    return;
                }

                showProgress(true);
                ContentValues postParams = new ContentValues();
                postParams.put("studentid", student.getId());
                postParams.put("guardianid", user.getId());
                postParams.put("code", code);

                String url = "http://www.studentaggregator.org/studentverification.php";
                serverHelper = new ServerHelper(url, postParams, new GetUserCallback() {
                    @Override
                    public void done() {
                        showProgress(false);
                        String reply = serverHelper.getReply();
                        if (reply == null) {
                            String errorDescription = getResources().getString(R.string.no_internet_warning);
                            Log.d(LOG_TAG, "StudentVerification - errorDescription = " + errorDescription);
                            Toast.makeText(StudentVerification.this, errorDescription, Toast.LENGTH_LONG).show();
                            return;
                        }
                        parseReply(reply);
                    }
                });
                serverHelper.execute((Void) null);
                //Toast.makeText(StudentVerification.this, "Working", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseReply(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        Boolean success = parser.parseSuccess(reply);

        // Un-Successful parsing
        if (!success) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "StudentVerification - errorDescription = " + errorDescription);
            Toast.makeText(StudentVerification.this, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            Log.d(LOG_TAG, "StudentVerification - Verification Successful");
            localDB.setStudent(student);

            DBHelper db = new DBHelper(StudentVerification.this);
            db.addStudent(student);

            Intent i = new Intent(StudentVerification.this, Initialize.class);
            startActivity(i);
            finish();
        }
    }//parseReply

    private boolean isCodeValid(String code) {
        Log.d(LOG_TAG, "code.length=" + code.length());
        if (code.length() == 6)
            return true;
        else return false;
    }

    private void bindUIComponents() {
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress);
    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(StudentVerification.this, AddStudent.class);
        startActivity(i);
        return true;
    }//onOptionsItemSelected

    /**
     * Shows or hides the progress UI
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            linearLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            linearLayoutProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    linearLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            linearLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }//showProgress

}

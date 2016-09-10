package com.devionlabs.ray.studentaggregator.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.GetUserCallback;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.JSONParser;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.helper.ServerHelper;
import com.devionlabs.ray.studentaggregator.helper.ServerUtilities;
import com.devionlabs.ray.studentaggregator.model.Student;
import com.devionlabs.ray.studentaggregator.model.User;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddStudent extends AppCompatActivity {

    private static String LOG_TAG = "mylog";
    private ServerHelper serverHelper = null;
    private boolean datePickerVisible;
    private GregorianCalendar calDateDOB;
    private String guardianID;

    // UI references
    private EditText editTextName;
    private EditText editTextDOB;
    private EditText editTextFather;
    private EditText editTextMother;
    private Button buttonAddStudent;
    private LinearLayout linearLayoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        datePickerVisible = false;
        calDateDOB = new GregorianCalendar();

        LocalDB localDB = new LocalDB(AddStudent.this);
        User user = localDB.getUser();
        guardianID = user.getId();

        bindUIComponents();
        setOnclickListeners();

    }//onCreate

    private void bindUIComponents() {
        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);
        editTextName = (EditText) findViewById(R.id.editTextStudentName);
        editTextDOB = (EditText) findViewById(R.id.editTextDOB);
        editTextFather = (EditText) findViewById(R.id.editTextFathersName);
        editTextMother = (EditText) findViewById(R.id.editTextMothersName);
        linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress);
    }//bindUIComponents

    public void setOnclickListeners() {

        editTextMother.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editTextadd || id == EditorInfo.IME_NULL) {
                    attemptFetchStudent();
                    return true;
                }
                return false;
            }
        });

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ServerUtilities.hasActiveInternetConnection(AddStudent.this)) {
                    attemptFetchStudent();
                } else {
                    Toast.makeText(AddStudent.this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
                }
            }
        });


        //listener for date picker
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //date picker will start from last picked date otherwise today
                String tempString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editTextDOB.setText(tempString);
                calDateDOB.set(Calendar.DATE, dayOfMonth);
                calDateDOB.set(Calendar.MONTH, monthOfYear);
                calDateDOB.set(Calendar.YEAR, year);

                datePickerVisible = false;
            }
        };

        editTextDOB.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (!datePickerVisible) {
                    DatePickerDialog dpd = new DatePickerDialog(AddStudent.this, listener,
                            (calDateDOB.get(Calendar.YEAR)), calDateDOB.get(Calendar.MONTH), calDateDOB.get(Calendar.DATE));
                    dpd.setTitle("Choose Date Of Birth");
                    dpd.show();
                    datePickerVisible = true;
                    return true;
                } else {
                    datePickerVisible = false;
                    return false;
                }
            }
        });
        editTextDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!datePickerVisible) {
                        DatePickerDialog dpd = new DatePickerDialog(AddStudent.this, listener,
                                (calDateDOB.get(Calendar.YEAR)), calDateDOB.get(Calendar.MONTH), calDateDOB.get(Calendar.DATE));
                        dpd.setTitle("Choose Date Of Birth");
                        dpd.show();
                        datePickerVisible = true;
                    } else {
                        datePickerVisible = false;
                    }
                }
            }
        });
    }//setOnclickListeners

    private void attemptFetchStudent() {

        //Reset errors
        editTextName.setError(null);
        editTextDOB.setError(null);
        editTextFather.setError(null);
        editTextMother.setError(null);

        // Store values at the time of the login attempt.
        String name = editTextName.getText().toString();
        String dob = editTextDOB.getText().toString();
        String father = editTextFather.getText().toString();
        String mother = editTextMother.getText().toString();

        //cancel will be set to true if there are form errors (invalid email, missing fields, etc.)
        //and field will be focused
        boolean cancel = false;
        View focusView = null;

        // Check for a valid mothers name.
        if (TextUtils.isEmpty(mother)) {
            editTextMother.setError(getString(R.string.error_field_required));
            focusView = editTextMother;
            cancel = true;
        }
        // Check for a valid fathers name.
        if (TextUtils.isEmpty(father)) {
            editTextFather.setError(getString(R.string.error_field_required));
            focusView = editTextFather;
            cancel = true;
        }
        // Check for a valid date of birth.
        if (TextUtils.isEmpty(dob)) {
            editTextDOB.setError(getString(R.string.error_field_required));
            focusView = editTextDOB;
            cancel = true;
        }
        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);
            ContentValues postParams = new ContentValues();
            postParams.put("name", name);
            postParams.put("dob", dob);
            postParams.put("father", father);
            postParams.put("mother", mother);
            postParams.put("user", guardianID);

            String url = "http://www.studentaggregator.org/requeststudent.php";
            serverHelper = new ServerHelper(url, postParams, new GetUserCallback() {
                @Override
                public void done() {
                    showProgress(false);
                    String reply = serverHelper.getReply();
                    if (reply == null) {
                        String errorDescription = getResources().getString(R.string.no_internet_warning);
                        Log.d(LOG_TAG, "Login - errorDescription = " + errorDescription);
                        Toast.makeText(AddStudent.this, errorDescription, Toast.LENGTH_LONG).show();
                        return;
                    }
                    parseReply(reply);
                }
            });
            serverHelper.execute((Void) null);
        }
    }//attemptFetchStudent

    private void parseReply(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        Student student = parser.parseStudent(reply);

        // Un-Successful parsing
        if (student == null) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "Login - errorDescription = " + errorDescription);
            Toast.makeText(AddStudent.this, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            /*DBHelper db = new DBHelper(AddStudent.this);
            db.addStudent(student);*/
            LocalDB localdb = new LocalDB(AddStudent.this);
            localdb.setTempStudent(student);

            //Intent i = new Intent(AddStudent.this, StudentVerification.class);
            Intent i = new Intent(AddStudent.this, Initialize.class);
            startActivity(i);
            finish();
        }
    }//parseReply

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

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {

        LocalDB localDB = new LocalDB(AddStudent.this);
        localDB.setUserLoggedIn(false);

        Intent i = new Intent(AddStudent.this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        return true;
    }//onOptionsItemSelected

    //cancel server request else go back
    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "back pressed");
        if (serverHelper != null) {
            Log.d(LOG_TAG, "request cancelled");
            showProgress(false);
            serverHelper = null;
        } else {
            finish();
        }
    }//onBackPressed

}//AddStudent

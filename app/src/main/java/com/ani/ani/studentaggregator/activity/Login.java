package com.devionlabs.ray.studentaggregator.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.GetUserCallback;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.gcm.GCMClientManager;
import com.devionlabs.ray.studentaggregator.helper.JSONParser;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.helper.ServerHelper;
import com.devionlabs.ray.studentaggregator.helper.ServerUtilities;
import com.devionlabs.ray.studentaggregator.model.User;

public class Login extends AppCompatActivity {

    private static String LOG_TAG = "mylog";
    private String gcmid;
    private ServerHelper serverHelper = null;
    private static final String PROJECT_NUMBER = "804375968805";

    // UI references
    private EditText editTextEmail;
    private TextView textViewRegister;
    private TextView textViewForgotPassword;
    private EditText editTextPassword;
    private Button buttonLogin;
    private LinearLayout linearLayoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gcmid = new LocalDB(Login.this).getGCMID();
        if (gcmid == null || gcmid.length() == 0) {
            Log.d(LOG_TAG, "gcm id = null");
            getGCMId();
        } else {
            Log.d(LOG_TAG, "Login - GCM ID = " + gcmid);
        }

        bindUIComponents();
        setOnClickListener();
    }//onCreate

    private void bindUIComponents() {
        editTextEmail = (EditText) findViewById(R.id.email);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.button_login);
        linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress);
    }//bindUIComponents

    public void getGCMId() {
        if (!ServerUtilities.hasActiveInternetConnection(Login.this)) {
            Toast.makeText(Login.this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {

            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d(LOG_TAG, "Login - GCM Registration id" + registrationId);
                LocalDB localDB = new LocalDB(Login.this);
                localDB.setGCMID(registrationId);
                gcmid = registrationId;
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                Log.d(LOG_TAG, "Login - Unable to register with GCM");
                getGCMId();
            }
        });
    }

    void setOnClickListener() {

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServerUtilities.hasActiveInternetConnection(Login.this)) {
                    attemptLogin();
                } else {
                    Toast.makeText(Login.this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
                }
            }
        });

        textViewRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, AddAccount.class);
                startActivity(i);
            }
        });

        textViewForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: send link to reset password
            }
        });

    }//setOnClickListener

    /**
     * Attempts to log in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        //Reset errors
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        // Store values at the time of the login attempt
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        //cancel will be set to true if there are form errors (invalid email, missing fields, etc.)
        //and field will be focused
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //when asynctask returns done()method is called
            showProgress(true);

            ContentValues postParams = new ContentValues();
            postParams.put("email", email);
            postParams.put("password", password);
            postParams.put("gcmid", gcmid);

            String url = "http://www.studentaggregator.org/requestlogin.php";
            serverHelper = new ServerHelper(url, postParams, new GetUserCallback() {
                @Override
                public void done() {
                    String reply = serverHelper.getReply();
                    if (reply == null) {
                        String errorDescription = getResources().getString(R.string.no_internet_warning);
                        Log.d(LOG_TAG, "Login - errorDescription = " + errorDescription);
                        Toast.makeText(Login.this, errorDescription, Toast.LENGTH_LONG).show();
                        showProgress(false);
                        return;
                    }
                    parseReply(reply);
                }
            });
            serverHelper.execute((Void) null);

        }
    }//attemptLogin

    private void parseReply(String reply) {

        String errorDescription;
        final JSONParser parser = new JSONParser();

        User user = parser.parseUser(reply);

        // Un-Successful parsing
        if (user == null) {
            errorDescription = parser.getErrorDescription();
            Log.d(LOG_TAG, "Login - errorDescription = " + errorDescription);
            Toast.makeText(Login.this, errorDescription, Toast.LENGTH_LONG).show();
        }
        //Successful parsing
        else {
            LocalDB localdb = new LocalDB(Login.this);
            localdb.setUserLoggedIn(true);
            localdb.saveUser(user);

            Log.d(LOG_TAG, "Login - Login Successful");
            Intent i = new Intent(Login.this, Initialize.class);
            startActivity(i);
            finish();
        }

        showProgress(false);
    }//parseReply

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }//isEmailValid

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }//isPasswordValid

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

    //cancel server request
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
    }


}//Login
package com.devionlabs.ray.studentaggregator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ray on 4/24/2016.
 */
public class TestJson extends AppCompatActivity {

    private static final String LOG_TAG = "mylog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        UserNetworkTask userNetworkTask = new UserNetworkTask();
        userNetworkTask.execute();
    }

    public class UserNetworkTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            /*---------------------------------end------------------------------------*/
            HttpUtility httpUtility = new HttpUtility();
            String url = "http://studentaggregator.org/markattendance.php";
            Map<String, String> postParamsMap = new HashMap<String, String>();
            postParamsMap.put("name", "Anna");
            String[] reply = null;

            try {
                httpUtility.sendPostRequest(url, postParamsMap);
                reply = httpUtility.readMultipleLinesRespone();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            Log.d(LOG_TAG, "reply from server=" + reply.toString());

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Log.d(LOG_TAG, "Success");
            } else {
                Log.d(LOG_TAG, "Failure");
            }
        }

    }
}
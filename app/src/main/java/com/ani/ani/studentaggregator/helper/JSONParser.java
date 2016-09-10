package com.devionlabs.ray.studentaggregator.helper;

import android.util.Log;

import com.devionlabs.ray.studentaggregator.model.Attendance;
import com.devionlabs.ray.studentaggregator.model.Event;
import com.devionlabs.ray.studentaggregator.model.Fee;
import com.devionlabs.ray.studentaggregator.model.Holiday;
import com.devionlabs.ray.studentaggregator.model.Student;
import com.devionlabs.ray.studentaggregator.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ray on 4/25/2016.
 */
public class JSONParser {

    private static final String LOG_TAG = "mylog";

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ERROR = "error";
    private static final String TAG_ERROR_DESCRIPTION = "description";
    private static final String TAG_DESCRIPTION = "description";
    private static String errorDescription = "";


    /*------------      Notification      -------------------*/
    public static final int NOTIFICATION_TYPE_ATTENDANCE = 1;
    public static final int NOTIFICATION_TYPE_FEE = 2;
    public static final int NOTIFICATION_TYPE_EVENT = 3;
    private static final String KEY_DESCRIPTION = "description";
    public static final String KEY_NOTIFICATION_CATEGORY = "category";


    /*------------      Attendance      -------------------*/
    private static final String TAG_ATTENDANCE_STUDENT_ID = "id";
    private static final String TAG_ATTENDANCE_STATUS = "status";
    private static final String TAG_ATTENDANCE_DESC = "description";
    private static final String TAG_ATTENDANCE_DATETIME = "datetime";

    /*------------      User      -------------------*/
    private static final String TAG_USER_ID = "id";
    private static final String TAG_USER_NAME = "name";
    private static final String TAG_USER_EMAIL = "email";
    private static final String TAG_USER_CONTACT = "contact";

    /*------------      Student      -------------------*/
    private static final String TAG_STUDENT_ID = "id";
    private static final String TAG_STUDENT_NAME = "name";
    private static final String TAG_STUDENT_DOB = "dob";
    private static final String TAG_STUDENT_GENDER = "gender";
    private static final String TAG_STUDENT_FATHER = "father";
    private static final String TAG_STUDENT_MOTHER = "mother";
    private static final String TAG_STUDENT_CLASS = "class";
    private static final String TAG_STUDENT_DIVISION = "division";
    private static final String TAG_STUDENT_HOUSE = "house";
    private static final String TAG_STUDENT_CONTACT = "contact";


    /*------------      Holiday      -------------------*/
    private static final String TAG_HOLIDAY_DATE = "date";
    private static final String TAG_HOLIDAY_DESC = "description";

    /*------------      Fee      -------------------*/
    public static final String KEY_FEE_STUDENT_ID = "studentid";
    public static final String KEY_FEE_TIME = "time";
    public static final String KEY_FEE_AMOUNT = "amount";
    public static final String KEY_FEE_TYPE = "type";
    public static final String KEY_FEE_DESCRIPTION = "description";

    /*------------      Event      -------------------*/
    public static final String KEY_EVENT_ID = "id";
    public static final String KEY_EVENT_TITLE = "title";
    public static final String KEY_EVENT_DESCRIPTION = "description";
    public static final String KEY_EVENT_DATETIME = "datetime";
    public static final String KEY_EVENT_IMAGES = "images";

    /*------------      Success      -------------------*/
    private static final String TAG_SUCCESS = "Success";


    //----------------------------     parseAttendance     -------------------------------
    public ArrayList<Attendance> parseAttendance(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Attendance
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Attendance> attendanceList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObj = jsonArray.getJSONObject(i);
                String id = singleJsonObj.getString(TAG_ATTENDANCE_STUDENT_ID);
                String status = singleJsonObj.getString(TAG_ATTENDANCE_STATUS);
                String desc = singleJsonObj.getString(TAG_ATTENDANCE_DESC);
                String datetime = singleJsonObj.getString(TAG_ATTENDANCE_DATETIME);
                Attendance attendance = new Attendance(id, status, desc, datetime);
                attendanceList.add(attendance);
            }
            return attendanceList;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }

    //------------------------------     parseUser     -----------------------------------
    public User parseUser(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to User
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            JSONObject singleJsonObject = jsonArray.getJSONObject(0);

            //extract user data from jsonObject
            String id = singleJsonObject.getString(TAG_USER_ID);
            String name = singleJsonObject.getString(TAG_USER_NAME);
            String email = singleJsonObject.getString(TAG_USER_EMAIL);
            String contact = singleJsonObject.getString(TAG_USER_CONTACT);

            return new User(id, name, email, contact);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }//parseUser


    //------------------------------     parseStudent     --------------------------------
    public Student parseStudent(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Student
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            JSONObject singleJsonObject = jsonArray.getJSONObject(0);

            String id = singleJsonObject.getString(TAG_STUDENT_ID);
            String name = singleJsonObject.getString(TAG_STUDENT_NAME);
            String dob = singleJsonObject.getString(TAG_STUDENT_DOB);
            String gender = singleJsonObject.getString(TAG_STUDENT_GENDER);
            String father = singleJsonObject.getString(TAG_STUDENT_FATHER);
            String mother = singleJsonObject.getString(TAG_STUDENT_MOTHER);
            String classOfStudent = singleJsonObject.getString(TAG_STUDENT_CLASS);
            String division = singleJsonObject.getString(TAG_STUDENT_DIVISION);
            String house = singleJsonObject.getString(TAG_STUDENT_HOUSE);
            String contact = singleJsonObject.getString(TAG_STUDENT_CONTACT);

            return new Student(id, name, dob, gender, father, mother, classOfStudent, division, house, contact);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    //------------------------------     parseHoliday     --------------------------------
    public ArrayList<Holiday> parseHoliday(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Holiday
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Holiday> holidayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                String date = singleJsonObject.getString(TAG_HOLIDAY_DATE);
                String description = singleJsonObject.getString(TAG_HOLIDAY_DESC);
                Holiday holiday = new Holiday(date, description);
                holidayList.add(holiday);
            }
            return holidayList;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    //--------------------------------     parseFee     -----------------------------------
    public ArrayList<Fee> parseFee(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Fee
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Fee> feeList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                String time = singleJsonObject.getString(KEY_FEE_TIME);
                String amount = singleJsonObject.getString(KEY_FEE_AMOUNT);
                String type = singleJsonObject.getString(KEY_FEE_TYPE);
                String description = singleJsonObject.getString(KEY_FEE_DESCRIPTION);
                String studentId = singleJsonObject.getString(KEY_FEE_STUDENT_ID);
                Fee fee = new Fee(studentId, time, amount, type, description);
                feeList.add(fee);
            }
            return feeList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    public ArrayList<Event> parseEvent(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Fee
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Event> eventList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                String id = singleJsonObject.getString(KEY_EVENT_ID);
                String title = singleJsonObject.getString(KEY_EVENT_TITLE);
                String description = singleJsonObject.getString(KEY_EVENT_DESCRIPTION);
                String datetime = singleJsonObject.getString(KEY_EVENT_DATETIME);
                String images = singleJsonObject.getString(KEY_EVENT_IMAGES);
                Event a = new Event(id, title, description, datetime, images);
                eventList.add(a);
            }
            return eventList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    public int parseNotificationType(String jsonString) {

        JSONArray jsonArray;

        try {
            JSONObject jsonObj = new JSONObject(jsonString);

            //check for successful reply
            String jsonArraytest = jsonObj.optString(TAG_RESULTS);
            if (jsonArraytest.length() == 0) {
                Log.d("mylog", TAG_RESULTS + " isnull : JSONParser");
                errorDescription = "Empty JSON : No data with " + TAG_RESULTS;

                jsonArraytest = jsonObj.optString(TAG_ERROR);
                if (jsonArraytest.length() == 0) {
                    Log.d("mylog", TAG_ERROR + " isnull : JSONParser");
                    errorDescription += " , Empty JSON : No data with " + TAG_ERROR;
                    return 0;
                }
                //when error is not empty
                else {
                    jsonArray = jsonObj.getJSONArray(TAG_ERROR);
                    JSONObject c = jsonArray.getJSONObject(0);
                    errorDescription = c.getString(TAG_ERROR_DESCRIPTION);
                }
                return 0;
            }

            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            JSONObject c = jsonArray.getJSONObject(0);
            String category = c.getString(KEY_NOTIFICATION_CATEGORY);

            if (category.equals("attendance")) {
                return NOTIFICATION_TYPE_ATTENDANCE;
            }
            //Log.d("mylog", "JSONParser final = " + arrayList.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }


    //Class functions
    public String getErrorDescription() {
        return errorDescription;
    }//getErrorDescription

    private boolean isValidData(String jsonString) {

        Log.d(LOG_TAG, "JSONParser - jsonString = " + jsonString);
        try {
            JSONObject jsonObj = new JSONObject(jsonString);

            //check for successful reply which contains result
            String jsonArrayTest = jsonObj.optString(TAG_RESULTS);

            //JSON dose'nt have data with result
            if (jsonArrayTest.length() == 0) {
                Log.d(LOG_TAG, "JSONParser - No data with result");
                errorDescription = "Empty JSON : No data with Result";

                //check for successful reply which contains error
                jsonArrayTest = jsonObj.optString(TAG_ERROR);

                //JSON dose'nt have data with error
                if (jsonArrayTest.length() == 0) {
                    Log.d(LOG_TAG, "JSONParser - no data with error");
                    errorDescription = "Empty JSON : No data with error";
                }
                //when error is not empty
                else {
                    JSONArray jsonArray = jsonObj.getJSONArray(TAG_ERROR);
                    JSONObject c = jsonArray.getJSONObject(0);
                    errorDescription = c.getString(TAG_ERROR_DESCRIPTION);
                    Log.d(LOG_TAG, "JSONParser - errorDescription = " + errorDescription);
                }
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }//isValidData

    public Boolean parseSuccess(String jsonString) {
        //if JSON has error
        if (!isValidData(jsonString)) {
            return false;
        }
        //parse JSON to User
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            JSONObject singleJsonObject = jsonArray.getJSONObject(0);

            //extract user data from jsonObject
            String success = singleJsonObject.getString(TAG_DESCRIPTION);
            if (success.contains("success"))
                return true;
            else return false;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return false;
        }
    }
}

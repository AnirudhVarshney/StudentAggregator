package com.devionlabs.ray.studentaggregator.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devionlabs.ray.studentaggregator.Fees;
import com.devionlabs.ray.studentaggregator.model.Attendance;
import com.devionlabs.ray.studentaggregator.model.Event;
import com.devionlabs.ray.studentaggregator.model.Fee;
import com.devionlabs.ray.studentaggregator.model.Holiday;
import com.devionlabs.ray.studentaggregator.model.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Ray on 1/30/2016.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String LOG_TAG = "mylog";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "database.db";
    private static Context context = null;


    private static final String TABLE_NAME_HOLIDAY = "holiday";

    private static final String KEY_DATE = "date";
    private static final String KEY_description = "description";

    private static final String TABLE_NAME_STUDENT = "student";


    private static final String KEY_STUDENTID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOB = "dob";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_FATHER = "father";
    private static final String KEY_MOTHER = "mother";
    private static final String KEY_CLASS = "class";
    private static final String KEY_DIVISION = "division";
    private static final String KEY_HOUSE = "house";
    private static final String KEY_CONTACT = "contact";

    private static final String TABLE_NAME_ATTENDANCE = "attendance";

    //private static final String KEY_STUDENTID = "id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATETIME = "datetime";

    private static final String TABLE_NAME_FEES = "fees";

    private static final String KEY_date = "date";
    //private static final String KEY_description = "description";
    public static final String KEY_paidon = "Paidon";
    public static final String KEY_descrip = "Description";
    public static final String KEY_type = "Type";
    public static final String KEY_studentid = "StudentId";
    public static final String KEY_amount = "Amount";
    public static final String KEY_tutionfee = "TutionFee";
    public static final String KEY_libraryfee = "LibraryFee";
    public static final String KEY_computerfee = " ComputerFee";
    public static final String KEY_sum = "TotalSum";

    //for fee
    private static final String TABLE_NAME_FEE = "fee";
    public static final String KEY_FEE_STUDENT_ID = "studentid";
    public static final String KEY_FEE_TIME = "time";
    public static final String KEY_FEE_AMOUNT = "amount";
    public static final String KEY_FEE_TYPE = "type";
    public static final String KEY_FEE_DESCRIPTION = "description";

    private static final String TABLE_NAME_EVENT = "event";

    public static final String KEY_EVENT_ID = "id";
    public static final String KEY_EVENT_TITLE = "title";
    public static final String KEY_EVENT_DESCRIPTION = "description";
    public static final String KEY_EVENT_DATETIME = "datetime";
    public static final String KEY_EVENT_IMAGES = "images";

    String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    public DBHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
        context = con;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*----------------------------------------Holiday------------------------------------------------*/

        Log.d("mylog", "DBHelper - creating table Holiday");
        String CREATE_TABLE_CALENDAR = "CREATE TABLE " + TABLE_NAME_HOLIDAY +
                "(" + KEY_DATE + " text, "
                + KEY_description + " text"
                + ")";

        db.execSQL(CREATE_TABLE_CALENDAR);
        Log.d("mylog", "DBHelper - Table Holiday created");

        /*----------------------------------------Student------------------------------------------------*/

        Log.d("mylog", "DBHelper - creating table Student");
        String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_NAME_STUDENT +
                "(" + KEY_STUDENTID + " text, "
                + KEY_NAME + " text, "
                + KEY_DOB + " text, "
                + KEY_GENDER + " text, "
                + KEY_FATHER + " text, "
                + KEY_MOTHER + " text, "
                + KEY_CLASS + " text, "
                + KEY_DIVISION + " text, "
                + KEY_HOUSE + " text, "
                + KEY_CONTACT + " text"
                + ")";

        db.execSQL(CREATE_TABLE_STUDENT);
        Log.d("mylog", "DBHelper - Table Student created");

        /*---------------------------------------Attendance-------------------------------------------------*/

        Log.d("mylog", "DBHelper - creating table Attendance");
        String CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_NAME_ATTENDANCE +
                "(" + KEY_STUDENTID + " text, "
                + KEY_STATUS + " text, "
                + KEY_DESCRIPTION + " text, "
                + KEY_DATETIME + " text"
                + ")";

        db.execSQL(CREATE_TABLE_ATTENDANCE);
        Log.d("mylog", "DBHelper - Table Attendance created");

        /*---------------------------------------Fees-------------------------------------------------*/

       /* Log.d("mylog", "DBHelper - creating table Fee");
        String CREATE_TABLE_FEES = "CREATE TABLE " + TABLE_NAME_FEES +
                "(" + KEY_studentid + " text, "
                + KEY_paidon + " text, "
                + KEY_amount + " text, "
                + KEY_descrip + " text, "
                + KEY_type + " text,"
                + KEY_tutionfee + " text, "
                + KEY_computerfee + " text, "
                + KEY_libraryfee + " text, "
                + KEY_sum + " text "
                + ")";

        db.execSQL(CREATE_TABLE_FEES);
        Log.d("mylog", "DBHelper - Table Fee created");*/

        /*---------------------------------------Fee-------------------------------------------------*/

        Log.d("mylog", "DBHelper - creating table Fee");
        //studentId time amount type description
        String CREATE_TABLE_FEE = "CREATE TABLE " + TABLE_NAME_FEE +
                "(" + KEY_FEE_STUDENT_ID + " text, "
                + KEY_FEE_TIME + " text, "
                + KEY_FEE_AMOUNT + " text, "
                + KEY_FEE_TYPE + " text, "
                + KEY_FEE_DESCRIPTION + " text"
                + ")";

        db.execSQL(CREATE_TABLE_FEE);
        Log.d("mylog", "DBHelper - Table Fee created");

        /*---------------------------------------Event-------------------------------------------------*/

        Log.d("mylog", "DBHelper - creating table Event");
        String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_NAME_EVENT +
                "(" + KEY_EVENT_ID + " text, "
                + KEY_EVENT_TITLE + " text, "
                + KEY_EVENT_DESCRIPTION + " text, "
                + KEY_EVENT_DATETIME + " text, "
                + KEY_EVENT_IMAGES + " text"
                + ")";

        db.execSQL(CREATE_TABLE_EVENT);
        Log.d("mylog", "DBHelper - Table event created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("mylog", "DBHelper - upgrading tables");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HOLIDAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVENT);

        onCreate(db);
        Log.d("mylog", "DBHelper - Tables upgraded");
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HOLIDAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVENT);
        onCreate(db);
    }

    public void addHoliday(String date, String descrition) {

        Log.d("mylog", "DBHelper - inserting to Table Calendar");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_description, descrition);

        db.insert(TABLE_NAME_HOLIDAY, null, values);

        Log.d("mylog", "DBHelper - Holiday added : values=" + values.toString());
        db.close();
    }

    public ArrayList<Holiday> getAllHolidays() {
        ArrayList<Holiday> holidayList = new ArrayList<Holiday>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_HOLIDAY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Holiday holiday = new Holiday();

                //adding data to object holiday
                holiday.setDate(cursor.getString(0));
                holiday.setDescription(cursor.getString(1));

                //adding object holiday to list
                Log.d("mylog", "DBHelper - DBhelper read holiday = " + holiday.toString());
                holidayList.add(holiday);
            } while (cursor.moveToNext());
            return holidayList;
        }
        db.close();
        return null;
    }

    public ArrayList<Holiday> getHolidaysForMonth(Date date) {
        ArrayList<Holiday> holidayList = new ArrayList<Holiday>();

        Date d = new Date(date.toString());
        String t = "%" + months[d.getMonth()] + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_HOLIDAY + " where " + KEY_DATE + " like '" + t + "'";

        Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Holiday holiday = new Holiday();

                //adding data to object holiday
                holiday.setDate(cursor.getString(0));
                holiday.setDescription(cursor.getString(1));

                //adding object holiday to list
                Log.d("mylog", "DBHelper - DBhelper read holiday = " + holiday.toString());
                holidayList.add(holiday);
            } while (cursor.moveToNext());
            return holidayList;
        }
        db.close();
        return null;
    }

    public ArrayList<Holiday> getHolidaysForMonth(GregorianCalendar date) {
        ArrayList<Holiday> holidayList = new ArrayList<Holiday>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String t = dateFormat.format(date.getTime()) + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_HOLIDAY + " where " + KEY_DATE + " like '" + t + "'";

        Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Holiday holiday = new Holiday();

                //adding data to object holiday
                holiday.setDate(cursor.getString(0));
                holiday.setDescription(cursor.getString(1));

                //adding object holiday to list
                Log.d("mylog", "DBHelper - DBhelper read holiday = " + holiday.toString());
                holidayList.add(holiday);
            } while (cursor.moveToNext());
            return holidayList;
        }
        db.close();
        return null;
    }

    public void addStudent(Student student) {

        Log.d("mylog", "DBHelper - inserting to Table Student");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STUDENTID, student.getId());
        values.put(KEY_NAME, student.getName());
        values.put(KEY_DOB, student.getDob());
        values.put(KEY_GENDER, student.getGender());
        values.put(KEY_FATHER, student.getFather());
        values.put(KEY_MOTHER, student.getMother());
        values.put(KEY_CLASS, student.getClassOfStudent());
        values.put(KEY_DIVISION, student.getDivision());
        values.put(KEY_HOUSE, student.getHouse());
        values.put(KEY_CONTACT, student.getContact());

        db.insert(TABLE_NAME_STUDENT, null, values);

        Log.d("mylog", "DBHelper - Student added : values=" + values.toString());
        db.close();
    }

    public String getStudentName(String id) {

        String selectQuery = "SELECT  " + KEY_NAME + " FROM " + TABLE_NAME_STUDENT + " where " + KEY_STUDENTID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                db.close();
                return cursor.getString(0);
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

   /* public String getAllStudents() {

        String selectQuery = "SELECT  " + KEY_NAME + " FROM " + TABLE_NAME_STUDENT + " where " + KEY_STUDENTID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                db.close();
                count++;
            } while (cursor.moveToNext());
        }
        db.close();

        return null;
    }*/

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentList = new ArrayList<Student>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_STUDENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();

                //adding data to object student
                student.setId(cursor.getString(0));
                student.setName(cursor.getString(1));
                student.setDob(cursor.getString(2));
                student.setGender(cursor.getString(3));
                student.setFather(cursor.getString(4));
                student.setMother(cursor.getString(5));
                student.setClassOfStudent(cursor.getString(6));
                student.setDivision(cursor.getString(7));
                student.setHouse(cursor.getString(8));
                student.setContact(cursor.getString(9));

                //adding object student to list
                Log.d("mylog", "DBHelper - DBhelper read student = " + student.toString());
                studentList.add(student);
            } while (cursor.moveToNext());
            return studentList;
        }
        db.close();
        return null;
    }

    public Student getStudent(String id) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_STUDENT + " where " + KEY_STUDENTID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();

                //adding data to object student
                student.setId(cursor.getString(0));
                student.setName(cursor.getString(1));
                student.setDob(cursor.getString(2));
                student.setGender(cursor.getString(3));
                student.setFather(cursor.getString(4));
                student.setMother(cursor.getString(5));
                student.setClassOfStudent(cursor.getString(6));
                student.setDivision(cursor.getString(7));
                student.setHouse(cursor.getString(8));

                db.close();
                return student;
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public void addAttendance(Attendance attendance) {

        Log.d("mylog", "DBHelper - inserting to Table Attendance");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STUDENTID, attendance.getId());
        values.put(KEY_STATUS, attendance.getStatus());
        values.put(KEY_DESCRIPTION, attendance.getDesc());
        values.put(KEY_DATETIME, attendance.getDatetime());

        db.insert(TABLE_NAME_ATTENDANCE, null, values);

        Log.d("mylog", "DBHelper - Attendance added : values = " + values.toString());
        db.close();
    }


    public void addAttendance(ArrayList<Attendance> attendanceList) {
        int i;
        SQLiteDatabase db = this.getWritableDatabase();

        for (i = 0; i < attendanceList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_STUDENTID, attendanceList.get(i).getId());
            values.put(KEY_STATUS, attendanceList.get(i).getStatus());
            values.put(KEY_DESCRIPTION, attendanceList.get(i).getDesc());
            values.put(KEY_DATETIME, attendanceList.get(i).getDatetime());

            db.insert(TABLE_NAME_ATTENDANCE, null, values);
            Log.d("mylog", "DBHelper - Attendance added : values = " + values.toString());
        }
        //Toast.makeText(context, "addAttendance : Attendance added " + attendanceList.size(), Toast.LENGTH_LONG).show();
        db.close();
    }

    public ArrayList<Attendance> getAllAttendance() {
        ArrayList<Attendance> attendanceList = new ArrayList<Attendance>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_ATTENDANCE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();

                //adding data to object holiday
                attendance.setId(cursor.getString(0));
                attendance.setStatus(cursor.getString(1));
                attendance.setDesc(cursor.getString(2));
                attendance.setDatetime(cursor.getString(3));

                //adding object Attendance to list
                //Log.d("mylog", "DBHelper - DBhelper read Attendance = " + attendance.toString());
                attendanceList.add(attendance);

            } while (cursor.moveToNext());
            return attendanceList;
        }

        db.close();
        return null;
    }

    public ArrayList<Attendance> getAttendanceForMonth(Date date) {

        ArrayList<Attendance> attendanceList = new ArrayList<Attendance>();
        Date d = new Date(date.toString());
        String t = "%" + months[d.getMonth()] + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_ATTENDANCE + " where " + KEY_DATETIME + " like '" + t + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();

                //adding data to object holiday
                attendance.setId(cursor.getString(0));
                attendance.setStatus(cursor.getString(1));
                attendance.setDesc(cursor.getString(2));
                attendance.setDatetime(cursor.getString(3));

                //adding object Attendance to list
                Log.d("mylog", "DBHelper - DBhelper read Attendance = " + attendance.toString());
                attendanceList.add(attendance);

            } while (cursor.moveToNext());
            return attendanceList;
        }
        db.close();
        return null;
    }


    public ArrayList<Attendance> getAttendanceForMonth(String id, GregorianCalendar date) {

        ArrayList<Attendance> attendanceList = new ArrayList<Attendance>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String t = dateFormat.format(date.getTime()) + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_ATTENDANCE + " where " + KEY_STUDENTID + "='" + id + "' and " + KEY_DATETIME + " like '" + t + "'";
        //Log.d(LOG_TAG, "querry=" + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();

                attendance.setId(cursor.getString(0));
                attendance.setStatus(cursor.getString(1));
                attendance.setDesc(cursor.getString(2));
                attendance.setDatetime(cursor.getString(3));

                attendanceList.add(attendance);

            } while (cursor.moveToNext());
            return attendanceList;
        }
        db.close();
        return null;
    }

    public void addHoliday(ArrayList<Holiday> holidayList) {
        int i;
        SQLiteDatabase db = this.getWritableDatabase();
        for (i = 0; i < holidayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, holidayList.get(i).getDate());
            values.put(KEY_DESCRIPTION, holidayList.get(i).getDescription());

            db.insert(TABLE_NAME_HOLIDAY, null, values);
            Log.d("mylog", "DBHelper - Holiday added : values = " + values.toString());
        }
        db.close();
    }

    /*public void clearAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("mylog", "DBHelper - Truncating table Attendance");
        db.execSQL("delete from " + TABLE_NAME_ATTENDANCE);
        Log.d("mylog", "DBHelper - Table Attendance truncated");
        db.close();
    }*/

    public void clearHoliday() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("mylog", "DBHelper - Truncating table Holiday");
        db.execSQL("delete from " + TABLE_NAME_HOLIDAY);
        Log.d("mylog", "DBHelper - Table Holiday truncated");
        db.close();
    }

    public void clearFee() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME_FEES);
    }


    public void addFees(ArrayList<Fees> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_studentid, arrayList.get(i).getStudentId());
            values.put(KEY_paidon, arrayList.get(i).getPaidon());
            values.put(KEY_amount, arrayList.get(i).getAmount());
            values.put(KEY_descrip, arrayList.get(i).getDescription());
            values.put(KEY_type, arrayList.get(i).getType());
            values.put(KEY_tutionfee, arrayList.get(i).getTutionFee());
            values.put(KEY_computerfee, arrayList.get(i).getComputerFee());
            values.put(KEY_libraryfee, arrayList.get(i).getLibraryFee());
            values.put(KEY_sum, arrayList.get(i).getTutionFee() + arrayList.get(i).getComputerFee() + arrayList.get(i).getLibraryFee());
            db.insert(TABLE_NAME_FEES, null, values);
            db.close();
        }
    }


    public ArrayList<Fees> getAllFees() {
        ArrayList<Fees> FeesList = new ArrayList<Fees>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_FEES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Fees fees = new Fees();

                //adding data to object holiday
                fees.setStudentId(cursor.getInt(0));
                fees.setPaidon(cursor.getString(1));
                fees.setAmount(cursor.getInt(2));
                fees.setDescription(cursor.getString(3));
                fees.setType(cursor.getString(4));
                fees.setTutionFee(cursor.getInt(5));
                fees.setComputerFee(cursor.getInt(6));
                fees.setLibraryFee(cursor.getInt(7));
                fees.setTotalSum(cursor.getInt(8));
                //adding object holiday to list
                Log.d("mylog", "DBHelper - DBhelper read fees = " + fees.toString());
                FeesList.add(fees);
            } while (cursor.moveToNext());
            return FeesList;
        }
        return null;
    }

    public void addFee(ArrayList<Fee> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_FEE_AMOUNT, arrayList.get(i).getAmount());
            values.put(KEY_FEE_DESCRIPTION, arrayList.get(i).getDescription());
            values.put(KEY_FEE_STUDENT_ID, arrayList.get(i).getStudentId());
            values.put(KEY_FEE_TIME, arrayList.get(i).getTime());
            values.put(KEY_FEE_TYPE, arrayList.get(i).getType());
            db.insert(TABLE_NAME_FEE, null, values);
            db.close();
            Log.d("mylog", "DBHelper - fee added" + values.toString());
        }
    }


    public ArrayList<Fee> getAllFee(String sid) {
        //studentId time amount type description

        ArrayList<Fee> FeeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_FEE + " where " + KEY_FEE_STUDENT_ID + " = '" + sid + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Fee fee = new Fee();
                //adding data to object holiday
                fee.setStudentId(cursor.getString(0));
                fee.setTime(cursor.getString(1));
                fee.setAmount(cursor.getString(2));
                fee.setType(cursor.getString(3));
                fee.setDescription(cursor.getString(4));
                //Log.d("mylog", "DBHelper - DBhelper read fee = " + fee.toString());
                FeeList.add(fee);
            } while (cursor.moveToNext());
            return FeeList;
        }
        return null;
    }

    /*public void addEvent(ArrayList<Event> arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_EVENT_ID, arrayList.get(i).getId());
            values.put(KEY_EVENT_TITLE, arrayList.get(i).getTitle());
            values.put(KEY_EVENT_DESCRIPTION, arrayList.get(i).getDescription());
            values.put(KEY_EVENT_DATETIME, arrayList.get(i).getDatetime());
            values.put(KEY_EVENT_IMAGES, arrayList.get(i).getImages());

            db.insert(TABLE_NAME_EVENT, null, values);
            db.close();
        }
    }*/

    public ArrayList<Event> getAllEvents() {

        ArrayList<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EVENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();

                event.setId(cursor.getString(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDatetime(cursor.getString(3));
                event.setImages(cursor.getString(4));

                Log.d("mylog", "DBHelper - DBhelper read Event = " + event.toString());
                eventList.add(event);

            } while (cursor.moveToNext());

            return eventList;
        }
        return null;
    }


    public void clearEvent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME_EVENT);
    }

    public ArrayList<Event> getAllEventDetails() {
        ArrayList<Event> EventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EVENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();

                //adding data to object holiday
                event.setId(cursor.getString(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDatetime(cursor.getString(3));
                event.setImages(cursor.getString(4));
                EventList.add(event);

            } while (cursor.moveToNext());

            Log.d("anirudh", "eventlist" + EventList.size() + EventList.get(0).getImages());
            return EventList;

        }
        return null;
    }

    public void addEvent(ArrayList<Event> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_EVENT_ID, arrayList.get(i).getId());
            values.put(KEY_EVENT_TITLE, arrayList.get(i).getTitle());
            values.put(KEY_EVENT_DESCRIPTION, arrayList.get(i).getDescription());
            values.put(KEY_DATETIME, arrayList.get(i).getDatetime());
            values.put(KEY_EVENT_IMAGES, String.valueOf(arrayList.get(i).getImages()));
            db.insert(TABLE_NAME_EVENT, null, values);
            Log.d("anirudh", "insert" + values.get(KEY_EVENT_TITLE) + values.size());
            db.close();
        }

    }


    public boolean isHoliday(GregorianCalendar date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String t = dateFormat.format(date.getTime()) + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_HOLIDAY + " where " + KEY_DATE + " like '" + t + "'";
        //Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        db.close();
        //Log.d("mylog", "DBHelper - count = " + count);
        if (count == 0) {
            return false;
        }
        return false;
    }

    public boolean isPresent(GregorianCalendar date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String t = dateFormat.format(date.getTime()) + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_ATTENDANCE + " where " + KEY_DATETIME + " like '" + t + "' and status='1'";
        //Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        db.close();
        if (count == 0) {
            return false;
        }
        return true;
    }
}//DBHelper

package com.devionlabs.ray.studentaggregator.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devionlabs.ray.studentaggregator.Utils;
import com.devionlabs.ray.studentaggregator.model.Fee;
import com.devionlabs.ray.studentaggregator.model.Student;
import com.devionlabs.ray.studentaggregator.model.User;

// Created by Ray on 3/28/2016.

public class LocalDB {

    //private String LOG_TAG = "mylog";
    public static final String SP_NAME = "UserDetails";
    SharedPreferences sp;

    public LocalDB(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("id", user.getId());
        spEditor.putString("name", user.getName());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("contact", user.getContact());
        spEditor.apply();
    }

    public User getUser() {
        String id = sp.getString("id", "");
        String name = sp.getString("name", "");
        String email = sp.getString("email", "");
        String contact = sp.getString("contact", "");
        return new User(id, name, email, contact);
    }

    public boolean isUserLoggedIn() {
        return sp.getBoolean("loggedin", false);
    }

    public void setUserLoggedIn(boolean b) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean("loggedin", b);
        spEditor.apply();
    }

    public void setStudent(Student student) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("student-id", student.getId());
        spEditor.putString("student-name", student.getName());
        spEditor.putString("student-dob", student.getDob());
        spEditor.putString("student-gender", student.getGender());
        spEditor.putString("student-father", student.getFather());
        spEditor.putString("student-mother", student.getMother());
        spEditor.putString("student-class", student.getClassOfStudent());
        spEditor.putString("student-division", student.getDivision());
        spEditor.putString("student-house", student.getHouse());
        spEditor.putString("student-contact", student.getContact());
        spEditor.apply();
    }

    public Student getStudent() {
        String id = sp.getString("student-id", "");
        String name = sp.getString("student-name", "");
        String dob = sp.getString("student-dob", "");
        String gender = sp.getString("student-gender", "");
        String father = sp.getString("student-father", "");
        String mother = sp.getString("student-mother", "");
        String classOfStudent = sp.getString("student-class", "");
        String division = sp.getString("student-division", "");
        String house = sp.getString("student-house", "");
        String contact = sp.getString("student-contact", "");

        return new Student(id, name, dob, gender, father, mother, classOfStudent, division, house, contact);
    }

    public void setTempStudent(Student student) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("temp-student-id", student.getId());
        spEditor.putString("temp-student-name", student.getName());
        spEditor.putString("temp-student-dob", student.getDob());
        spEditor.putString("temp-student-gender", student.getGender());
        spEditor.putString("temp-student-father", student.getFather());
        spEditor.putString("temp-student-mother", student.getMother());
        spEditor.putString("temp-student-class", student.getClassOfStudent());
        spEditor.putString("temp-student-division", student.getDivision());
        spEditor.putString("temp-student-house", student.getHouse());
        spEditor.putString("temp-student-contact", student.getContact());
        spEditor.apply();
    }

    public Student getTempStudent() {
        String id = sp.getString("temp-student-id", "");
        String name = sp.getString("temp-student-name", "");
        String dob = sp.getString("temp-student-dob", "");
        String gender = sp.getString("temp-student-gender", "");
        String father = sp.getString("temp-student-father", "");
        String mother = sp.getString("temp-student-mother", "");
        String classOfStudent = sp.getString("temp-student-class", "");
        String division = sp.getString("temp-student-division", "");
        String house = sp.getString("temp-student-house", "");
        String contact = sp.getString("temp-student-contact", "");

        return new Student(id, name, dob, gender, father, mother, classOfStudent, division, house, contact);
    }

    public boolean isStudentAdded() {

        String id = sp.getString("student-id", null);
        return id != null;
    }

    public void setGCMID(String id) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("gcmid", id);
        spEditor.apply();
    }

    public String getGCMID() {
        return sp.getString("gcmid", null);
    }

    public void clear() {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.clear();
        spEditor.apply();
    }

    public void saveNextPayment(Fee fee) {
        SharedPreferences.Editor spEditor = sp.edit();

        String id = fee.getStudentId();
        spEditor.putString("time" + id, fee.getTime());
        spEditor.putString("amount" + id, fee.getAmount());
        spEditor.putString("description" + id, fee.getDescription());

        spEditor.apply();
    }

    public Fee getNextPayment(String studentID) {
        String date = sp.getString("time" + studentID, "");
        String amount = sp.getString("amount" + studentID, "");
        String type = "";
        String description = sp.getString("description" + studentID, "");
        return new Fee(studentID, date, amount, type, description);
    }

    public boolean isAlarmSet() {
        return sp.getBoolean("alarm", false);
    }

    public void setAlarm(boolean b) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean("alarm", b);
        spEditor.apply();
    }

}

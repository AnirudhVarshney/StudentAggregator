package com.devionlabs.ray.studentaggregator.model;

/**
 * Created by Ray on 4/25/2016.
 */
public class Attendance {
    private String id;
    private String status;
    private String desc;
    private String datetime;

    public Attendance() {
        id = "";
        status = "";
        desc = "";
        datetime = "";
    }

    public Attendance(String id, String status, String desc, String datetime) {
        this.id = id;
        this.status = status;
        this.desc = desc;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "id = " + id + " : status = " + status + " : description = " + desc + " : datetime = " + datetime;
    }
}

package com.devionlabs.ray.studentaggregator.model;

/**
 * Created by ABHINAV on 20-05-2016.
 */
public class Event {
    /*
    id
    title
    description
    datetime
    images
    */
    private String id;
    private String title;
    private String description;
    private String datetime;
    private String images;

    public Event() {
        id = "";
        title = "";
        description = "";
        datetime = "";
        images= "";
    }

    public Event(String id, String title, String description, String datetime, String images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "id = " + id + " : title = " + title + " : description = " + description + " : datetime = " + datetime + " : images = " + images;
    }
}

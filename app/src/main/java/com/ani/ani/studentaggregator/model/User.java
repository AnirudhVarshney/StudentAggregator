package com.devionlabs.ray.studentaggregator.model;

/**
 * Created by Ray on 3/28/2016.
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String contact;

    public User() {
        this.name = "";
        this.email = "";
        this.contact = "";
        this.id = "";
    }

    public User(String id, String name, String email, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "id = " + id + " : name = " + name + " : email = " + email + " : contact = " + contact;
    }
}

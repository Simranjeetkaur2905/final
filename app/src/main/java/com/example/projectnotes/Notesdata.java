package com.example.projectnotes;

import java.io.Serializable;
import java.util.ArrayList;

public class Notesdata implements Serializable {

    int id;
    double lat,lng;
    String category , notesTitle, description, date;

    public Notesdata(int id, String category, String notesTitle, String description, String date, double lat,double lng) {
        this.id = id;
        this.category = category;
        this.notesTitle = notesTitle;
        this.description = description;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public static ArrayList<Notesdata> notesdata = new ArrayList<>();

}

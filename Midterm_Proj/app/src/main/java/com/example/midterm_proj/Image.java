package com.example.midterm_proj;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Image {
    private Uri uri;
    private String name;
    private int size;
    private Date dateAdded;
    private int id;

    public Image(Uri uri, String name, int size, Date dateAdded, int id) {
        this.uri = uri;
        this.name = name;
        this.size = size;
        this.dateAdded = dateAdded;
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public int getId() {
        return id;
    }
}

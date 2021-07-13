package com.example.midterm_proj.ui.main;

import java.util.Date;
import java.util.List;

public class PhotoDate {
    Date date;
    List<Photo> photos;

    public PhotoDate(Date date, List<Photo> photos) {
        this.date = date;
        this.photos = photos;
    }

    public Date getDate() {
        return date;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}

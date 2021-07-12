package com.example.midterm_proj.ui.main;

public class PhotoModel {
    String url;
    String name;

    public PhotoModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}

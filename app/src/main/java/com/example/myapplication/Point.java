package com.example.myapplication;

public class Point {


    // A point is simply a set of coordinates with a unique name
    private String id;
    private float longitude;
    private float latitude;

    public Point(String id, float longitude, float latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    // Create a copy of a point (preserves private variables)
    public Point(Point point) {
        this.id = point.getId();
        this.longitude = point.getLongitude();
        this.latitude = point.getLatitude();
    }

    public float getLongitude() {
        return Float.valueOf(longitude);
    }

    public float getLatitude() {
        return Float.valueOf(latitude);
    }


    public String getId() {
        return id;
    }





}

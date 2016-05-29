package com.example.etty.locationsapp;


public class PlaceModel {

    private String name;
    private String address;
    private String location;
    private float distance;
    private String imageURL;

    public PlaceModel(String name, String address, String location, float distance, String imageURL) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.distance = distance;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

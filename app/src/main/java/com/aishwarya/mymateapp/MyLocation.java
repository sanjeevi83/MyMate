package com.aishwarya.mymateapp;

public class MyLocation {
    private int locationId;
    private String address;
    private String locTitle;

    public MyLocation(int locationId, String address, String title) {
        this.locationId = locationId;
        this.address = address;
        this.locTitle = title;
    }

    public int getLocationId() {
        return locationId;
    }
    public String getAddress() {
        return address;
    }
    public String getTitle() {
        return locTitle;
    }
}

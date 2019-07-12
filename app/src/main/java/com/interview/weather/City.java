package com.interview.weather;

public class City {

    public final static int VIEW_TYPE_CITY = 1;
    public final static int VIEW_TYPE_COUNTRY = 2;

    public final String name;
    public final String countryName;
    public final int viewType;
    public final double latitude;
    public final double longitude;

    public City(final String name, final String countryName, final double latitude, final double longitude, int viewType) {
        this.name = name;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.viewType = viewType;
    }
}

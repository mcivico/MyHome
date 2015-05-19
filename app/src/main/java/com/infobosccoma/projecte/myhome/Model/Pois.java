package com.infobosccoma.projecte.myhome.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Marc on 19/05/2015.
 */
public class Pois {
    private int _id;
    private String name, city;
    private double latitude, longitude;
    private LatLng posicio;

    public Pois(int _id, String name,double latitude, double longitude, String city ){
        this._id = _id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }
    public Pois( String name,double latitude, double longitude, String city ){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getPosicio() {
        return posicio;
    }

    public void setPosicio(LatLng posicio) {
        this.posicio = posicio;
    }
}
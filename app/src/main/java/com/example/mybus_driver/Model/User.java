package com.example.mybus_driver.Model;

public
class User {
    double currentLat , currentLng ;

    public User(double currentLat, double currentLng) {
        this.currentLat = currentLat;
        this.currentLng = currentLng;
    }

    public User() {
    }





    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(double currentLng) {
        this.currentLng = currentLng;
    }
}

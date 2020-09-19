package com.example.mybus_driver.Model;

public
class CustomerLocation {
    double currentLatCustomer  ,currentLngCustomer ;

    public CustomerLocation(double currentLatCustomer, double currentLngCustomer) {
        this.currentLatCustomer = currentLatCustomer;
        this.currentLngCustomer = currentLngCustomer;
    }

    public CustomerLocation() {
    }

    public double getCurrentLatCustomer() {
        return currentLatCustomer;
    }

    public void setCurrentLatCustomer(double currentLatCustomer) {
        this.currentLatCustomer = currentLatCustomer;
    }

    public double getCurrentLngCustomer() {
        return currentLngCustomer;
    }

    public void setCurrentLngCustomer(double currentLngCustomer) {
        this.currentLngCustomer = currentLngCustomer;
    }
}

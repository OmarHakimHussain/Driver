package com.example.mybus_driver;

public class Customers {
   // private String Name,Email,ID,Phone_Number,Receipt_Number,Password,Gender,Line_Name;
    private String Name,Phone,Line_Name,Image_URL,ID,Reference_ID,Email;

    public Customers(){

    }

    public Customers(String name, String phone, String line_Name, String image_URL, String ID, String reference_ID, String email) {
        Name = name;
        Phone = phone;
        Line_Name = line_Name;
        Image_URL = image_URL;
        this.ID = ID;
        Reference_ID = reference_ID;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLine_Name() {
        return Line_Name;
    }

    public void setLine_Name(String line_Name) {
        Line_Name = line_Name;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getReference_ID() {
        return Reference_ID;
    }

    public void setReference_ID(String reference_ID) {
        Reference_ID = reference_ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    /* public Customers(String name, String email, String ID, String phone_Number, String receipt_Number, String password, String gender) {
        Name = name;
        Email = email;
        this.ID = ID;
        Phone_Number = phone_Number;
        Receipt_Number = receipt_Number;
        Password = password;
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getReceipt_Number() {
        return Receipt_Number;
    }

    public void setReceipt_Number(String receipt_Number) {
        Receipt_Number = receipt_Number;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }*/
}

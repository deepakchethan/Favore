package com.favoreme.favore.Models;

/**
 * Object for the Post
 */

public class User {
    String fName;
    String lName;
    String gender;
    String phone;
    String about;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User(String fName, String lName, String phone, String gender, String about) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.gender = gender;
        this.about = about;
    }

    public User(){}

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

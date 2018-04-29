package com.favoreme.favore.Models;

import java.util.ArrayList;

/**
 * Object for the User
 */

public class User {
    int uid;
    String fName;
    String lName;
    String uName;
    String dName;
    String gender;
    String phone;
    String about;
    int age;

    ArrayList<Integer> posts,friends;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return about;
    }

    public void setBio(String about) {
        this.about = about;
    }

    public User(int uid,String fName, String lName,String uName,String dName, String gender, String phone, String about,int age) {
        this.uid = uid;
        this.fName = fName;
        this.lName = lName;
        this.uName = uName;
        this.dName = dName;
        this.phone = phone;
        this.gender = gender;
        this.about = about;
        this.age = age;
        posts = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public ArrayList<Integer> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Integer> posts) {
        this.posts = posts;
    }

    public ArrayList<Integer> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Integer> friends) {
        this.friends = friends;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

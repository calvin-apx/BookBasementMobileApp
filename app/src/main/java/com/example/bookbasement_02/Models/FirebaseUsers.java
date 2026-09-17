package com.example.bookbasement_02.Models;

public class FirebaseUsers {
    String id;
    String imageURL;
    String username;
    String email;
    String gender;
    String birthday;
    String phone;
    String status;
    String type;

    public String getType ( ) {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getStatus ( ) {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getEmail ( ) {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getGender ( ) {
        return gender;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

    public String getBirthday ( ) {
        return birthday;
    }

    public void setBirthday (String birthday) {
        this.birthday = birthday;
    }

    public String getPhone ( ) {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public String getId ( ) {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getImageURL ( ) {
        return imageURL;
    }

    public void setImageURL (String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername ( ) {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
}

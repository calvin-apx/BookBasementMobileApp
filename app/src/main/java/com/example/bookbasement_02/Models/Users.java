package com.example.bookbasement_02.Models;

public class Users {

    private String user_id;
    private String email;
    private String type;
    private String name;
    private String status;
    private String gender;
    private String birth_date;
    private String genres;
    private String password;
    private String phone;
    private int points;
    private String image_url;

    @Override
    public String toString ( ) {
        return "Users{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", genres='" + genres + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", points=" + points +
                ", image_url='" + image_url + '\'' +
                '}';
    }

    public Users ( ) {
    }

    public String getImage_url ( ) {
        return image_url;
    }

    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }

    public String getPhone ( ) {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public String getPassword ( ) {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getUser_id ( ) {
        return user_id;
    }

    public void setUser_id (String user_id) {
        this.user_id = user_id;
    }

    public String getEmail ( ) {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

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

    public String getBirth_date ( ) {
        return birth_date;
    }

    public void setBirth_date (String birth_date) {
        this.birth_date = birth_date;
    }

    public String getGenres ( ) {
        return genres;
    }

    public void setGenres (String genres) {
        this.genres = genres;
    }

    public int getPoints ( ) {
        return points;
    }

    public void setPoints (int points) {
        this.points = points;
    }

    public String getName ( ) {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getGender ( ) {
        return gender;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

}

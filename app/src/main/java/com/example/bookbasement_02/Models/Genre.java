package com.example.bookbasement_02.Models;

public class Genre {
    String image_url;
    String name;
    String genre_id;


    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
    }


    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre(String genre_id, String name, String image_url) {
        this.genre_id = genre_id;
        this.image_url = image_url;
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public String getGenre_id() {
        return genre_id;
    }


}

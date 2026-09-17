package com.example.bookbasement_02.Models;

public class SearchFilter {
    String title;
    String genre;
    String author;
    String publisher;
    int price;
    public SearchFilter(){}

    public SearchFilter (String title, String genre, String author, String publisher, int price) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
    }

    public String getTitle ( ) {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getGenre ( ) {
        return genre;
    }

    public void setGenre (String genre) {
        this.genre = genre;
    }

    public String getAuthor ( ) {
        return author;
    }

    public void setAuthor (String author) {
        this.author = author;
    }

    public String getPublisher ( ) {
        return publisher;
    }

    public void setPublisher (String publisher) {
        this.publisher = publisher;
    }

    public int getPrice ( ) {
        return price;
    }

    public void setPrice (int price) {
        this.price = price;
    }


}




package com.example.bookbasement_02.Models;

public class Book {
    private String title;
    private String sub_title;
    private String genre;
    private String description;
    private String image;
    private String publisher;
    private String authors;
    private double total;
    private double price;
    private int qty;

    public Book ( ) {
    }

    public Book (String title, String sub_title, String genre, String description, String image, String publisher, String authors, double total, double price, int qty) {
        this.title = title;
        this.sub_title = sub_title;
        this.genre = genre;
        this.description = description;
        this.image = image;
        this.publisher = publisher;
        this.authors = authors;
        this.total = total;
        this.price = price;
        this.qty = qty;
    }

    @Override
    public String toString ( ) {
        return "Book{" +
                "title='" + title + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image + '\'' +
                ", publisher='" + publisher + '\'' +
                ", authors='" + authors + '\'' +
                ", total=" + total +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }

    public String getTitle ( ) {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getSub_title ( ) {
        return sub_title;
    }

    public void setSub_title (String sub_title) {
        this.sub_title = sub_title;
    }

    public String getGenre ( ) {
        return genre;
    }

    public void setGenre (String genre) {
        this.genre = genre;
    }

    public String getDescription ( ) {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getImage ( ) {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public String getPublisher ( ) {
        return publisher;
    }

    public void setPublisher (String publisher) {
        this.publisher = publisher;
    }

    public String getAuthors ( ) {
        return authors;
    }

    public void setAuthors (String authors) {
        this.authors = authors;
    }

    public double getTotal ( ) {
        return total;
    }

    public void setTotal (double total) {
        this.total = total;
    }

    public double getPrice ( ) {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public int getQty ( ) {
        return qty;
    }

    public void setQty (int qty) {
        this.qty = qty;
    }
}

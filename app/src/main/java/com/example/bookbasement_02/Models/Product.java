package com.example.bookbasement_02.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Product  implements Serializable {
    private int book_id;
    private int genre_id;
    private String title;
    private String sub_title;
    private String genre;
    private String description;
    private String image_url;
    private String publisher;
    private String authors;
    private String status;
    private double total;
    private double price;
    private float rating;
    private int page_count;
    private int qty;

    @Override
    public String toString ( ) {
        return "Product{" +
                "book_id=" + book_id +
                ", genre_id=" + genre_id +
                ", title='" + title + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", publisher='" + publisher + '\'' +
                ", authors='" + authors + '\'' +
                ", status='" + status + '\'' +
                ", total=" + total +
                ", price=" + price +
                ", rating=" + rating +
                ", page_count=" + page_count +
                ", qty=" + qty +
                '}';
    }

    public Product ( ) {
        this.book_id = 0;
        this.genre_id = 0;
        this.title = "";
        this.sub_title = "";
        this.genre = "";
        this.description = "";
        this.price = 0;
        this.qty = 0;
        this.total = 0;
        this.publisher = "";
        this.authors = "";
        this.page_count = 0;
        this.image_url = "";
        this.rating = 0;
        this.status = "";
    }

    public Product (
            int book_id,
            int genre_id,
            String title,
            String sub_title,
            String genre,
            String description,
            double price,
            int qty,
            double total,
            String publisher,
            String authors,
            int page_count,
            String image_url,
            float rating,
            String status) {
        this.book_id = book_id;
        this.genre_id = genre_id;
        this.title = title;
        this.sub_title = sub_title;
        this.genre = genre;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.total = total;
        this.publisher = publisher;
        this.authors = authors;
        this.page_count = page_count;
        this.image_url = image_url;
        this.rating = rating;
        this.status = status;
    }

    public int getBook_id ( ) {
        return book_id;
    }

    public void setBook_id (int book_id) {
        this.book_id = book_id;
    }

    public String getDescription ( ) {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public int getQty ( ) {
        return qty;
    }

    public void setQty (int qty) {
        this.qty = qty;
    }

    public String getImage_url ( ) {
        return image_url;
    }

    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }

    public double getPrice ( ) {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
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

    public String getAuthors ( ) {
        return authors;
    }

    public void setAuthors (String authors) {
        this.authors = authors;
    }

    public String getStatus ( ) {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public double getTotal ( ) {
        return total;
    }

    public void setTotal (double total) {
        this.total = total;
    }

    public String getPublisher ( ) {
        return publisher;
    }

    public void setPublisher (String publisher) {
        this.publisher = publisher;
    }

    public int getPage_count ( ) {
        return page_count;
    }

    public void setPage_count (int page_count) {
        this.page_count = page_count;
    }

    public float getRating ( ) {
        return rating;
    }

    public void setRating (float rating) {
        this.rating = rating;
    }

    public int getGenre_id ( ) {
        return genre_id;
    }

    public void setGenre_id (int genre_id) {
        this.genre_id = genre_id;
    }


}

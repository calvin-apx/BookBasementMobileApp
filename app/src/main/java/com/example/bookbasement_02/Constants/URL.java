package com.example.bookbasement_02.Constants;

import com.google.firebase.auth.FirebaseAuth;

public class URL {
    private static String IP = "http://192.168.254.101/";
    public static final String URL_ADDRESS  = IP +"BookBasementApp/public/";
    public static final String IMAGE_URL_ADDRESS  = IP +"BookBasementApp/public/storage/images/books/";
    public static final String IMAGE_GENRE_URL_ADDRESS  = IP +"BookBasementApp/public/storage/images/genres/";
    private static  final String BASE_URL_BOOK="https://www.googleapis.com/books/v1/volumes?q=";
    public static String USER_ID = FirebaseAuth.getInstance().getUid();
    public static  String USER_TYPE = "";

}

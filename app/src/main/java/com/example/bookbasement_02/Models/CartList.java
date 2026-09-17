package com.example.bookbasement_02.Models;

import java.io.Serializable;

public class CartList extends Product {
    private int cart_list_id;
    private String user_id;
    private int cart_qty;

    public CartList ( ) {
        this.cart_list_id = 0;
        this.user_id = "";
        this.cart_qty = 0;
    }

    public int getCart_qty ( ) {
        return cart_qty;
    }

    public void setCart_qty (int cart_qty) {
        this.cart_qty = cart_qty;
    }

    public int getCart_list_id ( ) {
        return cart_list_id;
    }

    public void setCart_list_id (int cart_list_id) {
        this.cart_list_id = cart_list_id;
    }

    public String getUser_id ( ) {
        return user_id;
    }

    public void setUser_id (String user_id) {
        this.user_id = user_id;
    }


}

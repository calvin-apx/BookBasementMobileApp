package com.example.bookbasement_02.RestApi;



import com.example.bookbasement_02.App;
import com.example.bookbasement_02.Models.Appointment;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.Donate;
import com.example.bookbasement_02.Models.Favorite;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.Models.Genre;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Recycle;
import com.example.bookbasement_02.Models.SearchFilter;
import com.example.bookbasement_02.Models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    /**
     * @return Collection data
     * @author Calvin
     * Get All genre list
     */
    @GET("genres/data")
    Call<List<Genre>> getGenre ( );

    /**
     * @return Collection data
     * @author Calvin
     * Get Recommendation of Books products
     */
    @GET("recommendation/data")
    Call<List<Product>> getBookRecommendation ( );

    /**
     * @return Collection data
     * @author Calvin
     * Get Recommendation of genre by genre_id passed
     */
    @GET("recommendation/dataGenre/{id}")
    Call<List<Product>> getBookGenreRecommendation (@Path("id") int id);

    @Headers({
            "Content-type: application/json"
    })
    @POST("appointments/donate/donateStore")
    Call<Donate> storeDonateAppointment (@Body Donate donate);

    @Headers({
            "Content-type: application/json"
    })
    @POST("cartList/store")
    Call<CartList> storeCartList (@Body CartList cartList);

    /**
     * @return Collection data
     * @author Calvin
     * Get All genre list
     */
    @GET("cartList/data/{id}")
    Call<List<CartList>> getCartList (@Path("id") String id);

    /**
     * @return Boolean
     * @author Calvin
     * Remove cartList
     */
    @Headers({
            "Content-type: application/json"
    })
    @POST("cartList/remove")
    Call<CartList> removeCartList (@Body CartList cartList);


    @Headers({
            "Content-type: application/json"
    })
    @POST("appointments/buy/buyStore")
    Call<Appointment> storeBuyAppointment (@Body Appointment appointment);

    /**
     * @return Collection data
     * @author Calvin
     */
    @GET("appointments/data/{user_id}")
    Call<List<Appointment>> getAppointments (@Path("user_id") String user_id);


    /**
     * @return Collection data
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })
    @POST("favorites/store")
    Call<Favorite> storeFavorites (@Body Favorite favorite);

    /**
     * @return Collection data
     * @author Calvin
     */
    @GET("favorites/data/{user_id}")
    Call<List<Favorite>> getFavorites (@Path("user_id") String user_id);

    /**
     * @return Collection
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })
    @POST("favorites/remove")
    Call<Favorite> removeFavorite (@Body Favorite favorite);

    /**
     * @return Collection
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })

    @POST("user/register")
    Call<Users> setUsers (@Body Users users);

    /**
     * @return Boolean
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })

    @POST("user/checkUser")
    Call<Users> checkUser (@Body Users users);

    /**
     * @return Collection
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })

    @POST("products/findFiltered")
    Call<List<Product>> setSearch (@Body SearchFilter searchFilter);


    /**
     * @return Collection
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })

    @POST("appointments/sell/sellStore")
    Call<Appointment> sellStore (@Body Appointment appointment);


    /**
     * @return Collection data
     * @author Calvin
     * Get person information
     */
    @GET("user/data/{user_id}")
    Call<Users> getUser (@Path("user_id") String user_id);


    /**
     * @return Collection data
     * @author Calvin
     * Get person information
     */
    @GET("recycle/data")
    Call<List<Recycle>> getRecycle();


    /**
     * @return Collection
     * @author Calvin
     */
    @Headers({
            "Content-type: application/json"
    })

    @POST("appointments/recycle/recycleStore")
    Call<Recycle> storeRecycle (@Body Recycle recycle);


}

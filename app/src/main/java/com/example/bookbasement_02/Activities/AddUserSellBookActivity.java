package com.example.bookbasement_02.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.R;
import com.tapadoo.alerter.Alerter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddUserSellBookActivity extends AppCompatActivity {
    private ImageView imageViewUploaded;
    private AwesomeValidation awesomeValidation;
    private EditText bookTitle, bookDescriptiveTitle, bookGenre, bookDescription, bookPrice, bookQuantity, bookAuthor;
    private Button submitButton, uploadButton;
    private static final int CAMERA_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 1;
    private Uri image_uri;
    private Bitmap bmp = null;
    boolean imageUrl = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        setContentView(R.layout.add_sale_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookTitle = findViewById(R.id.title_id);
        bookDescriptiveTitle = findViewById(R.id.book_descriptive_title_id);
        bookGenre = findViewById(R.id.book_genre_id);
        bookDescription = findViewById(R.id.book_description_id);
        bookPrice = findViewById(R.id.book_price_id);
        bookQuantity = findViewById(R.id.book_quantity_id);
        bookAuthor = findViewById(R.id.book_author_id);
        imageViewUploaded = findViewById(R.id.book_image_id);
        submitButton = findViewById(R.id.button_submit_id);

        //get Intent
        imageUrl = getIntent().getBooleanExtra("imagePath", false);

        if(imageUrl){
            bmp  = BitmapTransfer.bitmap;
            imageViewUploaded.setImageBitmap(bmp);
        }


        awesomeValidation.addValidation(this, R.id.book_price_id,
                "^(\\d{0,9}\\.\\d{1,4}|\\d{1,9})$", R.string.errorBookPrice);
        awesomeValidation.addValidation(this, R.id.book_quantity_id,
                "^[0-9]+$", R.string.errorBookQuantity);
        awesomeValidation.addValidation(this, R.id.title_id,
                RegexTemplate.NOT_EMPTY, R.string.errorBookTitle);
        awesomeValidation.addValidation(this, R.id.book_descriptive_title_id,
                RegexTemplate.NOT_EMPTY, R.string.errorBookDescriptiveTitle);
        awesomeValidation.addValidation(this, R.id.book_genre_id,
                RegexTemplate.NOT_EMPTY, R.string.errorBookGenre);
        awesomeValidation.addValidation(this, R.id.book_description_id,
                RegexTemplate.NOT_EMPTY, R.string.errorBookDescription);
        awesomeValidation.addValidation(this, R.id.book_author_id,
                RegexTemplate.NOT_EMPTY, R.string.errorBookAuthor);

        imageViewUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getBaseContext() , DetectObjectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate() && bmp != null) {
                    Product product = new Product();
                    product.setTitle(bookTitle.getText()+"");
                    product.setSub_title(bookDescriptiveTitle.getText()+"");
                    product.setGenre(bookGenre.getText()+"");
                    product.setDescription(bookDescription.getText()+"");
                    product.setPrice(Double.parseDouble(bookPrice.getText().toString()));
                    product.setQty(Integer.parseInt(bookQuantity.getText().toString()));
                    product.setAuthors(bookAuthor.getText().toString());
                    product.setImage_url(imageToString(bmp));
                    Intent intent = new Intent(getBaseContext(), AppointmentSetterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("user_id",URL.USER_ID);
                    intent.putExtra("purpose","sell");
                    intent.putExtra("product", product);
                    getBaseContext().startActivity(intent);
                } else {
                    new SweetAlertDialog(AddUserSellBookActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!\n" +
                                    "Please check your inputs")
                            .show();
                }
            }
        });


    }


    private String imageToString(Bitmap imgBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}

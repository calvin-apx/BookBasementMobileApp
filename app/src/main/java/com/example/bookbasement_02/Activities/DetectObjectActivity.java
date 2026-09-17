package com.example.bookbasement_02.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bookbasement_02.Models.ObjectDetection;
import com.example.bookbasement_02.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetectObjectActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1;
    Button button;
    TextView booksPercent, laptopPercent, cellphonePercent, coffeePercent, pencilPercent, tablePercent;
    ProgressBar bookProgressBar, laptopProgressBar, cellphoneProgressBar, coffeeProgressBar, pencilProgressBar, tableProgressBar;
    ImageView imageView;
    AutoMLImageLabelerOptions autoMLImageLabelerOptions;
    List<ObjectDetection> objectDetectionList = new ArrayList<>();
    private Uri imageUri;
    private Bitmap bitmap;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_object);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        booksPercent = findViewById(R.id.book_percent);
        laptopPercent = findViewById(R.id.laptop_percent);
        cellphonePercent = findViewById(R.id.cellphone_percent);
        coffeePercent = findViewById(R.id.coffee_percent);
        pencilPercent = findViewById(R.id.pencil_percent);
        tablePercent = findViewById(R.id.table_percent);

        bookProgressBar = findViewById(R.id.book_progress);
        laptopProgressBar = findViewById(R.id.laptop_progress);
        cellphoneProgressBar = findViewById(R.id.cellphone_progress);
        coffeeProgressBar = findViewById(R.id.coffee_progress);
        pencilProgressBar = findViewById(R.id.pencil_progress);
        tableProgressBar = findViewById(R.id.table_progress);

        button = findViewById(R.id.image_btn_id);
        imageView = findViewById(R.id.image_id);

        AutoMLImageLabelerLocalModel localModel =
                new AutoMLImageLabelerLocalModel.Builder()
                        .setAssetFilePath("manifest.json")
                        .build();

        autoMLImageLabelerOptions =
                new AutoMLImageLabelerOptions.Builder(localModel)
                        .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                openImage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                float confidence = (float) bookProgressBar.getProgress();
                if(confidence > 50.0f){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    BitmapTransfer.bitmap = bitmap;
                    final byte[] byteArray = stream.toByteArray();

                    new SweetAlertDialog(DetectObjectActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Objected Detected")
                            .setContentText("Label : Book , confidence: " + String.format("%.2f", (confidence)) + "%")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick (SweetAlertDialog sDialog) {
                                    Intent intent = new Intent(getBaseContext() , AddUserSellBookActivity.class);
                                    intent.putExtra("imagePath",true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getBaseContext().startActivity(intent);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }else{
                    new SweetAlertDialog(DetectObjectActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("This image is not a book")
                            .show();
                }


            }
        });
    }

    private void openImage ( ) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            bitmap= (Bitmap) data.getExtras().get("data");
//            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
            imageView.setImageBitmap(bitmap);
            InputImage image;
            assert bitmap != null;
            image = InputImage.fromBitmap(bitmap ,0);

            ImageLabeler labeler = ImageLabeling.getClient(autoMLImageLabelerOptions);
            labeler.process(image).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                @Override
                public void onSuccess (List<ImageLabel> imageLabels) {
                    // Task completed successfully
                    // ...
                    String result = "";
                    for (ImageLabel label : imageLabels) {
                        String labels = label.getText();
                        float confidence = label.getConfidence();
                        int index = label.getIndex();
                        objectDetectionList.add(new ObjectDetection(labels, (confidence * 100)));
                        if ("books".equals(labels)) {
                            booksPercent.setText(String.format("%.2f", (confidence * 100)) + "%");
                            bookProgressBar.setProgress((int) (confidence * 100));
                        } else if ("laptop".equals(labels)) {
                            laptopPercent.setText(String.format("%.2f", (confidence * 100)) + "%");
                            laptopProgressBar.setProgress((int) (confidence * 100));

                        } else if ("cellphone".equals(labels)) {
                            cellphonePercent.setText(String.format("%.2f", (confidence * 100)) + "%");
                            cellphoneProgressBar.setProgress((int) (confidence * 100));

                        } else if ("coffee".equals(labels)) {
                            coffeePercent.setText(String.format("%.2f", (confidence * 100)) + "%");
                            coffeeProgressBar.setProgress((int) (confidence * 100));

                        } else if ("table".equals(labels)) {
                            tablePercent.setText(String.format("%.2f", (confidence * 100)) + "%");
                            tableProgressBar.setProgress((int) (confidence * 100));

                        } else if ("pencil".equals(labels)) {
                            pencilPercent.setText(String.format("%.2f", (confidence * 100)) + "%");
                            pencilProgressBar.setProgress((int) (confidence * 100));
                        } else {

                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure (@NonNull Exception e) {
                    // Task failed with an exception
                    // ...
                }
            });

        }
    }

}
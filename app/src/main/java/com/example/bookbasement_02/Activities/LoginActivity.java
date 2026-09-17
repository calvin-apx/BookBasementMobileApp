package com.example.bookbasement_02.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail,
            editTextPassword;
    private Button btnSignIn;
    private TextView signUpTextVIew , recyclingTextView;
    private DatabaseReference databaseReference;

    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private Api api = new Api();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getSupportActionBar().hide();

        initializeData();
        setupValidation();
        setActions();


    }

    private void initializeData ( ) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.email_id);
        editTextPassword = (EditText) findViewById(R.id.password_id);
        btnSignIn = (Button) findViewById(R.id.btn_submit_id);
        signUpTextVIew = (TextView) findViewById(R.id.btn_register_id);
//        recyclingTextView = findViewById(R.id.login_with_recycling_facility_id);
    }

    private void setupValidation ( ) {
        awesomeValidation.addValidation(this, R.id.email_id,
                RegexTemplate.NOT_EMPTY, R.string.error_email);
        awesomeValidation.addValidation(this, R.id.password_id,
                RegexTemplate.NOT_EMPTY, R.string.error_password);

    }

    private void setActions ( ) {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (awesomeValidation.validate()) {
                    checkUserFirebase(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                } else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!" +
                                    "\nPlease check your inputs!")
                            .show();
                }
            }
        });
        signUpTextVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
                finish();
            }
        });

//        recyclingTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                Intent intent = new Intent(getBaseContext(), LoginWithRecyclingFacilitiesActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getBaseContext().startActivity(intent);
//                finish();
//            }
//        });
    }

    public void checkUserFirebase (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN-ACTIVITY", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            URL.USER_ID = user.getUid();
                            Call<Users> usersCall = api.apiInterface.getUser(URL.USER_ID);
                            usersCall.enqueue(new Callback<Users>() {
                                @Override
                                public void onResponse (Call<Users> call, Response<Users> response) {

                                    if (!response.isSuccessful()) {
                                        new SweetAlertDialog(getBaseContext(), SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Something went wrong!")
                                                .show();
                                    }else{
                                        Users users = response.body();
                                        URL.USER_TYPE = users.getType();
                                        if (URL.USER_TYPE.equals("Recycling Facility")) {
                                            Intent intent = new Intent(getBaseContext(), RecylingFacilitiesActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getBaseContext().startActivity(intent);
                                            finish();
                                        } else if(URL.USER_TYPE.equals("Normal")){
                                            Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getBaseContext().startActivity(intent);
                                            finish();
                                        }else{
                                            Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getBaseContext().startActivity(intent);
                                            finish();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure (Call<Users> call, Throwable t) {

                                }
                            });

                        } else {
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!" +
                                            "\nPlease check your inputs!")
                                    .show();
                        }


                    }
                });

    }

    public void onStart ( ) {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            URL.USER_ID = currentUser.getUid();
            if (URL.USER_TYPE.equals("Recycling Facility")) {
                Intent intent = new Intent(getBaseContext(), RecylingFacilitiesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
                finish();
            }

        }
    }


}

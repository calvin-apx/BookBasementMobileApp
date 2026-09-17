package com.example.bookbasement_02.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Helpers.DateTimeHelper;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextName,
            editTextEmail,
            editTextPassword,
            editTextPhone;

    private Button btnSignUp , btnBirthDate;
    private TextView signInTextVIew;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DateTimeHelper dateTimeHelper;
    private String genderSelected = "male";
    private String userType = "Normal";
    private Spinner userTypeSpinner;

    private AwesomeValidation awesomeValidation;
    private Api api = new Api();

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        getSupportActionBar().hide();

        //initalized data
        initializeData();
        //setup error validation
        setupValidation();
        //setup actions
        setActions();

    }

    private void initializeData ( ) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        dateTimeHelper = new DateTimeHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();

        editTextName = (EditText) findViewById(R.id.name_id);
        editTextEmail = (EditText) findViewById(R.id.email_id);
        editTextPassword = (EditText) findViewById(R.id.password_id);
        editTextPhone = (EditText) findViewById(R.id.phone_id);
        btnBirthDate = (Button) findViewById(R.id.brith_date_id);
        radioGroup = (RadioGroup) findViewById(R.id.genderGroup);
        btnSignUp = (Button) findViewById(R.id.btn_submit_id);
        signInTextVIew = (TextView) findViewById(R.id.btn_signin_id);
        userTypeSpinner = (Spinner) findViewById(R.id.type_id);
    }

    private void setupValidation ( ) {
        awesomeValidation.addValidation(this, R.id.name_id,
                RegexTemplate.NOT_EMPTY, R.string.error_name);
        awesomeValidation.addValidation(this, R.id.email_id,
                RegexTemplate.NOT_EMPTY, R.string.error_email);
        awesomeValidation.addValidation(this, R.id.password_id,
                RegexTemplate.NOT_EMPTY, R.string.error_password);
        awesomeValidation.addValidation(this, R.id.phone_id,
                RegexTemplate.TELEPHONE, R.string.error_phone);
        awesomeValidation.addValidation(this, R.id.brith_date_id,
                RegexTemplate.NOT_EMPTY, R.string.error_date);
    }

    private void setActions ( ) {

        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                dateTimeHelper.handleDate(btnBirthDate);
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                genderSelected = radioButton.getText().toString();
                setGenderSelected(genderSelected);

            }
        });

        signInTextVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (awesomeValidation.validate()) {
                    register(editTextName.getText() + "", editTextEmail.getText() + "", editTextPassword.getText() + "");
                } else {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!" +
                                    "\nPlease check your inputs!")
                            .show();
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                userType = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });
    }


    private void register (final String username, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userid = firebaseUser.getUid();
                            URL.USER_ID = userid;


                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("email", editTextEmail.getText() + "");
                            hashMap.put("birthday", btnBirthDate.getText() + "");
                            hashMap.put("gender", genderSelected);
                            hashMap.put("phone", editTextPhone.getText() + "");
                            hashMap.put("type", userType);
                            hashMap.put("status", "offline");
                            hashMap.put("imageURL", "default");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete (@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        saveUser(userid);

                                        if (userType.equals("Recycling Facility")) {
                                            Intent intent = new Intent(getBaseContext(), RecylingFacilitiesActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getBaseContext().startActivity(intent);
                                            finish();
                                        } else if(userType.equals("Normal")){
                                            Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getBaseContext().startActivity(intent);
                                            finish();
                                        }else{
                                            new SweetAlertDialog(getBaseContext(), SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText("Something went wrong!")
                                                    .show();
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
                            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(task.getException().getMessage().toString())
                                    .show();
                        }
                    }
                });
    }

    public void setGenderSelected (String genderSelected) {
        this.genderSelected = genderSelected;
    }


    public void onStart ( ) {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
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

    public void saveUser (String userId) {
        Users user = new Users();
        user.setUser_id(userId);
        user.setName(editTextName.getText() + "");
        user.setEmail(editTextEmail.getText() + "");
        user.setPassword(editTextPassword.getText() + "");
        user.setPhone(editTextPhone.getText() + "");
        user.setBirth_date(btnBirthDate.getText() + "");
        user.setGender(genderSelected);
        user.setType(userType);
        user.setStatus("AC");

        Call<Users> call = api.apiInterface.setUsers(user);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse (Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Users users = response.body();
                    Log.d("Success Register", "Return : " + users.toString());
                }
            }

            @Override
            public void onFailure (Call<Users> call, Throwable t) {
                System.out.println(t);
            }
        });
    }


}

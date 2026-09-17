package com.example.bookbasement_02.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Helpers.DateTimeHelper;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1;
    View.OnClickListener changeProfile = new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            openImage();
        }
    };
    private EditText editTextName, editTextEmail, editTextPhone;
    private CircleImageView circleImageView;
    private Button btnSubmit, btnBirthDate;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DateTimeHelper dateTimeHelper;
    private String genderSelected = "male";
    private AwesomeValidation awesomeValidation;
    private Api api = new Api();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        initializeData();
        setupValidation();
        setActions();

    }

    private void initializeData ( ) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        dateTimeHelper = new DateTimeHelper(this);

        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        editTextName = (EditText) findViewById(R.id.name_id);
        editTextEmail = (EditText) findViewById(R.id.email_id);
        editTextPhone = (EditText) findViewById(R.id.phone_id);
        btnBirthDate = (Button) findViewById(R.id.brith_date_id);
        radioGroup = (RadioGroup) findViewById(R.id.genderGroup);
        btnSubmit = (Button) findViewById(R.id.btn_submit_id);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }

    private void setupValidation ( ) {
        awesomeValidation.addValidation(this, R.id.name_id,
                RegexTemplate.NOT_EMPTY, R.string.error_name);
        awesomeValidation.addValidation(this, R.id.email_id,
                RegexTemplate.NOT_EMPTY, R.string.error_email);
        awesomeValidation.addValidation(this, R.id.password_id,
                RegexTemplate.NOT_EMPTY, R.string.error_password);
        awesomeValidation.addValidation(this, R.id.phone_id,
                RegexTemplate.NOT_EMPTY, R.string.error_phone);
        awesomeValidation.addValidation(this, R.id.brith_date_id,
                RegexTemplate.NOT_EMPTY, R.string.error_date);
    }

    private void setActions ( ) {
        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                dateTimeHelper.handleDate((Button) btnBirthDate);
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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers firebaseUsers = snapshot.getValue(FirebaseUsers.class);
                assert firebaseUsers != null;
                editTextName.setText(firebaseUsers.getUsername());
                editTextEmail.setText(firebaseUsers.getEmail());
                editTextPhone.setText(firebaseUsers.getPhone());
                btnBirthDate.setText(firebaseUsers.getBirthday());
                if (firebaseUsers.getGender().equals("male")) {
                    radioGroup.check(R.id.radioMale);
                    radioButton = findViewById(R.id.radioMale);
                    genderSelected = radioButton.getText().toString();
                    setGenderSelected(genderSelected);
                } else {
                    radioGroup.check(R.id.radioFemale);
                    radioButton = findViewById(R.id.radioFemale);
                    genderSelected = radioButton.getText().toString();
                    setGenderSelected(genderSelected);

                }
                if (firebaseUsers.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.default_image);
                } else {
                    Glide.with(getBaseContext()).load(firebaseUsers.getImageURL()).into(circleImageView);
                }

            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

        circleImageView.setOnClickListener(changeProfile);
    }

    private void openImage ( ) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension (Uri uri) {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage ( ) {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileReferense = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = fileReferense.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then (@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReferense.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete (@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        databaseReference.updateChildren(map);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure (@NonNull Exception e) {
                    new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!" + "\n" + e.getMessage())
                            .show();
                    progressDialog.dismiss();
                }
            });

        } else {
            new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No image selected!")
                    .show();
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Upload in progress")
                        .show();
            }else{
                uploadImage();
            }
        }
    }

    public void setGenderSelected (String genderSelected) {
        this.genderSelected = genderSelected;
    }
}
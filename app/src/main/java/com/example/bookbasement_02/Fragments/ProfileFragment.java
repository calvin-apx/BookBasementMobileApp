package com.example.bookbasement_02.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Activities.ContainerActivity2;
import com.example.bookbasement_02.Activities.EditProfileActivity;
import com.example.bookbasement_02.Activities.GenreSearchActivity;
import com.example.bookbasement_02.Adapters.ProfileAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Button edit_profile_btn;
    CircleImageView profileImage;
    TextView nameTextView,phoneTextView,pointsTextView;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    final Context context = getContext();
    Api api = new Api();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.container_profile_fragments, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        edit_profile_btn = view.findViewById(R.id.edit_profile_btn);
        profileImage = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.name_textview);
        phoneTextView = view.findViewById(R.id.phone_textview_id);
        pointsTextView = view.findViewById(R.id.points_textview);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers firebaseUsers = snapshot.getValue(FirebaseUsers.class);
                nameTextView.setText(firebaseUsers.getUsername());
                phoneTextView.setText(firebaseUsers.getPhone());

                if(firebaseUsers.getImageURL().equals("default")){
                    profileImage.setImageResource(R.drawable.default_image);
                }else{
                    if (isValidContextForGlide(view.getContext())){
                        Glide.with(view.getContext()).load(firebaseUsers.getImageURL()).into(profileImage);
                    }

                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });
        getUser();


        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("HISTORY");
        arrayList.add("FAVORITES");
        arrayList.add("PROFILE");

        prepareViewPager(viewPager, arrayList);

        tabLayout.setupWithViewPager(viewPager);

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getContext() , EditProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void getUser ( ) {
        Call<Users> usersCall = api.apiInterface.getUser(URL.USER_ID);
        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse (Call<Users> call, Response<Users> response) {


                if (!response.isSuccessful()) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }else{
                    Users users = response.body();
//                    System.out.println("users for me");
//                    System.out.println(users);
                    pointsTextView.setText(users.getPoints()+"");
                }

            }

            @Override
            public void onFailure (Call<Users> call, Throwable t) {

            }
        });
    }

    private void prepareViewPager (ViewPager viewPager, ArrayList<String> arrayList) {
        ProfileAdapter adapter = new ProfileAdapter(getChildFragmentManager());
        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new HistoryFragment());
        fragments.add(new FavoritesFragment());
        fragments.add(new InformationFragment());

        for (int x = 0; x < arrayList.size(); x++) {    
            adapter.addFragment(fragments.get(x), arrayList.get(x));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume ( ) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            URL.USER_ID = firebaseUser.getUid();
        }
        super.onResume();
    }

    public void onStart ( ) {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            URL.USER_ID = firebaseUser.getUid();
        }
    }


}

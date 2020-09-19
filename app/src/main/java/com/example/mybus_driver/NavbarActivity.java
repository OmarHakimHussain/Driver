package com.example.mybus_driver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class NavbarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference reference;
    private String userID;
    private String mName;
    private String mEmail;
    private String mProfileImageUrl;
    private String dProfileImageUrl;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);
        //dy mohma
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        //Di mesh sha3'ala ya 7akimmmmmmmmmm
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);

        mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Log.e("Omar",userID);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
        // Query query = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Context context;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
      /*FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      reference=firebaseDatabase
      DatabaseReference myRef = firebaseDatabase.getReference("User");
        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail() , "online")); */

        context = Objects.requireNonNull(NavbarActivity.this).getApplicationContext();

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this , drawer , toolbar,
                R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // 3shan ashoof default al home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                new Map_Home_Fragment()).commit();


        //updateNavHeader();
        getUserInfo();

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_profile :

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new ProfileFragment()).commit();
                toolbar.setTitle("My Profile");
                break;

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new Map_Home_Fragment()).commit();
                toolbar.setTitle("Home");

                break;

            case R.id.nav_line:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new LineFragment()).commit();
                toolbar.setTitle("Bus Lines");
                  /* pd = new ProgressDialog(NavbarActivity.this);
                  pd.setMessage("Please Wait .. Bus-Lines is Loading ");
                   pd.show();*/
                 Toast.makeText(NavbarActivity.this, "Please Wait .. Bus-Lines is Loading", Toast.LENGTH_SHORT).show();
                 break;
            case R.id.nav_C_List:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new Customer_listFragment()).commit();
                toolbar.setTitle("Customer Attendance List");
                break;
            case R.id.nav_complaint:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new ComplaintFragment()).commit();
                toolbar.setTitle("Complaint");
                break;
            case R.id.nav_logout :

                FirebaseAuth.getInstance().signOut();
                Intent go = new Intent(getApplicationContext() , DriverLoginActivity.class);
                startActivity(go);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

      // pd.dismiss();
        return true;
    }


    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else { // navigation is not open then will do this
            super.onBackPressed();
        }
    }
   /* public void updateNavHeader()
    {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_email);
        ImageView navImage = headerView.findViewById(R.id.nav_user_photo);


        navUserMail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());


        Glide.with(this).load(currentUser.getPhotoUrl()).into(navImage);




    }*/
   private void getUserInfo() {
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                   Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                   NavigationView navigationView = findViewById(R.id.nav_view);
                   View headerView = navigationView.getHeaderView(0);
                   TextView navUserName = headerView.findViewById(R.id.nav_username);
                   TextView navUserMail = headerView.findViewById(R.id.nav_user_email);
                   ImageView navImage = headerView.findViewById(R.id.nav_user_photo);

                   if (map.get("Name") != null) {
                       mName = map.get("Name").toString();
                       navUserName.setText(mName);
                   }

                   if (map.get("Email") != null) {
                       mEmail = map.get("Email").toString();
                       navUserMail.setText(mEmail);
                   }
                   if (map.get("Image_URL") != null) {
                       //mProfileImageUrl = map.get("Image_URL").toString();
                       //Glide.with(NavbarActivity.this.getApplicationContext()).load(mProfileImageUrl).into(navImage);
                       dProfileImageUrl = map.get("Image_URL").toString();
                       Glide.with(NavbarActivity.this.getApplicationContext()).load(dProfileImageUrl).centerCrop().circleCrop().into(navImage);
                   }
               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(NavbarActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();

           }
       });
   }


}

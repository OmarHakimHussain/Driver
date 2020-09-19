package com.example.mybus_driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverChooseActivity extends AppCompatActivity {
    private static final String TAG ="" ;
    Button D_RegisterBtn,D_LoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_choose);


        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Driver");


        //init views
        D_RegisterBtn =findViewById(R.id.D_register_btn);
        D_LoginBtn =findViewById(R.id.D_login_btn);

        //handle register button click
        D_RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start RegisterActivity
                startActivity(new Intent(DriverChooseActivity.this,DriverRegisterActivity.class));
            }
        });
        //handle login button click
        D_LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start LoginActivity
                startActivity(new Intent(DriverChooseActivity.this, DriverLoginActivity.class));
            }
        });

        // Method yfdal Loged in until sign out
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(DriverChooseActivity.this, NavbarActivity.class);
            DriverChooseActivity.this.finish();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }*/



    }
}

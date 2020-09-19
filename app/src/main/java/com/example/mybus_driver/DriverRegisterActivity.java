package com.example.mybus_driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class DriverRegisterActivity extends AppCompatActivity {
    private static final String TAG ="" ;
    //views
    EditText mEmailEt, mPasswordEt, mnameEt, mphoneEt, midEt, mnidEt;
    Button D_RegisterBtn;
    TextView mHaveAccountTv;
    RadioGroup radioGroup;
    RadioButton radiomale, radiofemale;
    TextView textView;
    private String str_gender = "";
    private Uri resultUri;
    FirebaseUser firebaseUser;

    private static final int PICK_FILE = 1;
    //progressbar to display while registering user
    ProgressDialog progressDialog;

    private ImageView img;
   // private Uri filePath;
    long maxid = 0;

    private FirebaseStorage storage;
   // private StorageReference storageReference;
    Spinner spinner;
    String str ="";

    FirebaseAuth auth;
    private DatabaseReference reference;
    ProgressDialog pd;
    private StorageReference filePath;
    private String userid;

    private String str_id;
    private String str_email;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        //Spinner Lines
        spinner =findViewById(R.id.spinner1);
        ArrayList<String> arrayList= new ArrayList<>();
        arrayList.add("El-Mohandessin");
        arrayList.add("Dokki");
        arrayList.add("El-Haram");
        arrayList.add("Nasr City");
        arrayList.add("Helioplis");
        arrayList.add("New Cairo");
        arrayList.add("El-Maadi");
        arrayList.add("Ain-Shams");
        arrayList.add("Shoubra-Masr");
        arrayList.add("El-Sheikh Zayed City");
        arrayList.add("6-October City");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        // adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str = (String) spinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                str= "No Line";

            }
        });

        str = (String) spinner.getSelectedItem().toString();

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mnameEt = findViewById(R.id.nameEt);
        mphoneEt = findViewById(R.id.phoneEt);
        midEt = findViewById(R.id.idEt);
        mnidEt = findViewById(R.id.nidEt);

        radioGroup = findViewById(R.id.radioGroup);
        radiomale = findViewById(R.id.radio_1);
        radiofemale = findViewById(R.id.radio_2);

        textView = findViewById(R.id.txt_view_selected);

        D_RegisterBtn = findViewById(R.id.D_registerBtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);
        img = findViewById(R.id.D_img);

        storage = FirebaseStorage.getInstance();
       // storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

// da 5as b el soora 3shan a3ml choose
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        /*text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_registerActivity.this,Start_LoginActivity.class));
            }
        });*/


        D_RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(DriverRegisterActivity.this);
                pd.setMessage("Please Wait while Register Complete");
                //  pd.setMessage("Registering Driver...");
                pd.show();

                //...........................................................
                //Get user all data from edit text
                String str_name = mnameEt.getText().toString();
                str_email = mEmailEt.getText().toString();
                str_id = midEt.getText().toString();
                String str_phone = mphoneEt.getText().toString();
                String str_password = mPasswordEt.getText().toString();
                String str_nid = mnidEt.getText().toString();



                //validate
                if (mnameEt.getText().toString().isEmpty()) {
                    //set error and focus to name edittext
                    pd.dismiss();
                    mnameEt.setError("Invalid Name");
                    mnameEt.setFocusable(true);
                } else if (mnameEt.getText().toString().length() > 16) {
                    //set error and focus to name edittext
                    pd.dismiss();
                    mnameEt.setError("Name Length at Most 16 Characters");
                    mnameEt.setFocusable(true);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEt.getText().toString()).matches()) {
                    //set error and focus to email edittext
                    pd.dismiss();
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                } else if (midEt.getText().toString().isEmpty()) {
                    //set error and focus to id edittext
                    pd.dismiss();
                    midEt.setError("Invalid ID");
                    midEt.setFocusable(true);
                } else if (mphoneEt.getText().toString().isEmpty()) {
                    //set error and focus to id edittext
                    pd.dismiss();
                    mphoneEt.setError("Invalid Phone Number");
                    mphoneEt.setFocusable(true);
                }else if (mphoneEt.getText().toString().length() < 11 || mphoneEt.getText().toString().length() > 11) {
                    //set error and focus to rc edittext
                    pd.dismiss();
                    mphoneEt.setError("Phone Number Length is 11 Numbers");
                    mphoneEt.setFocusable(true);
                } else if (mnidEt.getText().toString().isEmpty()) {
                    //set error and focus to rc edittext
                    pd.dismiss();
                    mnidEt.setError("Invalid National ID");
                    mnidEt.setFocusable(true);
                } else if (mnidEt.getText().toString().length() < 14 || mnidEt.getText().toString().length() > 14 ) {
                    //set error and focus to rc edittext
                    pd.dismiss();
                    mnidEt.setError("National ID Length is 14 Numbers");
                    mnidEt.setFocusable(true);
                }else if (mPasswordEt.getText().toString().length() < 6 || mPasswordEt.getText().toString().isEmpty() ) {
                    //set error and focus to password edit_text
                    pd.dismiss();
                    mPasswordEt.setError("Password Length at Least 6 Characters");
                    mPasswordEt.setFocusable(true);
                }else if (!radiomale.isChecked() && !radiofemale.isChecked() ) {
                    //set error and focus to radiobutton
                    pd.dismiss();
                    radiomale.setError("Please Select Your Gender");
                    radiofemale.setError("Please Select Your Gender");
                    radioGroup.setFocusable(true);
                } else {
                    register(str_name,str_email,str_id,str_phone,str_password,str_nid,str_gender,str);



                    //progressDialog.show();
                    //reference.child(String.valueOf(maxid + 1)).setValue(mnameEt.getText().toString()+mEmailEt.getText().toString()+midEt.getText().toString()
                    //+mphoneEt.getText().toString()+ mPasswordEt.getText().toString());
                    //reference.child(String.valueOf(maxid + 1)).setValue(hashMap);

                }
            }
        });

        // Method yfdal Loged in until sign out
       /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(DriverRegisterActivity.this, NavbarActivity.class);
            DriverRegisterActivity.this.finish();

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }*/

        //handle login textview click listener
        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverRegisterActivity.this, DriverLoginActivity.class));
                finish();
            }
        });

    }


//.....................................................................



//dy el method el mas2ola 3n el e5tyar el sora


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //final Uri imageUri = data.getData();
            resultUri = data.getData();
            if (resultUri != null) {
                // saveUserInformation();
               img.setImageURI(resultUri);
              //Glide.with(DriverRegisterActivity.this.getApplicationContext()).load(resultUri).centerCrop().circleCrop().into(img);
            }
        }
    }


    private void uploadImage() {
        filePath = FirebaseStorage.getInstance().getReference().child("Users Images/Drivers Images/").child(str_email+"_"+str_id+".png");
        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    requireNonNull(task.getResult()).getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uriDownUrl = task.getResult();
                            img.setImageURI(resultUri);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Image_URL", requireNonNull(uriDownUrl).toString());
                            reference.updateChildren(userInfo);
                        }
                    });
                }
            }


        });
    }

//.........................................................................................

    public void checkButton(View view) {
        if (radiomale.isChecked()) {
            str_gender = "Male";
        } else if (radiofemale.isChecked()) {
            str_gender = "Female";
        }
    }


//Code for user authentication in database

    public void register (final String name, final String email, final String id,final String phone, final String password, final String nid,final String gender,final String line) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                (DriverRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           FirebaseUser firebaseUser = auth.getCurrentUser();
                           userid = firebaseUser.getUid();
                           reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userid);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            //put user data in database
                            hashMap.put("Reference_ID",userid);
                            hashMap.put("Name",name);
                            hashMap.put("Email",email);
                            hashMap.put("ID",id);
                            hashMap.put("Phone",phone);
                            hashMap.put("Password",password);
                            hashMap.put("National_ID",nid);
                            hashMap.put("Gender",gender);
                            hashMap.put("Line_Name",line);
                           // hashMap.put("Image_URL","");
                            //hashMap.put("Complaint","");
                            hashMap.put("Login_Type","Driver");
                            uploadImage();
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(DriverRegisterActivity.this, NavbarActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(DriverRegisterActivity.this, "Welcome Mr/Ms : "+name, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }else {
                            pd.dismiss();
                            Toast.makeText(DriverRegisterActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}

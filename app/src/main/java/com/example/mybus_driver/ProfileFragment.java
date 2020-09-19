package com.example.mybus_driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ProfileFragment extends Fragment {
    EditText mEmailEt, mPasswordEt, mnameEt, mphoneEt, midEt, mnidEt;
    Button D_confirmBtn;
    private ImageView img;
    TextView txt_line, txt_gender,txt_recover;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog pd;
    private DatabaseReference reference;
    private String userID;
    private String mProfileImageUrl;
    private String dProfileImageUrl;

    private String str_name;
    private String str_email;
    private String str_id;
    private String str_phone;
    private String str_password;
    private String str_nid;
    private String str_gender;
    private String str_line;

    private Uri resultUri;
    private StorageReference filePath;



    // and al 3mlha
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        img = v.findViewById(R.id.c_img);
        mEmailEt = v.findViewById(R.id.emailEt);
        mPasswordEt = v.findViewById(R.id.passwordEt);
        mnameEt = v.findViewById(R.id.nameEt);
        mphoneEt = v.findViewById(R.id.phoneEt);
        midEt = v.findViewById(R.id.idEt);
        mnidEt = v.findViewById(R.id.nidEt);
        txt_line = v.findViewById(R.id.txt_line);
        txt_gender = v.findViewById(R.id.txt_gender);
        D_confirmBtn = v.findViewById(R.id.D_confirmBtn);
        txt_recover= v.findViewById(R.id.recoverpassTv);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        txt_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
            D_confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Please Wait, Your Information Will be Update Soon", Toast.LENGTH_SHORT).show();
                    uploadImage_text();
                }
            });

        mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
        Context context;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // context = Objects.requireNonNull(getContext()).getApplicationContext();

        getUserInfo();
        return v;


    }

    private void getUserInfo() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("Image_URL") != null) {
                        mProfileImageUrl = map.get("Image_URL").toString();
                        Glide.with(getActivity().getApplicationContext()).load(mProfileImageUrl).centerCrop().circleCrop().into(img);
                        dProfileImageUrl = map.get("Image_URL").toString();
                        Glide.with(getActivity().getApplicationContext()).load(dProfileImageUrl).centerCrop().circleCrop().into(img);
                    }
                    if (map.get("Name") != null) {
                        str_name = map.get("Name").toString();
                        mnameEt.setText(str_name);
                    }
                    if (map.get("Email") != null) {
                        str_email = map.get("Email").toString();
                        mEmailEt.setText(str_email);
                    }
                    if (map.get("ID") != null) {
                        str_id = map.get("ID").toString();
                        midEt.setText(str_id);
                    }
                    if (map.get("Phone") != null) {
                        str_phone = map.get("Phone").toString();
                        mphoneEt.setText(str_phone);
                    }
                    if (map.get("National_ID") != null) {
                        str_nid = map.get("National_ID").toString();
                        mnidEt.setText(str_nid);
                    }
                    if (map.get("Password") != null) {
                        str_password = map.get("Password").toString();
                        mPasswordEt.setText(str_password);
                    }
                    if (map.get("Gender") != null) {
                        str_gender = map.get("Gender").toString();
                        txt_gender.setText(str_gender);
                    }
                    if (map.get("Line_Name") != null) {
                        str_line = map.get("Line_Name").toString();
                        txt_line.setText(str_line);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


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
                Glide.with(getActivity().getApplicationContext()).load(resultUri).centerCrop().circleCrop().into(img);
            }
        }
    }

    private void uploadImage_text() {
        final String str_phone1;
        final String str_password1;
        if (mphoneEt.getText().toString().isEmpty()) {
            //set error and focus to id edittext
            pd.dismiss();
            mphoneEt.setError("Invalid Phone Number");
            mphoneEt.setFocusable(true);
        } else if (mphoneEt.getText().toString().length() < 11 || mphoneEt.getText().toString().length() > 11) {
            //set error and focus to rc edittext
            pd.dismiss();
            mphoneEt.setError("Phone Number Length is 11 Numbers");
            mphoneEt.setFocusable(true);
        } else if (mPasswordEt.getText().toString().length() < 6 || mPasswordEt.getText().toString().isEmpty() ) {
            //set error and focus to password edit_text
            pd.dismiss();
            mPasswordEt.setError("Password Length at Least 6 Characters");
            mPasswordEt.setFocusable(true);
        } else {
            str_phone1 = mphoneEt.getText().toString();
            //str_password1 = mPasswordEt.getText().toString();
            filePath = FirebaseStorage.getInstance().getReference().child("Users Images/Drivers Images/").child(str_email + "_" + str_id + ".png");
            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        requireNonNull(task.getResult()).getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uriDownUrl = task.getResult();
                                img.setImageURI(resultUri);
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("Image_URL", requireNonNull(uriDownUrl).toString());
                                userInfo.put("Phone", str_phone1);
                               // userInfo.put("Password", str_password1);
                                reference.updateChildren(userInfo);
                                Toast.makeText(getContext(), "Thank You, Your Information has been Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


            });
        }
    }

    private void showRecoverPasswordDialog() {
        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Recover Your Password");
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(getContext());
        //views to se in dialog
        final EditText emailEt = new EditText(getContext());
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        /* set the main width of the EditView to fit  n 'M' letters regardless of the actual text
         extension and text size*/
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email=emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //buttons cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(getContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Failed To Sent", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //get and show proper error message
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.mybus_driver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.HashMap;


public class ComplaintFragment extends Fragment {
    ImageView img_logo;
    EditText mComplainted, mid;
    Button complaint_btn;
    DatabaseReference reff;
    Complaints c;
    ProgressDialog pd;
    public ComplaintFragment() {
        // Required empty public constructor
    }

    // and al 3mlha
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_complaint, container, false);
        img_logo = v.findViewById(R.id.img_logo_complaint);
        mComplainted = v.findViewById(R.id.compalintEt);
        complaint_btn = v.findViewById(R.id.D_complaintBtn);
        mid = v.findViewById(R.id.idtEt);
        c = new Complaints();
        reff = FirebaseDatabase.getInstance().getReference().child("Complaint").child("Drivers_Complaints");


        complaint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_complaint = mComplainted.getText().toString();
                final String str_id = mid.getText().toString();
                if (str_complaint.isEmpty()) {
                    mComplainted.setError("Please Enter Your Complaint");
                    mComplainted.setFocusable(true);
                } else if (str_id.isEmpty()) {
                    mid.setError("Please Enter Your ID");
                    mid.setFocusable(true);
                } else {
                   /* final ProgressDialog pd = new ProgressDialog(getContext());
                    pd.setMessage("Submitting Your Complaint .... Please Wait");
                    pd.show();*/
                    c.setComplaint(str_complaint);
                    c.setID(str_id);
                    reff.push().setValue(c);

                    mComplainted.setText("");
                    mid.setText("");
                    Toast.makeText(getContext(), "                         Thank You\nYour Complaint has been Submitted Successfully", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getContext(), "Connection Failed\nPlease Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
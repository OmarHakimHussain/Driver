package com.example.mybus_driver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mybus_driver.Model.CustomerLocation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Customer_listFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Customers> C_List;
    private FirebaseRecyclerOptions<Customers> options;
    private FirebaseRecyclerAdapter<Customers,FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    TextView txt_clist;

    FirebaseAuth mAuth;
    private String mProfileImageUrl;
    private String dProfileImageUrl;
     String userID;
    FirebaseAuth auth;
    DatabaseReference reference1,reference2;
    ProgressDialog pd;



    public Customer_listFragment() {
        // Required empty public constructor
    }

   @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_list, container, false);
        recyclerView =v.findViewById(R.id.recyclerView_Student_List);

        mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser().getUid());
        txt_clist=v.findViewById(R.id.txt_clist1);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        C_List = new ArrayList<Customers>();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        //query ();
        databaseReference = query();
      databaseReference.keepSynced(true);
        options = new  FirebaseRecyclerOptions.Builder<Customers>().setQuery(databaseReference,Customers.class).build();


        adapter = new FirebaseRecyclerAdapter<Customers, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder holder, final int position, @NonNull final Customers model) {


                holder.txt_Cline.setText(model.getLine_Name());

                holder.txt_Cname.setText(model.getName());

                txt_clist.setText(model.getLine_Name());

                // mProfileImageUrl = model.getImage_URL();
                //Glide.with(getContext().getApplicationContext()).load(mProfileImageUrl).into(holder.image);
                dProfileImageUrl = model.getImage_URL();
                Glide.with(getContext().getApplicationContext()).load(dProfileImageUrl).into(holder.image);
                Glide.with(getContext().getApplicationContext()).load(dProfileImageUrl).centerCrop().circleCrop().into(holder.image);

                holder.imageCall.setImageResource(R.drawable.ic_phone_call);

                holder.ic_exit.setVisibility(View.GONE);

                holder.imageCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent go = new Intent(Intent.ACTION_DIAL);
                        go.setData(Uri.parse("tel:" + model.getPhone()));
                        getContext().startActivity(go);

                    }
                });


                Calendar c =Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
                String Date =simpleDateFormat.format(c.getTime());

                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss");
                String Time =simpleDateFormat1.format(c.getTime());

                final String SDate_Time= Date+" "+Time;

                Calendar c1 =Calendar.getInstance();
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyy");
                String Date1 =simpleDateFormat2.format(c.getTime());

                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("hh:mm:ss");
                String Time1 =simpleDateFormat3.format(c.getTime());

                final String EDate_Time= Date1+" "+Time1;


                holder.ic_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.ic_enter.setVisibility(View.GONE);
                        holder.ic_exit.setVisibility(View.VISIBLE);

                        //FirebaseUser firebaseUser = auth.getCurrentUser();
                        //userID = firebaseUser.getUid();

                        //String Cid=FirebaseDatabase.getInstance().getReference("Users").child("Customers").getKey();

                        //String CAid=FirebaseDatabase.getInstance().getReference("Attendance").getKey();

                        //if (Cid == CAid) {

                        String ID = model.getReference_ID();


                            reference1 = FirebaseDatabase.getInstance().getReference("Attendance").child(ID);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Status", "1");
                            hashMap.put("Name",model.getName());
                            hashMap.put("Email", model.getEmail());
                            hashMap.put("ID",model.getID());
                            hashMap.put("Line_Name",model.getLine_Name());
                            hashMap.put("SDate&Time",SDate_Time);
                            hashMap.put("EDate&Time","");

                        reference1.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Entered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    //}

                });

                holder.ic_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.ic_exit.setVisibility(View.GONE);
                        holder.ic_enter.setVisibility(View.VISIBLE);

                       // FirebaseUser firebaseUser = auth.getCurrentUser();
                       // userID = firebaseUser.getUid();

                        //String Cid=FirebaseDatabase.getInstance().getReference("Users").child("Customers").getKey();

                        //String CAid=FirebaseDatabase.getInstance().getReference("Attendance").getKey();



                        String ID = model.getReference_ID();

                        //if (Cid == CAid) {

                            reference2 = FirebaseDatabase.getInstance().getReference("Attendance").child(ID);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Status", "2");
                            hashMap.put("Name",model.getName());
                            hashMap.put("Email", model.getEmail());
                            hashMap.put("ID",model.getID());
                            hashMap.put("Line_Name",model.getLine_Name());
                            hashMap.put("EDate&Time",EDate_Time);


                        reference2.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Exit", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                   // }

                });

            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_design,parent,false));
            }
        };


        recyclerView.setAdapter(adapter);

        return v;
    }

    public void addc (){



         List<Customers> tempList = new ArrayList<>();
        DatabaseReference reff1 = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        reff1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot shipperSnapShot: dataSnapshot.getChildren())
                {
                    Customers c1 = shipperSnapShot.getValue(Customers.class);
                    c1.setReference_ID(shipperSnapShot.getKey());
                    c1.setEmail((String) shipperSnapShot.getPriority());
                    c1.setLine_Name(shipperSnapShot.getKey());
                    c1.setName(shipperSnapShot.getKey());
                    c1.setID(shipperSnapShot.getKey());

                    tempList.add(c1);
                }

         /*
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Customers c1 = snapshot.getValue(Customers.class);
                Toast.makeText(getContext(), "7alw fash5", Toast.LENGTH_SHORT).show();
*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public DatabaseReference query(){

        DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
        Query query0 = reference0.child("Line_Name");
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");


        if (query0.equals("El-Sheikh Zayed City")) {

            Query query = reff.orderByChild("Line_Name").equalTo("El-Sheikh Zayed City");

            if (query.equals(query0)) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Customers c = snapshot.getValue(Customers.class);
                                txt_clist.setText(c.getLine_Name());


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            } else if (query0.equals("Dokki")){
               //reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
                Query query2 = reff.orderByChild("Line_Name").equalTo("Dokki");


                if (query2.equals(query0)) {
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Customers c = snapshot.getValue(Customers.class);
                                    txt_clist.setText(c.getLine_Name());

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        }else {
            //Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
return reff;
    }
}

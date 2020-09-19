package com.example.mybus_driver;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LineFragment extends Fragment {
    RecyclerView recyclerView;
    List<item> Item = new ArrayList();
    FirebaseRecyclerAdapter<item, ItemViewHolder> adapter;
    SparseBooleanArray expandestate = new SparseBooleanArray();
    //ProgressDialog pd = new ProgressDialog(getContext());
    ;

    public LineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_line, container, false);
        //pd = new ProgressDialog(getContext());
       /* pd.setMessage("Please Wait ... Bus-Lines in Loading ");
        pd.show();*/

        recyclerView = v.findViewById(R.id.rc_view3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // retrieve date
        RetrieveData();

        // set Data
        setData();


        //progressBar.setVisibility(View.INVISIBLE);


        return v;


    }

    private void setData() {


        Query query = FirebaseDatabase.getInstance().getReference().child("Items");
      /*  FirebaseRecyclerOptions<item> options = new FirebaseRecyclerOptions.Builder<item>()
                .setQuery(query ,item.class)
                .build();*/

        FirebaseRecyclerOptions<item> options = new FirebaseRecyclerOptions.Builder<item>()
                .setQuery(query, item.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<item, ItemViewHolder>(options) {

            @Override
            public int getItemViewType(int position) {

                if (Item.get(position).isExpandable())

                    return 1;

                else

                    return 0;

            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull final item model) {

                switch (holder.getItemViewType()) {
                    case 0:// without item
                    {
                        ItemViewHolder viewHolder = (ItemViewHolder) holder;

                        viewHolder.setIsRecyclable(false);

                        viewHolder.txt_item.setText(model.getText());

                        //event

                        viewHolder.setItemClickListner(new ItemClickListner() {
                            @Override
                            public void onClick(View view, int position) {
                                Toast.makeText(getActivity(), "without child" + Item.get(position).getText(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;


                    case 1: //with item


                    {
                        final ItemViewHolder viewHolder = (ItemViewHolder) holder;

                        viewHolder.setIsRecyclable(false);

                        viewHolder.txt_item.setText(model.getText());


                        // because we use recycle View so we need to use expandble liner layout


                        ///////////////////////////////////////////////////////////////////////////////
                        //child 1 >> importance
                        viewHolder.expandableLinearLayout.setInRecyclerView(true);
                        viewHolder.expandableLinearLayout.setExpanded(expandestate.get(position));
                        viewHolder.expandableLinearLayout.setListener(new ExpandableLayoutListenerAdapter() {
                            @Override
                            public void onPreOpen() {
                       /* ChangRotate(viewHolder.button , 0f , 180f).start();
                        expandestate.put(position, true);*/

                                ChangRotate(viewHolder.button, 0f, 180).start();
                                expandestate.put(position, true);

                            }

                            @Override
                            public void onPreClose() {
                     /*   ChangRotate(viewHolder.button , 180f , 0f).start();
                        expandestate.put(position,false );*/

                                ChangRotate(viewHolder.button, 180f, 0f).start();
                                expandestate.put(position, false);

                            }
                        });

                        ///////////////////////////////////////////////////////////////////////////


                        /////////////////////////////////////////////////////////////////////
                        // child 2 >> importance
                        viewHolder.linearLayout.setInRecyclerView(true);
                        viewHolder.linearLayout.setExpanded(expandestate.get(position));
                        viewHolder.linearLayout.setListener(new ExpandableLayoutListenerAdapter() {
                            @Override
                            public void onPreOpen() {
                       /* ChangRotate(viewHolder.button , 0f , 180f).start();
                        expandestate.put(position, true);*/

                                ChangRotatee(viewHolder.button, 0f, 180).start();
                                expandestate.put(position, true);
                            }

                            @Override
                            public void onPreClose() {
                                ChangRotate(viewHolder.button, 180f, 0f).start();
                                expandestate.put(position, false);

                                ChangRotatee(viewHolder.button, 180f, 0f).start();
                                expandestate.put(position, false);
                            }
                        });
                        viewHolder.button.setRotation(expandestate.get(position) ? 180f : 0f);
                        viewHolder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewHolder.expandableLinearLayout.toggle(); // child 1 >> importance
                                viewHolder.linearLayout.toggle(); // child 2 >> importance
                            }
                        });
                        ///////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////
                        viewHolder.txt_child.setText(model.getSubText());
                        viewHolder.txt_child.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Toast.makeText(getActivity(), ""+viewHolder.txt_child.getText(), Toast.LENGTH_SHORT).show();
                             /*   Intent go =new Intent(getActivity(), Driver_MapsActivity.class);
                                Bundle b = new Bundle();
                                b.putString("LineName","Omar's Home");
                                b.putDouble("lat",29.977453231811523);
                                b.putDouble("lng",31.21539878845215);

                                go.putExtras(b);
                                startActivity(go);*/
                                Toast.makeText(getContext(), "Please Go to Bus Administrator\n       To Modify Your Bus Line", Toast.LENGTH_SHORT).show();
                            }
                        });

                        viewHolder.txt_anotherchild.setText(model.getLine());
                        viewHolder.txt_anotherchild.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                              /*  Intent go =new Intent(getActivity(),Driver_MapsActivity.class);
                                Bundle b = new Bundle();
                                b.putString("LineName","Hyper One");
                                b.putDouble("lat",30.030093);
                                b.putDouble("lng",31.021370);

                                go.putExtras(b);
                                startActivity(go);*/
                                // Toast.makeText(getActivity(), ""+viewHolder.txt_anotherchild.getText(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Please Go to Bus Administrator\n       To Modify Your Bus Line", Toast.LENGTH_SHORT).show();

                            }
                        });

                        viewHolder.txt_line1.setText(model.getLine1());
                        viewHolder.txt_line1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                              /*  Intent go =new Intent(getActivity(),Driver_MapsActivity.class);
                                Bundle b = new Bundle();
                                b.putString("LineName","Hyper One");
                                b.putDouble("lat",30.030093);
                                b.putDouble("lng",31.021370);

                                go.putExtras(b);
                                startActivity(go);*/
                                // Toast.makeText(getActivity(), ""+viewHolder.txt_anotherchild.getText(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Please Go to Bus Administrator\n       To Modify Your Bus Line", Toast.LENGTH_SHORT).show();

                            }
                        });
                        viewHolder.txt_line2.setText(model.getLine2());
                        viewHolder.txt_line2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                              /*  Intent go =new Intent(getActivity(),Driver_MapsActivity.class);
                                Bundle b = new Bundle();
                                b.putString("LineName","Hyper One");
                                b.putDouble("lat",30.030093);
                                b.putDouble("lng",31.021370);

                                go.putExtras(b);
                                startActivity(go);*/
                                // Toast.makeText(getActivity(), ""+viewHolder.txt_anotherchild.getText(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Please Go to Bus Administrator\n       To Modify Your Bus Line", Toast.LENGTH_SHORT).show();

                            }
                        });

/////////////////////////////////////////////////////////////////////////////////////////////


                        // secondChild
                        // viewHolder.txt_anotherchild.setText(model.getLine());
               /* viewHolder.txt_anotherchild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecycleViewwithFirbase.this, ""+viewHolder.txt_anotherchild.getText(), Toast.LENGTH_SHORT).show();
                    }
                });*/


                        //set Event

                        viewHolder.setItemClickListner(new ItemClickListner() {
                            @Override
                            public void onClick(View view, int position) {
                                viewHolder.expandableLinearLayout.toggle(); // child 1 >> importance
                                viewHolder.linearLayout.toggle();

                                // Toast.makeText(getActivity(), ""+model.getText(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    break;
                    default:
                        break;
                }

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                if (viewType == 0) //without item

                // dy and 8ayrt feha
                {
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_without_child, parent, false);
                    return new ItemViewHolder(itemView, false); // false
                } else  // with item
                {
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_with_child, parent, false);
                    return new ItemViewHolder(itemView, true); // true
                }

            }
        };
        // sparseArray  , all View is expandable
        expandestate.clear();
        for (int i = 0; i < Item.size(); i++)

            expandestate.append(i, false);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

// child 1 >> importance

    private ObjectAnimator ChangRotate(RelativeLayout button, float from, float to) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", from, to);

        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;


    }

    // child 2 >> importance
    private ObjectAnimator ChangRotatee(RelativeLayout button, float fromm, float too) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", fromm, too);

        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;


    }


    private void RetrieveData() {


        Item.clear();
        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot itemsnapshot : dataSnapshot.getChildren()) {
                    item item = itemsnapshot.getValue(item.class);

                    Item.add(item);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("ERROR", "" + databaseError.getMessage());

            }
        });


    }

    @Override
    public void onStart() {

        if (adapter != null)
            adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (adapter != null)
            adapter.stopListening();

        super.onStop();
    }

}

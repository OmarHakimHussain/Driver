package com.example.mybus_driver;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.example.mybus_driver.Common.common;
import com.example.mybus_driver.Model.CustomerLocation;
import com.example.mybus_driver.Model.User;
import com.example.mybus_driver.Remote.IGoogleAPI;
import com.example.mybus_driver.Remote.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map_Home_Fragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Button D_Start_btn,D_end_btn,D_Clist_btn;
    Toolbar toolbar;
    Calendar Sc,Ec;
    String STime,ETime ;




    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    AutocompleteSupportFragment places_fragment;
    private Polyline redPolyline;

    private Marker shipperMarker;
    PlacesClient placesClient;
    private boolean isInit = false;
    private Location previousLocation = null;
    //Animation
    private Handler handler;
    private int index, next;
    private LatLng start, end;
    private float v;
    private double lat, lng;
    private Polyline blackPolyline, grayPolyline;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private List<LatLng> polylineList;
    private IGoogleAPI iGoogleApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Polyline yellowPolyline;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference reference;
    private String userID;




    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map__home_, container, false);

        iGoogleApi = RetrofitClient.getInstance().create(IGoogleAPI.class);

        initPlaces();

        buildLocationRequest();
       buildLocationCallBack();

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "You must enabled this location permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


        mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userID);
        Context context;

      //  mAuth = FirebaseAuth.getInstance();
       // currentUser = mAuth.getCurrentUser();

      Sc=Calendar.getInstance();
      SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
      STime =format.format(Sc.getTime());

        Ec=Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        STime =format.format(Ec.getTime());



        mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment==null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        D_Start_btn=v.findViewById(R.id.D_Start_btn);
        D_end_btn=v.findViewById(R.id.D_end_btn);
        D_Clist_btn=v.findViewById(R.id.D_Clist_btn);

        D_Start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  D_Start_btn.setVisibility(View.GONE);
                D_end_btn.setVisibility(View.VISIBLE);
                D_Clist_btn.setVisibility(View.VISIBLE);*/

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                User user = new User(location.getLatitude() , location.getLongitude());


                                mAuth = FirebaseAuth.getInstance();
                                userID = requireNonNull(mAuth.getCurrentUser()).getUid();
                                reference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userID);

                                FirebaseDatabase.getInstance()
                                        .getReference("currentLocation")
                                        .child(userID)
                                        .setValue(user)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                drawRoutes();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "OMAAAAR" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        D_end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D_end_btn.setVisibility(View.GONE);
                D_Clist_btn.setVisibility(View.GONE);
                D_Start_btn.setVisibility(View.VISIBLE);

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("End_Time", ETime);
                reference.updateChildren(userInfo);
            }
        });

        D_Clist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer_listFragment cl = new Customer_listFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,cl);
                transaction.commit();
                //toolbar = v.findViewById(R.id.toolbar);
                //toolbar.setTitle("Customer Attendance List");

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("Start_Time", STime);
                reference.updateChildren(userInfo);
            }
        });

        return v;

    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);


        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),
                    R.raw.uber_light_with_label));
            if(!success)
                Log.e("EDMTDEV" ,"Style parsing failed");
        }catch (Resources.NotFoundException ex)
        {
            Log.e("EDMTDEV" ,"Resource not found");
        }
    }
    public void call(View view) {
        Intent go = new Intent(Intent.ACTION_DIAL);
        go.setData(Uri.parse("tel:" +"+2"+"01141024562"));
        startActivity(go);
    }

    private void initPlaces() {
        Places.initialize(getContext(),getString(R.string.google_maps_key));
        placesClient = Places.createClient(getContext());

    }
    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);



                // Add a marker in Sydney and move the camera
                LatLng locationShipper = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());

                updateLocation(locationResult.getLastLocation());

                if(shipperMarker == null)
                {
                    // inflate drawable
                    int height,width;
                    height = width = 80;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat
                            .getDrawable(getContext() , R.drawable.shippernew); // Change icon
                    Bitmap resized = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(),width,height,false);

                    shipperMarker =   mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(resized))
                            .position(locationShipper).title("You"));


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationShipper,18));
                }


                if(isInit && previousLocation != null)
                {
                    String from = new StringBuilder()
                            .append(previousLocation.getLatitude())
                            .append(",")
                            .append(previousLocation.getLongitude())
                            .toString();

                    String to = new StringBuilder()
                            .append(locationShipper.latitude)
                            .append(",")
                            .append(locationShipper.longitude)
                            .toString();

                    moveMarkerAnimation(shipperMarker,from,to);

                    previousLocation = locationResult.getLastLocation();
                }

                if(!isInit)
                {
                    isInit = true;
                    previousLocation = locationResult.getLastLocation();
                }

            }
        };
    }
    private void drawRoutes() {



        reference =  FirebaseDatabase.getInstance().getReference("currentLocation");

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {


               CustomerLocation customerLocation = snapshot.getValue(CustomerLocation.class);
               final LatLng location2;
               location2 = new LatLng(customerLocation.getCurrentLatCustomer() , customerLocation.getCurrentLngCustomer());


               mMap.addMarker(new MarkerOptions()
                       .icon(BitmapDescriptorFactory.fromResource(R.drawable.box))

                       .position(new LatLng(location2.latitude,
                               location2.longitude)));

               if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   // TODO: Consider calling
                   //    ActivityCompat#requestPermissions
                   // here to request the missing permissions, and then overriding
                   //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                   //                                          int[] grantResults)
                   // to handle the case where the user grants the permission. See the documentation
                   // for ActivityCompat#requestPermissions for more details.

                   return;
               }



               fusedLocationProviderClient.getLastLocation()
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }).addOnSuccessListener(new OnSuccessListener<Location>() {
                   @Override
                   public void onSuccess(Location location) {

                       String to = new StringBuilder()
                               .append(location2.latitude)
                               .append(",")
                               .append(location2.longitude)
                               .toString();
                       String from = new StringBuilder()
                               .append(location.getLatitude())
                               .append(",")
                               .append(location.getLongitude())
                               .toString();

                       compositeDisposable.add(iGoogleApi.getDirections("driving",
                               "less_driving",
                               from, to,
                               getString(R.string.google_maps_key))
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(new Consumer<String>() {
                                   @Override
                                   public void accept(String s) throws Exception {
                                       try {
                                           JSONObject jsonObject = new JSONObject(s);
                                           JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                           for (int i = 0; i < jsonArray.length(); i++) {
                                               JSONObject route = jsonArray.getJSONObject(i);
                                               JSONObject poly = route.getJSONObject("overview_polyline");
                                               String polyline = poly.getString("points");
                                               polylineList = common.decodePoly(polyline);
                                           }

                                           polylineOptions = new PolylineOptions();
                                           polylineOptions.color(Color.RED);
                                           polylineOptions.width(12);
                                           polylineOptions.startCap(new SquareCap());
                                           polylineOptions.jointType(JointType.ROUND);
                                           polylineOptions.addAll(polylineList);

                                           redPolyline = mMap.addPolyline(polylineOptions);
                                       } catch (Exception e) {
                                           Toast.makeText(getContext(), "This Error here" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               }, new Consumer<Throwable>() {
                                   @Override
                                   public void accept(Throwable throwable) throws Exception {
                                       Toast.makeText(getContext(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               }));
                   }
               });


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


}
        //Add Box







    private void buildLocationRequest() {
        locationRequest =  new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(15000); // 15 sec
        locationRequest.setFastestInterval(10000); // 10 sec
        locationRequest.setSmallestDisplacement(20f); //20 meters
    }
    private void updateLocation(Location lastLocation) {

        User user = new User(lastLocation.getLatitude() , lastLocation.getLongitude());


        mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userID);


        FirebaseDatabase.getInstance()
                .getReference("currentLocation")
                .child(userID)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "DONE", Toast.LENGTH_SHORT).show();
                    }
                });




    }
    private void moveMarkerAnimation(final Marker marker, String from, String to) {
        //Request Request API to get data
        compositeDisposable.add(iGoogleApi.getDirections("driving" ,
                "less_driving",
                from,to,
                getString(R.string.google_maps_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String returnResult) throws Exception {

                        Log.d("API_RETURN" , returnResult);
                        try
                        {
                            //Parse json
                            JSONObject jsonObject = new JSONObject(returnResult);
                            JSONArray jsonArray = jsonObject.getJSONArray("routes");
                            for(int i= 0;i<jsonArray.length();i++)
                            {
                                JSONObject route = jsonArray.getJSONObject(i);
                                JSONObject poly = route.getJSONObject("overview_polyline");
                                String polyline = poly.getString("points");
                                polylineList = common.decodePoly(polyline);
                            }

                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.GRAY);
                            polylineOptions.width(5);
                            polylineOptions.startCap(new SquareCap());
                            polylineOptions.jointType(JointType.ROUND);
                            polylineOptions.addAll(polylineList);
                            grayPolyline = mMap.addPolyline(polylineOptions);

                            blackPolylineOptions =new PolylineOptions();
                            blackPolylineOptions.color(Color.BLACK);
                            blackPolylineOptions.width(5);
                            blackPolylineOptions.startCap(new SquareCap());
                            blackPolylineOptions.jointType(JointType.ROUND);
                            blackPolylineOptions.addAll(polylineList);
                            blackPolyline = mMap.addPolyline(blackPolylineOptions);

                            //Animator
                            ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
                            polyLineAnimator.setDuration(2000);
                            polyLineAnimator.setInterpolator(new LinearInterpolator());
                            polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    List<LatLng> points = grayPolyline.getPoints();
                                    int percentValue = (int)valueAnimator.getAnimatedValue();
                                    int size = points.size();
                                    int newPoints = (int)(size*(percentValue/100.0f));
                                    List<LatLng> p = points.subList(0,newPoints);
                                    blackPolyline.setPoints(p);

                                }
                            });
                            polyLineAnimator.start();

                            //Bike Moving
                            handler = new Handler();
                            index = -1;
                            next = 1;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(index < polylineList.size()-1)
                                    {
                                        index++;
                                        next= index+1;
                                        start = polylineList.get(index);
                                        end = polylineList.get(next);

                                    }

                                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0,1);
                                    valueAnimator.setDuration(1500);
                                    valueAnimator.setInterpolator(new LinearInterpolator());
                                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            v = animation.getAnimatedFraction();
                                            lng = v*end.longitude+(1-v)
                                                    *start.longitude;
                                            lat = v*end.latitude+(1-v)
                                                    *start.latitude;
                                            LatLng newPos =new LatLng(lat,lng);
                                            marker.setPosition(newPos);
                                            marker.setAnchor(0.5f,0.5f);
                                            marker.setRotation(common.getBearing(start,newPos));

                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition())); // Move Camera
                                        }
                                    });

                                    valueAnimator.start();
                                    if(index < polylineList.size() -2) // Reach destination
                                        handler.postDelayed(this,1500);



                                }
                            },1500);

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(throwable != null)
                            Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));

    }


}

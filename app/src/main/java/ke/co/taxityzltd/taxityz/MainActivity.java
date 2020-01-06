package ke.co.taxityzltd.taxityz;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intentfilter.androidpermissions.PermissionManager;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ke.co.taxityzltd.taxityz.POJO.Example;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static java.util.Collections.singleton;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    public GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener listener;

    private Double xx_long;
    private Double yy_lat;
    private Double latitude;
    private Double longitude;
    private Double latitude2, destinationLatitude;
    private Double longitude2, destinationLongitude;
    private String distance, destinationName, pickupName;
    private int TotalPrice;

    private String rideId;
    private String Email;
    private String myPhone;
    private String myName;
    private String paymentMethod;
    private String drop_latitude;
    private String drop_longitude;


    private String y;
    private LatLng origin;
    private LatLng dest;
    private ArrayList<LatLng> MarkerPoints;
    private Polyline line;


    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Button estimateButton, requestButton;
    private ImageButton exitButton;
    private ProgressDialog progressDialog;
    Animation uptodown, downtoup;

    private TextView pickUpview, durationView, destinationview, basePriceView, perKmView, distanceView, totalView;
    private TextView mTextMessage;
    private CardView cardView;
    private EditText pickPoint, destination;
    Spinner spinnerPay;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseUser, databaseCordinates, retrieveRiders;
    private Object obj;

    @Override
    protected void onStart() {
        super.onStart();

        permissions();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 2000, 0, listener);
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
        getPhoneDetails();


        retrieveRiders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//trying to get several markers


                int size = (int) dataSnapshot.getChildrenCount();
                Marker[] allMarkers = new Marker[size];
                mMap.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    RetrieveRides cordinates = new RetrieveRides();

                    for(int i=0;i<=size;i++) {
                        try {
                            cordinates.setEmail(ds.getValue(RetrieveRides.class).getEmail());
                            cordinates.setLatitude(ds.getValue(RetrieveRides.class).getLatitude());
                            cordinates.setLongitude(ds.getValue(RetrieveRides.class).getLongitude());


                            //  Toast.makeText(getApplicationContext(), cordinates.getLatitude() + cordinates.getLongitude(), Toast.LENGTH_LONG).show();


                            //convert string latitude to double
                            Double latitude1 = Double.parseDouble(cordinates.getLatitude());
                            Double longitude1 = Double.parseDouble(cordinates.getLongitude());

                            LatLng buda = new LatLng(latitude1, longitude1);
                            //   mMap.clear();
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            mMap.setTrafficEnabled(true);
                            mMap.setBuildingsEnabled(true);
                            mMap.getUiSettings().setZoomControlsEnabled(true);


                            //lets add updated marker
                            allMarkers[i] = mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)).position(buda));
                        }catch (Exception ex){}
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        permissions();
        checkLocationPermission();

        //get user profile
        getprofile();

        databaseUser= FirebaseDatabase.getInstance().getReference("Taxityz_Users");
        databaseCordinates= FirebaseDatabase.getInstance().getReference("Cordinates");
        retrieveRiders= FirebaseDatabase.getInstance().getReference("Taxis_Available");

         spinnerPay= (Spinner)findViewById(R.id.spinner);

// Initializing markerpoints
        MarkerPoints = new ArrayList<>();

        //reference all the views
        cardView=(CardView)findViewById(R.id.myCard) ;
        estimateButton=(Button)findViewById(R.id.estimateButton);
        requestButton=(Button)findViewById(R.id.requestButton);
        exitButton=(ImageButton)findViewById(R.id.exit);

        durationView=(TextView)findViewById(R.id.durationView);
        pickUpview=(TextView)findViewById(R.id.pickUpview);
        destinationview=(TextView)findViewById(R.id.destinationview);
        basePriceView=(TextView)findViewById(R.id.basePriceView);
        perKmView=(TextView)findViewById(R.id.perKmView);
        distanceView=(TextView)findViewById(R.id.distanceView);
        totalView=(TextView)findViewById(R.id.totalView);

requestButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        paymentMethod = spinnerPay.getSelectedItem().toString();

        postCordinates();

         mMap.clear();

        destination.setText("");
        pickPoint.setText("");
        uptodown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodown);
        cardView.setAnimation(uptodown);
        cardView.setVisibility(View.INVISIBLE);
    }
});
       //display estimates
        estimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(View.VISIBLE);
                downtoup = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downtoup);
                cardView.setAnimation(downtoup);



            }
        });

        //lets get places
        pickPoint=(EditText)findViewById(R.id.pickUp_edit);
        destination=(EditText)findViewById(R.id.destination_edit);


        pickPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  try {

                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("KE")
                            .build();
                    Intent intent =new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)

                                    .setFilter(autocompleteFilter)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                }
*/

                PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
                builder.setAndroidApiKey("YOUR_ANDROID_API_KEY")
                        .setMapsApiKey("YOUR_MAPS_API_KEY");

                // If you want to set a initial location rather then the current device location.
                // NOTE: enable_nearby_search MUST be true.
                // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

                try {
                    Intent placeIntent = builder.build(MainActivity.this);
                    startActivityForResult(placeIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
                catch (Exception ex) {
                    // Google Play services is not available...
                }

            }
        });

        //destination pickicked up point



        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* try {
                    estimateButton.setEnabled(true);
                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)

                            .setCountry("KE")
                            .build();
                    Intent intent =new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)

                            .setFilter(autocompleteFilter)
                            .build(MainActivity.this);
                    startActivityForResult(intent, 200);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                }

*/

                PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
                builder.setAndroidApiKey("YOUR_ANDROID_API_KEY")
                        .setMapsApiKey("YOUR_MAPS_API_KEY")
                ;


                // If you want to set a initial location rather then the current device location.
                // NOTE: enable_nearby_search MUST be true.
                // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

                try {
                    Intent placeIntent = builder.build(MainActivity.this);
                    startActivityForResult(placeIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
                catch (Exception ex) {
                    // Google Play services is not available...
                }
            }
        });

        //exit estimates
    exitButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        uptodown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodown);
        cardView.setAnimation(uptodown);
        cardView.setVisibility(View.INVISIBLE);
    }
});
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView2.getHeaderView(0);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                xx_long = location.getLongitude();
                yy_lat = location.getLatitude();
                String currentDateTimeString = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                }
                y = currentDateTimeString;
                LatLng juja = new LatLng(yy_lat, xx_long);
                CameraPosition position = CameraPosition.builder()
                        .target(juja)
                        .zoom(15)
                        .bearing(0)
                        .tilt(0)
                        .build();

                mMap.setTrafficEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                }

                mMap.setMyLocationEnabled(true);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
                    } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
           // firebaseAuth.signOut();
            //closing activity
          //  finish();
            //starting login activity


        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }


            return false;
        } else

        {
            return true;
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Hide the sliding pane
        // slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        locationManager.requestLocationUpdates("gps", 2000, 0, listener);

        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        // calling multiple co-rdinates
        // retrieving_multiple();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                markers(point);
            }
        });

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MainActivity.this, R.raw.style_json));

            if (!success) {
                Log.e("Map", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map", "Can't find style.", e);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(MainActivity.this, data);

                //lets retrieve the drop-off Point
                destinationName= (String) place.getName();
                pickPoint.setText(destinationName);


                destinationLatitude = place.getLatLng().latitude;
                destinationLongitude = place.getLatLng().longitude;

                LatLng latlangDest = new LatLng(destinationLatitude, destinationLongitude);

                //call markers function
                markers(latlangDest);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(MainActivity.this, data);

                //  Toast.makeText(getApplicationContext(), "Place: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if(requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(MainActivity.this, data);

                //get pick_up poin
                pickupName= (String) place.getName();
                destination.setText(pickupName);

                latitude2 = place.getLatLng().latitude;
                longitude2 = place.getLatLng().longitude;
                LatLng latlangpicked = new LatLng(latitude2, longitude2);

                //call markers function
                markers(latlangpicked);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(MainActivity.this, data);

                Toast.makeText(getApplicationContext(), "Place: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void markers(LatLng point) {
        // clearing map and generating new marker points if user clicks on map more than two times
        if (MarkerPoints.size() > 1) {
            mMap.clear();
            MarkerPoints.clear();
            MarkerPoints = new ArrayList<>();
          //  ShowDistanceDuration.setText("");
        }
        // Adding new item to the ArrayList
        MarkerPoints.add(point);
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(point);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (MarkerPoints.size() == 1) {

            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.begin));

        } else if (MarkerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
        }


        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);
        CameraPosition position = CameraPosition.builder()
                .target(point)
                .zoom(15)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

        // Checks, whether start and end locations are captured
        if (MarkerPoints.size() >= 2) {
            origin = MarkerPoints.get(0);
            dest = MarkerPoints.get(1);
            //lets calculate distances covered
            build_retrofit_and_get_response("driving");
            //   build_retrofit_and_get_response("walking");


        }
    }

    private void build_retrofit_and_get_response(final String type) {
        final Poly x = new Poly();

        try {
            String url = "https://maps.googleapis.com/maps/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitMaps service = retrofit.create(RetrofitMaps.class);

            Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude, dest.latitude + "," + dest.longitude, type);

            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(retrofit.Response<Example> response, Retrofit retrofit) {
                    try {
                        //Remove previous line from map
                        if (line != null) {
                            line.remove();
                        }
                        // This loop will go through all the results and add marker on each location.
                        for (int i = 0; i < response.body().getRoutes().size(); i++) {
                            distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();

                            int distance1 = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getValue();
                            String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                            if (type == "walking") {
                                // ShowDistanceDuration.setText("Walking Distance:" + distance + ", Duration:" + time);
                            } else if (type == "driving") {
                                        /*TextView ShowDistanceDuration1=(TextView)findViewById(R.id.show_distance_time1);
                                        if (distance1<=5) {


                                            TotalPrice = 50;
                                           // ShowDistanceDuration1.setText("Less than 5KM Price = 50\n"+ " Distance:" + distance + " Duration:" + time + " \n Amount per Km=20" + " Total"
                                                //    + TotalPrice + " ");
                                        }
                                        else if(distance1>5)
                                        {
                                            TotalPrice= (distance1 / 1000) * 20;
                                          //  ShowDistanceDuration1.setText("Less than 5KM Price= 50\n"+ " Distance:" + distance + " Duration:" + time + " \n Amount per Km=20"+ " Total"
                                                 //   + TotalPrice+" ");*/
                                TotalPrice= (distance1 / 1000) * 40;

                                pickUpview.setText("Pickup point:"+destinationName);
                                destinationview.setText("Destination:"+pickupName);
                                distanceView.setText("Distance:"+distance);
                                durationView.setText("Duration:"+time);
                                perKmView.setText("Amount per Km:Kshs. 40");
                                totalView.setText("Total:"+TotalPrice);

                            }
                        }
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = x.decodePoly(encodedString);
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(8)
                                .color(Color.BLUE)
                                .geodesic(true)
                        );

                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void getprofile()
    {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        //view objects
        TextView textViewUserEmail;
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView)hView.findViewById(R.id.textViewmail);
        TextView logout=(TextView) hView.findViewById(R.id.textViewlogout);
        //displaying logged in user name
        if (user != null) {
            textViewUserEmail.setText("Welcome "+user.getEmail()+" "+myPhone);
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(MainActivity.this, Login.class));

            }
        });
    }

    private void postCordinates()
    {
                 if (longitude2 != null)
                        {
                            try{
                            FirebaseUser user=firebaseAuth.getInstance().getCurrentUser();

                            rideId=databaseCordinates.push().getKey();

                                PostCordinates postCordinates=new PostCordinates(rideId,longitude2.toString(),
                                        latitude2.toString(),Email,myPhone,TotalPrice,distance,myName,paymentMethod,pickupName,destinationName
                                        ,destinationLatitude.toString().trim(),destinationLongitude.toString().trim());
                                databaseCordinates.child(user.getUid()).setValue(postCordinates);
                                mMap.clear();

                                mDialog("Trip Booked ","Dear "+myName+" Trip to "+pickupName+" from "+destinationName+" has been Booked Succesfully",0);
                               // Toast.makeText(MainActivity.this, "Ride Requested,Kindly wait as we find nearest driver", Toast.LENGTH_LONG).show();

                            }catch(Exception ex)
                        {
                            Toast.makeText(MainActivity.this, "An Error occured,Try Again", Toast.LENGTH_LONG).show();

                        }

                        }
                else if(longitude2==null)
                        {
                        try{

                        FirebaseUser user=firebaseAuth.getInstance().getCurrentUser();

                        rideId=databaseCordinates.push().getKey();


                            PostCordinates postCordinates=new PostCordinates(rideId,longitude2.toString(),
                                    latitude2.toString(),Email,myPhone,TotalPrice,distance,myName,paymentMethod,pickupName,destinationName
                                    ,destinationLatitude.toString().trim(),destinationLongitude.toString().trim());
                            databaseCordinates.child(user.getUid()).setValue(postCordinates);
                        mMap.clear();

                        Toast.makeText(MainActivity.this, "Ride Requested,Kindly wait as we find nearest driver", Toast.LENGTH_LONG).show();

                        }catch(Exception ex)
                            {
                                Toast.makeText(MainActivity.this, "An Error occured,Try Again", Toast.LENGTH_LONG).show();

                            }
                        }
    }
    private void permissions()
    {
        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(singleton(Manifest.permission.ACCESS_FINE_LOCATION), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                //     Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied() {
                //     Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });

        permissionManager.checkPermissions(singleton(Manifest.permission.GET_ACCOUNTS), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                //       Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied() {
                //       Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getPhoneDetails() {
        permissions();

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    obj=ds.getKey();
                    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

                    String s=user.getUid();

                    UserModel userModel=new UserModel();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if(Objects.equals(s.toString(), obj.toString())) {

                            userModel.setUserPhone(ds.getValue(UserModel.class).getUserPhone());
                            userModel.setUserMail(ds.getValue(UserModel.class).getUserMail());
                            userModel.setName(ds.getValue(UserModel.class).getName());

                            myName=userModel.getName();
                            Email=userModel.getUserMail();
                            myPhone=userModel.getUserPhone();
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mDialog(String title, String message,int code){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }
                })
                .build();
        View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        ImageView imageView = (ImageView)view.findViewById(R.id.success);
        if (code != 0){
            imageView.setVisibility(View.GONE);
        }
        messageText.setText(message);
        dialog.show();
    }

}
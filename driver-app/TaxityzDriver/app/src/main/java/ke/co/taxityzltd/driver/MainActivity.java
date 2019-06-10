package ke.co.taxityzltd.driver;


import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intentfilter.androidpermissions.PermissionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ke.co.taxityzltd.driver.POJO.Example;
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
    private ArrayList<LatLng> MarkerPoints;
    private Polyline line;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseUser,databaseCordinates,retrieveRiders;
    private Object obj;

    private String rideId;
    private String Longitude;
    private String Latitude;
    private String Email;
    private String myPhone;
    private int totalPrice;
    private String distance;
    private String myName;
    private String paymentMode;
    private String pickupName;
    private String destinationName;
    private String destinationLatitude;
    private String destinationLongitude;
    private String driverName,driverEmail,driverPhone;

    private String distanceToPickup,durationToPickup;

    private List<RetrieveCordinates> rideList;
    private ListView listViewItems;
    private LatLng origin,dest;

    @Override
    protected void onPause() {
        super.onPause();
        rideList.clear();
    }
    @Override
    protected void onStart() {
        super.onStart();

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

        databaseCordinates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                    RetrieveCordinates cordinates = new RetrieveCordinates();

                    obj=ds1.getKey();

                    cordinates.setRideId(ds1.getValue(RetrieveCordinates.class).getRideId());
                    cordinates.setLatitude(ds1.getValue(RetrieveCordinates.class).getLatitude());
                    cordinates.setLongitude(ds1.getValue(RetrieveCordinates.class).getLongitude());
                    cordinates.setEmail(ds1.getValue(RetrieveCordinates.class).getEmail());
                    cordinates.setMyPhone(ds1.getValue(RetrieveCordinates.class).getMyPhone());
                    cordinates.setDistance(ds1.getValue(RetrieveCordinates.class).getDistance());
                    cordinates.setTotalPrice(ds1.getValue(RetrieveCordinates.class).getTotalPrice());

                    cordinates.setMyName(ds1.getValue(RetrieveCordinates.class).getMyName());
                    cordinates.setPaymentMode(ds1.getValue(RetrieveCordinates.class).getPaymentMode());
                    cordinates.setPickupName(ds1.getValue(RetrieveCordinates.class).getPickupName());
                    cordinates.setDestinationName(ds1.getValue(RetrieveCordinates.class).getDestinationName());

                    cordinates.setDestinationLatitude(ds1.getValue(RetrieveCordinates.class).getDestinationLatitude());
                    cordinates.setDestinationLongitude(ds1.getValue(RetrieveCordinates.class).getDestinationLongitude());


                    rideId=cordinates.getRideId();
                    myName=cordinates.getMyName();
                    totalPrice=cordinates.getTotalPrice();
                    distance=cordinates.getDistance();
                    myPhone=cordinates.getMyPhone();
                    paymentMode=cordinates.getPaymentMode();
                    Latitude=cordinates.getLatitude();
                    Longitude=cordinates.getLongitude();
                    Email=cordinates.getEmail();

                    pickupName=cordinates.getPickupName();
                    destinationName=cordinates.getDestinationName();

                    destinationLatitude=cordinates.getDestinationLatitude();
                    destinationLongitude=cordinates.getDestinationLongitude();
//*Lets get a notification
               //     sendNotification("Ride request", "Distance: "+cordinates.getDistance());

                    //convert string latitude to double
                    Double latitude1 = Double.parseDouble(cordinates.getLatitude());
                    Double longitude1 = Double.parseDouble(cordinates.getLongitude());

                    LatLng clientLatLang = new LatLng(latitude1, longitude1);

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    mMap.setTrafficEnabled(true);
                    mMap.setBuildingsEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    //lets add updated marker
                    mMap.addMarker(new MarkerOptions()
                            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)).position(buda).title("Call Client Phone:No " + cordinates.getEmail()));
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)).position(clientLatLang));
                    rideList.add(cordinates);

                    sendNotification("Taxityz","Customer available");
                }
                Custom_clients_adapter adapter = new Custom_clients_adapter(MainActivity.this, rideList);
                listViewItems.setAdapter(adapter);
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

        databaseUser= FirebaseDatabase.getInstance().getReference("Taxityz_Drivers");
        databaseCordinates= FirebaseDatabase.getInstance().getReference("Cordinates");
        retrieveRiders= FirebaseDatabase.getInstance().getReference("Taxis_Available");

        listViewItems =(ListView)findViewById(R.id.listItems);
        rideList=new ArrayList<>();
// Initializing markerpoints
        MarkerPoints = new ArrayList<>();

        //start sevice
        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        startService(i);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);

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
                String y = currentDateTimeString;
                LatLng myPos = new LatLng(yy_lat, xx_long);
                CameraPosition position = CameraPosition.builder()
                        .target(myPos)
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

        //item list onClick
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RetrieveCordinates retriveCordinatesFromFirebase=rideList.get(position);

                String c_Phone=retriveCordinatesFromFirebase.getMyPhone();

                dialContactPhone(c_Phone);

                deleteFromFirebase(obj.toString());
                mMap.clear();
            }
        });

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            try {
            String url = "https://maps.googleapis.com/maps/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitMaps service = retrofit.create(RetrofitMaps.class);

            Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude, dest.latitude+ "," + dest.longitude, type);

            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(retrofit.Response<Example> response, Retrofit retrofit) {
                    try {
                        // This loop will go through all the results and add marker on each location.
                        for (int i = 0; i < response.body().getRoutes().size(); i++) {

                            int distance1 = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getValue();
                            String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();

                            distanceToPickup= String.valueOf(distance1);
                            durationToPickup=time;

                        }


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
           // finish();
            //starting login activity
           // startActivity(new Intent(this, Login.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView)hView.findViewById(R.id.textViewmail);
        TextView logout=(TextView) hView.findViewById(R.id.textViewlogout);
        //displaying logged in user name
        if (user != null) {
            textViewUserEmail.setText("Welcome "+user.getEmail()+" "+user.getPhoneNumber());
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

                            driverName=userModel.getName();
                            driverEmail=userModel.getUserMail();
                            driverPhone=userModel.getUserPhone();
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

    private void dialContactPhone(final String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));

    }

    private void deleteFromFirebase(String UserId)
    {
        DatabaseReference letsDelete=FirebaseDatabase.getInstance().getReference("Cordinates").child(UserId);
        letsDelete.removeValue();
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        // Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri defaultSoundUri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/"+R.raw.noty);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_favorites)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 , notificationBuilder.build());
    }

}
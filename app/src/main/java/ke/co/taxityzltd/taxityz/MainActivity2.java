package ke.co.taxityzltd.taxityz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.intentfilter.androidpermissions.PermissionManager;
//import com.mikepenz.materialdrawer.DrawerBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.co.taxityzltd.taxityz.POJO.Example;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static java.util.Collections.singleton;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback
        {

            public GoogleMap mMap;
            private LocationManager locationManager;
            private LocationListener listener;

            private Double xx_long;

            private Double yy_lat;
            private Double latitude;
            private Double longitude;
            private Double latitude2;
            private Double longitude2;
            private String distance;
            private int TotalPrice;


            private String y;
            private String pphone;
            private CountDownTimer myCountDownTimer;
            private SlidingUpPanelLayout slidingLayout;
            private LatLng origin;
            private LatLng dest;
            private ArrayList<LatLng> MarkerPoints;
            private TextView ShowDistanceDuration;
            private Polyline line;


            int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
            private Button btnrequest;
            private String email;
            private FirebaseAuth firebaseAuth;
            private DatabaseReference databaseCordinates,retrieveRiders,dataBaseUser;
            public String myPhone,myFname,myLname,myTown;

            private Marker marker;
            private  String paymentMethod;

            private ProgressDialog progressDialog;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    try {

                        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(Place.TYPE_COUNTRY)
                                .setCountry("KE")
                                .build();
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)

                                        .setFilter(autocompleteFilter)
                                        .build(MainActivity2.this);
                        startActivityForResult(intent, 200);

                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                    }
                    return true;
                case R.id.navigation_dashboard:
                   //mTextMessage.setText(R.string.title_dashboard);
                    try {

                        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(Place.TYPE_COUNTRY)
                                .setCountry("KE")
                                .build();
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)

                                        .setFilter(autocompleteFilter)
                                        .build(MainActivity2.this);
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                    }

                    return true;
                case R.id.navigation_notifications:
                   // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

      //  new DrawerBuilder().withActivity(this).build();

        permissions();

        mTextMessage = (TextView) findViewById(R.id.message);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

    private void permissions()
    {
        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(singleton(android.Manifest.permission.ACCESS_FINE_LOCATION), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                //     Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied() {
                //     Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });

        permissionManager.checkPermissions(singleton(Manifest.permission.ACCESS_COARSE_LOCATION), new PermissionManager.PermissionRequestListener() {
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

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                //Hide the sliding pane
                // slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    return;
                }

                locationManager.requestLocationUpdates("gps", 200, 0, listener);

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
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlaceAutocomplete.getPlace(MainActivity2.this, data);

                        latitude = place.getLatLng().latitude;
                        longitude = place.getLatLng().longitude;
                        LatLng buda = new LatLng(latitude, longitude);

                        //call markers function
                        markers(buda);

                        String address = String.valueOf(latitude)+String.valueOf(longitude);
                        // Toast.makeText(getApplicationContext(), "Place: " + address, Toast.LENGTH_LONG).show();


                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                        Status status = PlaceAutocomplete.getStatus(MainActivity2.this, data);

                        //  Toast.makeText(getApplicationContext(), "Place: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                    } else if (resultCode == RESULT_CANCELED) {
                        // The user canceled the operation.
                    }
                }
                else if(requestCode == 200) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlaceAutocomplete.getPlace(MainActivity2.this, data);

                        latitude2 = place.getLatLng().latitude;
                        longitude2 = place.getLatLng().longitude;
                        LatLng buda = new LatLng(latitude2, longitude2);

                        //call markers function
                        markers(buda);

                        //  String address = String.valueOf(latitude)+String.valueOf(longitude);
                        //  Toast.makeText(getApplicationContext(), "Place: " + address, Toast.LENGTH_LONG).show();



                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                        Status status = PlaceAutocomplete.getStatus(MainActivity2.this, data);

                        Toast.makeText(getApplicationContext(), "Place: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                    } else if (resultCode == RESULT_CANCELED) {
                        // The user canceled the operation.
                    }
                }
            }
            public void markers(LatLng point) {
                // clearing map and generating new marker points if user clicks on map more than two times
                if (MarkerPoints.size() > 1) {
                    //  mMap.clear();
                    MarkerPoints.clear();
                    MarkerPoints = new ArrayList<>();
                    ShowDistanceDuration.setText("");
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

                                        AlertDialog.Builder builder;

                                        builder = new android.support.v7.app.AlertDialog.Builder(MainActivity2.this);

                                        builder.setTitle("Total Amount")
                                                .setMessage("Distance "+distance+"\n"+" Time:" + time + " \n")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //lets make the call by passing phone No to function

                                                        Toast.makeText(getApplicationContext(), "Ride Cancelled", Toast.LENGTH_LONG).show();

                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                })
                                                .show();


                                        }
                                    }
                                    String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                                    List<LatLng> list = x.decodePoly(encodedString);
                                    line = mMap.addPolyline(new PolylineOptions()
                                            .addAll(list)
                                            .width(8)
                                            .color(Color.YELLOW)
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

                progressDialog.dismiss();
            }


            public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
            public boolean checkLocationPermission() {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Asking user if explanation is needed
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);


                    }


                    return false;
                } else

                {
                    return true;
                }

            }

            }

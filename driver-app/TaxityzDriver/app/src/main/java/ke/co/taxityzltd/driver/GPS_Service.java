package ke.co.taxityzltd.driver;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GPS_Service extends Service {

    private Double xx, yy;
    private LocationListener listener;
    private LocationManager locationManager;
    private String email = null;
    private String myName,myPhone,myMail;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseCordinates,databaseUser;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //initialise firbase database
        databaseCordinates= FirebaseDatabase.getInstance().getReference("Taxis_Available");
        databaseUser= FirebaseDatabase.getInstance().getReference("Taxityz_Drivers");
        /////////////

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //send data to server
                xx = location.getLongitude();
                yy = location.getLatitude();

                //calling function that will post to Firebase
                //////////////////////////////////////
                postToFirebase(xx,yy);
                ////////////////////////////////////////

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
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);

        getPhoneDetails();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }

    //lets create a method to post coordinates to Firebase database
    public void postToFirebase(Double longi,Double lat)
    {

        String Longitude=longi.toString().trim();
        String Latitude=lat.toString().trim();

        String Email=email;
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

        PostDriverCordinate postCordinatesToFirebase= new PostDriverCordinate(Longitude,Latitude,Email);
        databaseCordinates.child(user.getUid()).setValue(postCordinatesToFirebase);

    //   Toast.makeText(this,"Your new location is "+Longitude+" "+Longitude+" "+Latitude , Toast.LENGTH_LONG).show();

    }

    private void getPhoneDetails() {

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Object obj=ds.getKey();
                    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

                    String s=user.getUid();

                    UserModel userModel=new UserModel();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if(Objects.equals(s.toString(), obj.toString())) {

                            userModel.setUserPhone(ds.getValue(UserModel.class).getUserPhone());
                            userModel.setUserMail(ds.getValue(UserModel.class).getUserMail());
                            userModel.setName(ds.getValue(UserModel.class).getName());

                            myName=userModel.getName();
                            myMail=userModel.getUserMail();
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


}

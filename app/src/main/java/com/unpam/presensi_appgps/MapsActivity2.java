package com.unpam.presensi_appgps;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;

import static com.unpam.presensi_appgps.Constants.POSITION_LATI;
import static com.unpam.presensi_appgps.Constants.POSITION_LONG;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener { //, GoogleMap.OnMapLongClickListener

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private double GEOFENCE_RADIUS = 100;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private TextView tjarak;
    private Location mLastLocation;
    double mylati, mylongi;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );



        tjarak = findViewById ( R.id.tjarak );
        geofencingClient = LocationServices.getGeofencingClient ( this );
        geofenceHelper = new GeofenceHelper ( this );




        if (checkPlayServices ()) {
            buildGoogleApiClient ();
        }

    }


    public static final double Radius = 6371; // In kilometers

    public static double haversine(double lat1, double lon1, double
            lat2, double lon2) {
        double dLat = Math.toRadians ( lat2 - lat1 );
        double dLon = Math.toRadians ( lon2 - lon1 );
        lat1 = Math.toRadians ( lat1 );
        lat2 = Math.toRadians ( lat2 );
        double a = Math.pow ( Math.sin ( dLat / 2 ),2) +
                Math.pow ( Math.sin ( dLon / 2 ), 2 ) * Math.cos ( lat1 ) * Math.cos ( lat2 );
        double c = 2 * Math.asin ( Math.sqrt ( a ) );
        return Radius * c;
    }

    public void refresh(View view) {
        startActivity ( new Intent ( MapsActivity2.this, MapsActivity.class ) );
        overridePendingTransition(R.anim.slide_up,R.anim.slide_up);

//        Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
//        tjarak.startAnimation ( animSlideUp );



    }


    @SuppressLint("MissingPermission")
    private void displayLocation() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation ( mGoogleApiClient );
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude ();
            double longitude = mLastLocation.getLongitude ();
            mylati = latitude;
            mylongi = longitude;
            double jarak = haversine ( latitude, longitude,
                    Constants.POSITION_LATI , Constants.POSITION_LONG ) * 1000;
            tjarak.setText ( "Jarak : " + String.format ( "%.2f", jarak ) + " m" );

        } else {
            tjarak.setText ( "(Couldn't get the location. Make sure GPS is enabled on the device)" );
        }
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location_instansi = new LatLng ( POSITION_LATI, POSITION_LONG );
        mMap.addMarker ( new MarkerOptions ().position ( location_instansi ).title ("PT. Usaha Gas Mandiri (UGM)" ));
        mMap.moveCamera ( CameraUpdateFactory.newLatLngZoom ( location_instansi, 16 ) );
        mMap.addCircle ( new CircleOptions ().center ( location_instansi ).radius ( GEOFENCE_RADIUS )
                .strokeColor ( Color.argb ( 255, 255, 0, 0 ) )
                .fillColor ( Color.argb ( 64, 255, 0, 0 ) )
                .strokeWidth ( 4 )
        );


        enableUserLocation ();

    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled ( true );
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale ( this, Manifest.permission.ACCESS_FINE_LOCATION )) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE );
            } else {
                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE );
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                mMap.setMyLocationEnabled ( true );
            } else {
                //We do not have the permission..

            }
        }

//        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //We have the permission
//                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
//            } else {
//                //We do not have the permission..
//                Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
//            }
//        }
    }



    public void btnNext(View view) {
        startActivity ( new Intent ( MapsActivity2.this, Presence2Activity.class ) );


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if
            (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,MapsActivity2.this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                Toast.makeText(MapsActivity2.this, "This device is not supported.",
                        Toast.LENGTH_LONG).show();
            } return false;
        } return true;
    } @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Smart Presence", "Connection failed: ConnectionResult.getErrorCode() = "+result.getErrorCode());
    }
    @Override
    public void onConnected(Bundle arg0) {
        displayLocation();
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }



}

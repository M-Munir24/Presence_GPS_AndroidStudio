package com.unpam.presensi_appgps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.unpam.presensi_appgps.Constants.BASE_URL;
import static com.unpam.presensi_appgps.Constants.POSITION_LONG;

public class Presence2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Location mLastLocation;
    double mylati, mylongi;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


    private TextView name;
    private TextView username;
    private TextView id;
    private TextView tjarak;
    private TextView time;
    private TextView date;
    private TextView txtin;
    private TextView txtout;
    private TextView txtbtn;
    private TextView title;
    private Button checkin,checkout,about,profil;
    MapsActivity mapsActivity;
    SessionManager sessionManager;
    String getId,getName,getUsername,getRegu,getGrup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_presence );
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();



        name = findViewById(R.id.name);
        title =(TextView) findViewById(R.id.title2);
        username = findViewById(R.id.username);
        tjarak = findViewById(R.id.txtJarak);
        date = findViewById(R.id.date);

        time = findViewById(R.id.time);
        time.setVisibility ( View.INVISIBLE );
        id = findViewById(R.id.id);
        id.setVisibility ( View.INVISIBLE );

        txtin = findViewById(R.id.txtin);
        txtin.setVisibility ( View.INVISIBLE );
        txtout = findViewById(R.id.txtout);
        txtout.setVisibility ( View.INVISIBLE );
        txtbtn = findViewById(R.id.txtbtn);
        txtbtn.setVisibility ( View.VISIBLE );

        checkin = findViewById(R.id.checkin);
        checkin.setVisibility ( View.INVISIBLE );
        checkout = findViewById(R.id.checkout);
        checkout.setVisibility ( View.INVISIBLE );
        about = findViewById(R.id.about);
        profil = findViewById(R.id.profil);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getName = user.get(SessionManager.NAME);
        getUsername = user.get(SessionManager.USERNAME);
        getRegu = user.get(SessionManager.REGU);
        getGrup = user.get(SessionManager.GRUP);
        name.setText(getName);
        username.setText("NIK : "+getUsername);

        Intent intent = getIntent();
        String extreaid = intent.getStringExtra("id_users");
        id.setText(extreaid);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String Time = sdf.format(new Date ());
        time.setText(Time);

        SimpleDateFormat dt = new SimpleDateFormat("E, dd MMM yyyy");
        String datime = dt.format(new Date ());
        date.setText(datime);


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

            if (jarak < 10) {

                checkin.setVisibility ( View.VISIBLE );
                checkout.setVisibility ( View.VISIBLE );
                txtbtn.setVisibility(View.INVISIBLE);
            }

        } else {
            tjarak.setText ( "(Couldn't get the location. Make sure GPS is enabled on the device)" );
        }
    }


    public void checkin(View view) {

        StringRequest postRequest = new StringRequest(Request.Method.POST,BASE_URL + "presenceapp_gps/system/checkin2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new
                                    JSONObject(response).getJSONObject("data");
                            int success = jsonResponse.getInt("success");
                            // String message = jsonResponse.getString("message");
                            // Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                            if (success == 1) {
                                Toast.makeText(Presence2Activity.this, "Success!", Toast.LENGTH_SHORT).show();
                                checkin.setVisibility(View.INVISIBLE);
                                txtin.setVisibility(View.VISIBLE);


//                        String statusCheckIn = jsonResponse.getString("statusCheckIn");
//                        String statusCheckOut = jsonResponse.getString("statusCheckOut");
//                        txtCheckIn.setText(statusCheckIn);
//                        txtCheckOut.setText(statusCheckOut);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", String.valueOf(getName));
                params.put("username", String.valueOf(getUsername));
                params.put("grup", String.valueOf(getGrup));
                params.put("regu", String.valueOf(getRegu));
                params.put("latitude", String.valueOf(mylati));
                params.put("id_users", String.valueOf(getId));
                params.put("longitude", String.valueOf(mylongi));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    public void checkout(View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "presenceapp_gps/system/checkout2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Presence2Activity.this, "Success!", Toast.LENGTH_SHORT).show();
                                checkout.setVisibility(View.INVISIBLE);
                                txtout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Presence2Activity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Presence2Activity.this, "Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_users", String.valueOf(getId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void about(View view) {
        startActivity(new Intent(Presence2Activity.this, AboutActivity.class));



    }

    public void profil(View view) {
        startActivity(new Intent(Presence2Activity.this, ProfilActivity.class));



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
                GooglePlayServicesUtil.getErrorDialog(resultCode,Presence2Activity.this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                Toast.makeText(Presence2Activity.this, "This device is not supported.",
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
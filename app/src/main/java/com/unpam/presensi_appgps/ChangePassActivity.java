package com.unpam.presensi_appgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.unpam.presensi_appgps.Constants.BASE_URL;

public class ChangePassActivity extends AppCompatActivity {
    private EditText username, password, new_password;
    private TextView link_regist;
    private Button btn_login, btn_change;
    private ProgressBar loading;
    private static String URL_LOGIN = "http://192.168.43.8/presenceapp_gps/system/set_password.php";
    private static String URL_CHANGE = "http://192.168.43.8/presenceapp_gps/system/change_password.php";

    SessionManager sessionManager;
    String getId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        this.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        setContentView ( R.layout.activity_change_pass );

        sessionManager = new SessionManager ( this );
        sessionManager.checkLogin ();

        loading = findViewById ( R.id.loading );
        username = findViewById ( R.id.username );
        username.setVisibility ( View.INVISIBLE );
        password = findViewById ( R.id.password );
        new_password = findViewById ( R.id.new_password );
        new_password.setVisibility ( View.INVISIBLE );
        btn_login = findViewById ( R.id.btn_login );
        btn_change = findViewById ( R.id.btn_change );
        btn_change.setVisibility ( View.INVISIBLE );

        //link_regist = findViewById ( R.id.link_regist );



        HashMap<String, String> user = sessionManager.getUserDetail ();
        getId = user.get ( SessionManager.ID );
        String mUsername = user.get ( SessionManager.USERNAME );

        Intent intent = getIntent ();

        String extraUsername = intent.getStringExtra ( "username" );
       // String extranwepass = intent.getStringExtra ( "new_password" );



        username.setText ( extraUsername );
        username.setText ( mUsername);

        btn_change.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                change ();
            }
        } );





        btn_login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String mEmail = username.getText ().toString ().trim ();
                String mPass = password.getText ().toString ().trim ();

                if (!mEmail.isEmpty () || !mPass.isEmpty ()) {
                    Login ( mEmail, mPass );
                    //startActivity(new Intent(LoginActivity.this, MapsActivity.class));

                } else {
                    username.setError ( "Please insert username" );
                    password.setError ( "Please insert password" );
                }
            }
        } );

//        link_regist.setOnClickListener ( new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                startActivity ( new Intent ( ChangePassActivity.this, RegisterActivity.class ) );
//            }
//        } );
    }

    private void Login(final String username, final String password) {
        //loading.setVisibility(View.VISIBLE);
        //btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, URL_LOGIN,
                new Response.Listener<String> () {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject ( response );
                            String success = jsonObject.getString ( "success" );
                            JSONArray jsonArray = jsonObject.getJSONArray ( "login" );

                            if (success.equals ( "1" )) {

                                for (int i = 0; i < jsonArray.length (); i++) {
                                    JSONObject object = jsonArray.getJSONObject ( i );

                                    String name = object.getString ( "name" ).trim ();
                                    String username = object.getString ( "username" ).trim ();
                                    String level = object.getString ( "level" ).trim ();
                                    String grup = object.getString ( "grup" ).trim ();
                                    String regu = object.getString ( "regu" ).trim ();
                                    String gambar = object.getString ( "gambar" ).trim ();
                                    String id_users = object.getString ( "id_users" ).trim ();



                                    sessionManager.createSession ( name, username, id_users,level,grup,regu,gambar );

                                    Toast.makeText ( ChangePassActivity.this,
                                            "Success. \nYour Name : "
                                                    + name + "\nYour NIK : "
                                                    + username, Toast.LENGTH_SHORT )
                                            .show ();

                                    btn_change.setVisibility ( View.VISIBLE );
                                    new_password.setVisibility ( View.VISIBLE );
                                    btn_login.setVisibility ( View.INVISIBLE );



                                    //Intent intent = new Intent (LoginActivity.this, MapsActivity.class);

//                                    Intent intent = new Intent (LoginActivity.this, MapsActivity.class);
//
//                                    loading.setVisibility(View.GONE);
//                                    intent.putExtra("id_users", id_users);
//                                    intent.putExtra("name", name);
//                                    intent.putExtra("email",email);
//                                    intent.putExtra("jabatan",jabatan);
//
////                                      inav.putExtra("name", name);
////                                      inav.putExtra("email",email);
//                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText ( ChangePassActivity.this, "Password salah!", Toast.LENGTH_SHORT ).show ();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace ();
                            //loading.setVisibility(View.GONE);
                            btn_login.setVisibility ( View.VISIBLE );
                            Toast.makeText ( ChangePassActivity.this, "Error" + e.toString (), Toast.LENGTH_SHORT ).show ();
                        }
                    }
                },
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility ( View.GONE );
                        btn_login.setVisibility ( View.VISIBLE );
                        Toast.makeText ( ChangePassActivity.this, "Error" + error.toString (), Toast.LENGTH_SHORT ).show ();

                    }
                } ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<> ();
                params.put ( "username", username );
                params.put ( "password", password );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );

    }

    private void change() {

        final String new_password = this.new_password.getText().toString().trim();
        final String id_users = getId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ChangePassActivity.this, "Change Success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangePassActivity.this, LoginActivity.class));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePassActivity.this, " Error! =" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassActivity.this, "Error! = " + error.toString(), Toast.LENGTH_SHORT).show();
//                        loading.setVisibility(View.GONE);
//                        btn_regist.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_users", getId);

                params.put("new_password", new_password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
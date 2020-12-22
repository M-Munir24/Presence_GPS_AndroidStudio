package com.unpam.presensi_appgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
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

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private TextView link_regist;
    private Button btn_login;
    private ProgressBar loading;
    private static String URL_LOGIN = "http://192.168.43.8/presenceapp_gps/system/login.php";
    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        loading = findViewById(R.id.loading);
        loading.setVisibility ( View.INVISIBLE );
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
       // link_regist = findViewById(R.id.link_regist);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername = username.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if (!mUsername.isEmpty() || !mPass.isEmpty()){
                    Login(mUsername,mPass);
                    //startActivity(new Intent(LoginActivity.this, MapsActivity.class));

                }else {
                    username.setError("Please insert username");
                    password.setError("Please insert password");
                }
            }
        });
//            link_regist.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                }
//            });
    }

    private void Login(final String username, final String password) {
            loading.setVisibility(View.VISIBLE);
            //btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          try {
                              JSONObject jsonObject = new JSONObject(response);
                              String success = jsonObject.getString("success");
                              JSONArray jsonArray = jsonObject.getJSONArray("login");

                              if (success.equals("1")){

                                  for (int i=0; i < jsonArray.length(); i++){
                                      JSONObject object = jsonArray.getJSONObject(i);

                                      String name = object.getString("name").trim();
                                      String username = object.getString("username").trim();
                                      String level = object.getString("level").trim();
                                      String regu = object.getString("regu").trim();
                                      String grup = object.getString("grup").trim();
                                      String gambar = object.getString("gambar").trim();
                                      String id_users = object.getString("id_users").trim();


                                      sessionManager.createSession(name,username,id_users,level,regu,grup,gambar);
                                    if (grup.equals ( "G1" )){

                                        Intent intent = new Intent (LoginActivity.this, MapsActivity.class);

                                        loading.setVisibility(View.GONE);
                                        intent.putExtra("id_users", id_users);
                                        intent.putExtra("name", name);
                                        intent.putExtra("regu", regu);
                                        intent.putExtra("grup", grup);
                                        intent.putExtra("gambar", gambar);
                                        intent.putExtra("level", level);
                                        intent.putExtra("username",username);

//                                      inav.putExtra("name", name);
//                                      inav.putExtra("email",email);
                                        startActivity(intent);
                                        finish ();

                                    } else if (grup.equals ( "G2" )){
                                        Intent intent = new Intent (LoginActivity.this, MapsActivity2.class);

                                        loading.setVisibility(View.GONE);
                                        intent.putExtra("id_users", id_users);
                                        intent.putExtra("name", name);
                                        intent.putExtra("gambar", gambar);
                                        intent.putExtra("regu", regu);
                                        intent.putExtra("grup", grup);
                                        intent.putExtra("level", level);
                                        intent.putExtra("username",username);

//                                      inav.putExtra("name", name);
//                                      inav.putExtra("email",email);
                                        startActivity(intent);
                                        finish ();

                                    }else {
                                        Toast.makeText(LoginActivity.this,"Anda Tidak Mempunyai Grup Absen", Toast.LENGTH_SHORT).show();
                                    }

//                                      Toast.makeText(LoginActivity.this,
//                                              "Success Login. \nYour Name : "
//                                                      +name+"\nYour Email : "
//                                                      +email, Toast.LENGTH_SHORT)
//                                              .show();
                                      //Intent intent = new Intent (LoginActivity.this, MapsActivity.class);

//                                      Intent intent = new Intent (LoginActivity.this, MapsActivity.class);
//
//                                      loading.setVisibility(View.GONE);
//                                      intent.putExtra("id_users", id_users);
//                                      intent.putExtra("name", name);
//                                      intent.putExtra("level", level);
//                                      intent.putExtra("username",username);

//                                      inav.putExtra("name", name);
//                                      inav.putExtra("email",email);
                                     // startActivity(intent);
                                  }
                              }else {
                                  Toast.makeText(LoginActivity.this,"Password Salah!", Toast.LENGTH_SHORT).show();
                                  loading.setVisibility(View.GONE);
                              }
                          } catch (JSONException e) {
                              e.printStackTrace();
                              loading.setVisibility(View.GONE);
                              btn_login.setVisibility(View.VISIBLE);
                              Toast.makeText(LoginActivity.this,"Username Salah!", Toast.LENGTH_SHORT).show(); //+e.toString()
                          }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,"Network Disorders !", Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}

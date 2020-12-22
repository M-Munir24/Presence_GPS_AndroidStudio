package com.unpam.presensi_appgps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.Request.Method.POST;
import static com.unpam.presensi_appgps.Constants.BASE_URL;

public class ProfilActivity extends AppCompatActivity {

    private static final String TAG = EditActivity.class.getSimpleName(); //get info
    private TextView name,username,level,grup,regu;
    private Button btn_logout, btn_photo_upload, cengpass;
    SessionManager sessionManager;
    String getId;
    private static String URL_READ = "http://192.168.43.8/presenceapp_gps/system/read_detail.php";
    private static String URL_EDIT = "http://192.168.43.8/presenceapp_gps/system/edit_detail.php";
    private static String URL_UPLOAD = "http://192.168.43.8/presenceapp_gps/system/upload.php";
    private static String URL_Gambar = "http://192.168.43.8/presenceapp_gps/admin/img/";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil );

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        name = findViewById(R.id.name);
        grup = findViewById(R.id.grup);
        regu = findViewById(R.id.regu);
        username = findViewById(R.id.username);
        level = findViewById(R.id.level);
        btn_logout = findViewById(R.id.btn_logout);
        cengpass = findViewById(R.id.cengpass);
        profile_image = findViewById(R.id.profile_image);


        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        String mName = user.get(SessionManager.NAME);
        String mUsername = user.get(SessionManager.USERNAME);
        String mgrup = user.get(SessionManager.GRUP);
        String mregu = user.get(SessionManager.REGU);
        String mLevel = user.get(SessionManager.LEVEL);
        String mGambar = user.get(SessionManager.GAMBAR);

        Intent intent = getIntent();
        String extreaName = intent.getStringExtra("name");
        String extreaLevel = intent.getStringExtra("level");
        String extreaRegu = intent.getStringExtra("regu");
        String extraUsername = intent.getStringExtra("email");
        String extraGambar = intent.getStringExtra("gambar");

        name.setText("Nama : "+extreaName);
        username.setText("NIK : "+extraUsername);
        level.setText("Regu : "+extreaRegu);
        level.setText("Sebagai : "+extreaLevel);

        //String uri ="http://192.168.43.8/presenceapp_gps/admin/img/" ;
//        profile_image.setImageURI ( Uri.parse ( uri+extraGambar ) );

//        Uri imgUri=Uri.parse("http://192.168.43.8/presenceapp_gps/admin/img/");
//        profile_image.setImageURI(imgUri);

//        Uri path = Uri.parse("http://192.168.43.8/presenceapp_gps/admin/img/" +mGambar);
//        String imgPath = path.toString();
//        //ivPerfil = (ImageView)findViewById(R.id.ivPerfil);
//        profile_image.setImageURI(path);

        name.setText("Nama : "+mName);
        username.setText("NIK : "+mUsername);
        level.setText("Sebagai : "+mLevel);
        grup.setText("Grup : "+mgrup);
        regu.setText("Regu : "+mregu);

        //profile_image.setImageBitmap ();

        RequestOptions requestOptions = new RequestOptions ();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy( DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.pp);
        requestOptions.error(R.drawable.pp);

        Glide.with(ProfilActivity.this)
                .load("http://192.168.43.8/presenceapp_gps/admin/img/"+mGambar)
                .apply(requestOptions)
                .into(profile_image);




    }


    public void logout(View view) {
        startActivity(new Intent(ProfilActivity.this, LoginActivity.class));
        finish ();


    }

    public void cengpass(View view) {
        startActivity(new Intent(ProfilActivity.this, ChangePassActivity.class));


    }
}

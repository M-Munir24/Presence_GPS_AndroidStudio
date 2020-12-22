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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = EditActivity.class.getSimpleName(); //get info
    private TextView name,username,jabatan,regu;
    private Button btn_logout, btn_photo_upload;
    SessionManager sessionManager;
    String getId;
    private static String URL_READ = "http://192.168.43.8/presenceapp_gps/system/read_detail.php";
    private static String URL_EDIT = "http://192.168.43.8/presenceapp_gps/system/edit_detail.php";
    private static String URL_UPLOAD = "http://192.168.43.8/presenceapp_gps/system/upload.php";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit );

//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        name = findViewById(R.id.name);
//        username = findViewById(R.id.username);
//        jabatan = findViewById(R.id.jabatan);
//        btn_logout = findViewById(R.id.btn_logout);
//        btn_photo_upload = findViewById(R.id.btn_photo);
//        gambar = findViewById(R.id.profile_image);
//
//        btn_photo_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseFile();
//
//            }
//        });
//
//        HashMap<String, String> user = sessionManager.getUserDetail();
//        getId = user.get(SessionManager.ID);
////        String mEmail = user.get(SessionManager.EMAIL);
//
////        Intent intent = getIntent();
////        String extreaName = intent.getStringExtra("name");
////        String extraEmail = intent.getStringExtra("email");
//
////        name.setText(extreaName);
////        email.setText(extraEmail);
////        name.setText(mName);
////        email.setText(mEmail);
//
//          btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sessionManager.logout();
//            }
//        });
//
//
//    }
//    //getUserDetail
//    private void getUserDetail(){
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        Log.i(TAG,response.toString());
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("read");
//
//                            if (success.equals("1")){
//                                for (int i=0; i<jsonArray.length(); i++){
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//                                    String strName = object.getString("name").trim();
//                                    String strEmail = object.getString("email").trim();
//
//                                    name.setText(strName);
//                                    username.setText(strEmail);
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                            Toast.makeText( EditActivity.this,"Error Reading Detail"+e.toString(), Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText( EditActivity.this,"Error Reading Detail"+error, Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id_users", getId);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getUserDetail();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_action, menu);
//
//        action = menu;
//        action.findItem(R.id.menu_save).setVisible(false);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//         switch (item.getItemId()){
//             case R.id.menu_edit:
//                 name.setFocusableInTouchMode(true);
//                 username.setFocusableInTouchMode(true);
//
//                 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                 imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
//
//                 action.findItem(R.id.menu_edit).setVisible(false);
//                 action.findItem(R.id.menu_save).setVisible(true);
//
//                 return true;
//
//             case R.id.menu_save:
//                 SaveEditDetail();
//
//                 action.findItem(R.id.menu_edit).setVisible(true);
//                 action.findItem(R.id.menu_save).setVisible(false);
//
//                 name.setFocusableInTouchMode(false);
//                 username.setFocusableInTouchMode(false);
//                 username.setFocusableInTouchMode(false);
//                 name.setFocusable(false);
//                 username.setFocusable(false);
//                 return true;
//
//             default:
//                 return super.onOptionsItemSelected(item);
//         }
//
//    }
////save
//    private void SaveEditDetail() {
//        final String name = this.name.getText().toString().trim();
//        final String username = this.username.getText().toString().trim();
//        final String jabatan = this.jabatan.getText().toString().trim();
//        final String gambar = this.gambar.toString ().trim();
//        final String id_users = getId;
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Saving...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            if (success.equals("1")){
//                                Toast.makeText( EditActivity.this, "Success!",Toast.LENGTH_SHORT).show();
//                                sessionManager.createSession(name,username,id_users,jabatan);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                            Toast.makeText( EditActivity.this, "Error!"+e.toString(),Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText( EditActivity.this, "Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String>params = new HashMap<>();
//                params.put("name",name);
//                params.put("nik",username);
//                params.put("id_users",id_users);
//                params.put("jabatan",jabatan);
//
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//
//    }
//
//    private void chooseFile(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                gambar.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            UploadPicture(getId, getStringImage(bitmap));
//        }
//    }
//
//    private void UploadPicture(final String id_users, final String gambar) {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Uploading...");
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                    Log.i(TAG, response.toString());
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//
//                            if (success.equals("1")){
//                                Toast.makeText( EditActivity.this,"Success!", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                            Toast.makeText( EditActivity.this,"Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText( EditActivity.this,"Try Again!"+error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String>params = new HashMap<>();
//                params.put("id_users",id_users);
//                params.put("gambar",gambar);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//    public String getStringImage(Bitmap bitmap){
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//
//        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
//        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
//
//        return encodeImage;
   }
}

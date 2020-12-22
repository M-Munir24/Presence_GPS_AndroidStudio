package com.unpam.presensi_appgps;



public class Constants  {


    public static final String BASE_URL = "http://192.168.43.8/";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String UNIQUE_ID = "unique_id";
    public static final double POSITION_LATI = -6.333661;
    public static final double POSITION_LONG = 106.749751;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate ( savedInstanceState );
//
//
//        requestQueue = Volley.newRequestQueue ( Constants.this );
//
//        list_data = new ArrayList<HashMap<String, String>> ();
//
//        stringRequest = new StringRequest ( Request.Method.GET, url, new Response.Listener<String> () {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject ( response );
//                    JSONArray jsonArray = jsonObject.getJSONArray ( "location" );
//                    for (int a = 0; a < jsonArray.length (); a++) {
//                        JSONObject json = jsonArray.getJSONObject ( a );
//
//
//
//                        HashMap<String, String> map = new HashMap<String, String> ();
//                        map.put ( "id_location", json.getString ( "id_location" ) );
//                        map.put ( "name_map", json.getString ( "name_map" ) );
//                        map.put ( "latitude", json.getString ( "latitude" ) );
//                        map.put ( "longitude", json.getString ( "longitude" ) );
//
//
//                        list_data.add ( map );
//                    }
//
//                    title.setText ( list_data.get ( 0 ).get ( "name_map" ) );
//
//                } catch (JSONException e) {
//                    e.printStackTrace ();
//                }
//            }
//        }, new Response.ErrorListener () {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText ( Constants.this, error.getMessage (), Toast.LENGTH_LONG ).show ();
//            }
//        } );
//
//        requestQueue.add ( stringRequest );
//
//    }
}

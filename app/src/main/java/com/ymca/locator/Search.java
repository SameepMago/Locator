package com.ymca.locator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class Search extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    final Context context = this;
    //StringRequest jsonArrayRequest ;
    //RequestQueue requestQueue ;
    MarkerOptions a;
    SupportMapFragment mapFragment;
    Marker regbus;
    Geocoder geocoder;
    List<Address> addresses;
    StringBuilder str;
    private DatabaseReference mDatabase;
    private double clatitude,clongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchbus);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.update);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                //JSON_DATA_WEB_CALL_NEW(sharedPreferences.getString("searchbusnum",""));
                showbuslocation();
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.searchmap);
        mapFragment.getMapAsync(this);

    }
    /*public void JSON_DATA_WEB_CALL_NEW(final String busnumber){
        jsonArrayRequest = new StringRequest(Request.Method.POST,GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse( String response) {
                        if(response.trim().equals("0 results")){

                        }
                        else {
                            JSONArray a = null;
                            try {
                                a = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSON_PARSE_DATA_AFTER_WEBCALL_NEW(a);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Search.this, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("busnum",busnumber);
                return params;
            }

        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }

        requestQueue.add(jsonArrayRequest);
    }
    public void JSON_PARSE_DATA_AFTER_WEBCALL_NEW(JSONArray a){


        for(int i = a.length()-1; i>=0 ;i--) {
            JSONObject json = null;
            try {
                json =a.getJSONObject(i);
                LatLng sydney = new LatLng(json.getDouble("currentlatitude"),json.getDouble("currentlongitude"));
                try {
                    geocoder = new Geocoder(Search.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(json.getDouble("currentlatitude"),json.getDouble("currentlongitude"), 1);
                    str = new StringBuilder();
                    if (geocoder.isPresent()) {
                        Address returnAddress = addresses.get(0);

                        String localityString = returnAddress.getLocality();
                        String city = returnAddress.getAddressLine(0);
                        String region_code = returnAddress.getCountryCode();
                        String zipcode = returnAddress.getPostalCode();

                        str.append(city+ ",");
                        str.append(localityString+ "," + region_code + ",");
                        str.append(zipcode + "");

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "geocoder not present", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {

                    Log.e("tag", e.getMessage());
                }
                regbus.setPosition(sydney);
                regbus.setSnippet("\n"+"Address:"+"\t"+str.toString());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                regbus.showInfoWindow();



            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }*/
    Runnable runnable = new Runnable(){

        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
            //JSON_DATA_WEB_CALL_NEW(sharedPreferences.getString("searchbusnum",""));
            showbuslocation();
            searchHandler.postDelayed(this, 2000); // determines the execution interval
        }
    };
    Handler searchHandler=new Handler();
    @Override
    public void onResume(){
        super.onResume();
        searchHandler.postDelayed(runnable,2000);
    }
    @Override
    public void onPause(){
        super.onPause();
        searchHandler.removeCallbacks(runnable);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        LatLng sydney=new LatLng(30.3150, 76.9348);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);
                info.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                TextView title = new TextView(context);
                title.setTextColor(Color.WHITE);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.WHITE);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        a=new MarkerOptions().position(sydney).title("Bus Number:"+sharedPreferences.getString("searchbusnum", "")).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        regbus=mMap.addMarker(a);
    }

    public void showbuslocation(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Busnum");
        mDatabase.keepSynced(true);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        mDatabase.child(sharedPreferences.getString("searchbusnum","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clatitude=Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                System.out.println("Latitude........"+clatitude);
                clongitude=Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                System.out.println("Longitude......."+clongitude);

                LatLng sydney = new LatLng(clatitude,clongitude);
                try {
                    geocoder = new Geocoder(Search.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(clatitude,clongitude, 1);
                    str = new StringBuilder();
                    if (geocoder.isPresent()) {
                        Address returnAddress = addresses.get(0);

                        String localityString = returnAddress.getLocality();
                        String city = returnAddress.getAddressLine(0);
                        String region_code = returnAddress.getCountryCode();
                        String zipcode = returnAddress.getPostalCode();

                        str.append(city + ",");
                        str.append(localityString+ "," + region_code + ",");
                        str.append(zipcode + "");

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "geocoder not present", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {

                    Log.e("tag", e.getMessage());
                }
                regbus.setPosition(sydney);
                regbus.setSnippet("\n"+"Address:"+"\t"+str.toString());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                regbus.showInfoWindow();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Locator", "Failed to read value.", error.toException());
            }
        });


    }
}

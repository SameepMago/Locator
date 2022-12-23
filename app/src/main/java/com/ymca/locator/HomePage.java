package com.ymca.locator;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomePage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    final Context context = this;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private GoogleMap mMap;
    TextView email;
    TextView busstop;

    private BottomBar mBottomBar;
    String busnum="null";
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
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        try {
            searchForUpdates();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbuslocation();
            }
        });
        if (getIntent().getBooleanExtra("IS_FROM_NOTIFICATION", false)) {
            Intent i = new Intent(HomePage.this, Notifications.class);
            startActivity(i);
        }
        if (getIntent().getBooleanExtra("IS_FROM_LOGIN", false)) {
            sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder
                    .setTitle("Locator")
                    .setIcon(R.drawable.main)
                    .setMessage("Welcome Back"+"\t"+sharedPreferences.getString("Name","")+"\n"+"Track your bus using Locator. Thank you for using our services."+"\n"+"For more details contact:"+"sameepmago@gmail.com")
                    .setCancelable(true);

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
        mBottomBar=(BottomBar)findViewById(R.id.bottomBar);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.bottomBarItemSecond:
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.editprofilebusnum, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        alertDialogBuilder.setView(promptsView);

                        final Spinner userInput = (Spinner) promptsView
                                .findViewById(R.id.newbusnum);
                        List<String> list = new ArrayList<String>();
                        list.add("Select Bus Number");
                        list.add("7620: Sector 9,Sector 10 to College");
                        list.add("4297: Baldev Nagar to College");
                        list.add("1185: Defence Colony to College");
                        list.add("1195: Kharga Canteen,Babyal,Boh to College");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (HomePage.this, R.layout.spinner_item, list);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        userInput.setAdapter(dataAdapter);

                        userInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {
                                switch (position){
                                    case 0:
                                        busnum="0";
                                        break;
                                    case 1:
                                        busnum="7620";
                                        break;
                                    case 2:
                                        busnum="4297";
                                        break;
                                    case 3:
                                        busnum="1185";
                                        break;
                                    case 4:
                                        busnum="1195";
                                        break;

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub
                            }
                        });


                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("Search",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                if (busnum == "0" || busnum == "null") {
                                                    Toast.makeText(HomePage.this, "Select a valid bus number", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("searchbusnum",busnum);
                                                    editor.apply();
                                                    Intent i=new Intent(HomePage.this,Search.class);
                                                    startActivity(i);
                                                    mBottomBar.setDefaultTabPosition(0);
                                                }
                                            }
                                        })
                                .setNegativeButton("Cancel",

                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                mBottomBar.setDefaultTabPosition(0);
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                        break;
                    case R.id.bottomBarItemOne:
                        busnum="0";
                        showbuslocation();
                        break;
                    case R.id.bottomBarItemThird:
                        if(noLocation()){
                            noLocation();
                        }
                        else{
                            Intent i=new Intent(HomePage.this, CurrentLocation.class);
                            startActivity(i);
                            mBottomBar.setDefaultTabPosition(0);
                        }

                        break;

                }
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        email = (TextView) hView.findViewById(R.id.email);
        busstop = (TextView) hView.findViewById(R.id.busstop);
        FloatingActionButton profile = (FloatingActionButton) hView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomePage.this,Profile.class);
                startActivity(i);
            }
        });
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        email.setText(sharedPreferences.getString("Email", ""));
        busstop.setText(sharedPreferences.getString("Busstop", ""));

    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else  if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.about){
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.about, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder
                    .setCancelable(true);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
        if (id==R.id.profile){
            Intent i=new Intent(HomePage.this,Profile.class);
            startActivity(i);
        }
        if (id == R.id.subscribe) {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.subscribe, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.secondbusnum);


            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Register",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    final String secbusnum=userInput.getText().toString();
                                    if(userInput.getText().toString().isEmpty()){
                                        Toast.makeText(HomePage.this, "Bus number can't be empty", Toast.LENGTH_SHORT).show();
                                    }else if(userInput.getText().toString().equals(sharedPreferences.getString("Busnum",""))){
                                        Toast.makeText(HomePage.this, "You are already registered for this bus", Toast.LENGTH_SHORT).show();
                                    }
                                    else {


                                        if(sharedPreferences.getInt("counter", Constants.counter)<1) {
                                            editor.putInt("counter", Constants.counter++);
                                          //  FirebaseMessaging.getInstance().subscribeToTopic(secbusnum);

                                        }
                                        else{
                                            new AlertDialog.Builder(HomePage.this).setMessage("You are currently subscribed to two buses. You can't get subscribed to more than two buses")
                                                    .setPositiveButton("Unsubscribe",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog,int id) {
                                                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    Constants.counter=Constants.counter-1;
                                                                    editor.putInt("counter", Constants.counter);
                                                                   // FirebaseMessaging.getInstance().unsubscribeFromTopic(secbusnum);
                                                                }
                                                            })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }

                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        if (id == R.id.nav_route) {
            i=new Intent(HomePage.this,BusRoute.class);
            startActivity(i);
        } else if (id == R.id.nav_fees) {
            i=new Intent(HomePage.this,Fees.class);
            startActivity(i);

        } else if (id == R.id.nav_driver) {
            i=new Intent(HomePage.this,DriverDetails.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Locator App from play store");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download Locator app for free"+"\t");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this).setMessage("You will not receive the notifications from registered bus Are you sure you want to Log Out")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which){
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);

                            //Opening the shared preferences editor to save values
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Saving the boolean as true i.e. the device is registered
                            editor.putBoolean(Constants.REGISTERED, false);
                            //Applying the changes on sharedpreferences
                            editor.apply();
                            //FirebaseMessaging.getInstance().unsubscribeFromTopic(sharedPreferences.getString("Busnum", ""));
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            mAuth.signOut();
                            Intent i=new Intent(HomePage.this,Login.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();

        }else if(id==R.id.nav_rate){
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.rating, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            final RatingBar userInput = (RatingBar) promptsView
                    .findViewById(R.id.ratingBar);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Toast.makeText(HomePage.this,"You have given"+"\t"+
                                                    String.valueOf(userInput.getRating())+"\tratings. Thanks for support",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }else if(id==R.id.nav_about){
            i=new Intent(HomePage.this,AboutUs.class);
            startActivity(i);
        }else if(id==R.id.nav_contact){
            i=new Intent(HomePage.this,ContactUs.class);
            startActivity(i);
        } else if (id==R.id.complaint){
            i=new Intent(HomePage.this,Feedback.class);
            startActivity(i);
        }else if(id==R.id.download){
            i=new Intent(HomePage.this,Download.class);
            startActivity(i);
        }else if(id==R.id.updates){
            i=new Intent(HomePage.this,MainActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
        a=new MarkerOptions().position(sydney).title("Bus Number:"+sharedPreferences.getString("Busnum", "")).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        regbus=mMap.addMarker(a);
    }
    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();

            enableLoc();
            return true;
        }
        return false;

    }

    public void showbuslocation(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Busnum");
        mDatabase.keepSynced(true);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        mDatabase.child(sharedPreferences.getString("Busnum","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clatitude=Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                System.out.println("Latitude........"+clatitude);
                clongitude=Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                System.out.println("Longitude......."+clongitude);

                LatLng sydney = new LatLng(clatitude,clongitude);
                try {
                    geocoder = new Geocoder(HomePage.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(clatitude,clongitude, 1);
                    str = new StringBuilder();
                    if (geocoder.isPresent()) {
                        Address returnAddress = addresses.get(0);

                        String localityString = returnAddress.getLocality();
                        String city = returnAddress.getAddressLine(0);
                        Log.i("address",city);
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


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) context, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });

        }
    }


    private void searchForUpdates() throws PackageManager.NameNotFoundException {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference("appDetails");
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("versionName").getValue().toString().equals("1.4")) {
                    showUpdateDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showUpdateDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ace.vishal.locator"));
                                startActivity(browserIntent);
                                finish();
                            }
                        }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
        dialog.show();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    default: {
                        Intent i = new Intent(HomePage.this, CurrentLocation.class);
                        startActivity(i);
                        mBottomBar.setDefaultTabPosition(0);
                    }
                }
                break;
        }

    }
    Runnable runnable = new Runnable(){

        public void run() {
            showbuslocation();
            regHandler.postDelayed(this, 2000); // determines the execution interval
        }
    };
    Handler regHandler=new Handler();
    @Override
    public void onResume(){
        super.onResume();
        regHandler.postDelayed(runnable,2000);
    }
    @Override
    public void onPause(){
        super.onPause();
        regHandler.removeCallbacks(runnable);
    }

}
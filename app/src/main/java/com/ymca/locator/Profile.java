package com.ymca.locator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    private Resources mResources;
    private ImageView mImageView;
    private Bitmap mBitmap;
    final Context context = this;
    TextView name,email,busnumber,busstop;
    public String busnum;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mResources = getResources();
        mImageView = (ImageView) findViewById(R.id.imageview_rounded_image);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        busnumber=(TextView)findViewById(R.id.busnumber);
        busstop=(TextView)findViewById(R.id.busstop);
        busnumber.setOnClickListener(this::onClick);
        busstop.setOnClickListener(this::onClick);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        name.setText(sharedPreferences.getString("Name",""));
        email.setText(sharedPreferences.getString("Email",""));
        busnumber.setText(sharedPreferences.getString("Busnum",""));
        busstop.setText(sharedPreferences.getString("Busstop",""));
        //rounded image drawable
        mBitmap = BitmapFactory.decodeResource(mResources,R.drawable.profile);

        mImageView.setImageBitmap(mBitmap);
        mImageView.setImageBitmap(mBitmap);
        RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(mBitmap);
        mImageView.setImageDrawable(roundedImageDrawable);
    }

    private RoundedBitmapDrawable createRoundedBitmapImageDrawableWithBorder(Bitmap bitmap){
        int bitmapWidthImage = bitmap.getWidth();
        int bitmapHeightImage = bitmap.getHeight();
        int borderWidthHalfImage = 2;

        int bitmapRadiusImage = Math.min(bitmapWidthImage,bitmapHeightImage)/2;
        int bitmapSquareWidthImage = Math.min(bitmapWidthImage,bitmapHeightImage);
        int newBitmapSquareWidthImage = bitmapSquareWidthImage+borderWidthHalfImage;

        Bitmap roundedImageBitmap = Bitmap.createBitmap(newBitmapSquareWidthImage,newBitmapSquareWidthImage,Bitmap.Config.ARGB_8888);
        Canvas mcanvas = new Canvas(roundedImageBitmap);
        mcanvas.drawColor(Color.GRAY);
        int i = borderWidthHalfImage + bitmapSquareWidthImage - bitmapWidthImage;
        int j = borderWidthHalfImage + bitmapSquareWidthImage - bitmapHeightImage;

        mcanvas.drawBitmap(bitmap, i, j, null);

        Paint borderImagePaint = new Paint();
        borderImagePaint.setStyle(Paint.Style.STROKE);
        borderImagePaint.setStrokeWidth(borderWidthHalfImage*2);
        borderImagePaint.setColor(Color.GRAY);
        mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources,roundedImageBitmap);
        roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }
    public void onClick(View v){
        if(v==busstop){
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.editprofilebusstop, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.newbusstop);


            // set dialog message
            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    final String newbusstop=userInput.getText().toString();
                                    if(userInput.getText().toString().isEmpty()){
                                        Toast.makeText( Profile.this, "Bus stop can't be empty", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        editor.putString("Busstop",newbusstop);
                                        busstop.setText(newbusstop);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        mDatabase.child("users").child(user.getUid()).child("busstop").setValue(newbusstop);
                                        editor.apply();
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
        }
        if(v==busnumber){
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
                    (this, R.layout.spinner_item, list);

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
                            busnum="7622";
                            break;
                        case 3:
                            busnum="4297";
                            break;
                        case 4:
                            busnum ="0516";
                            break;
                        case 5:
                            busnum="7619";
                            break;
                        case 6:
                            busnum="1185";
                            break;
                        case 7:
                            busnum="1186";
                            break;
                        case 8:
                            busnum="1195";
                            break;
                        case 9:
                            busnum="4298";
                            break;
                        case 10:
                            busnum="5116";
                            break;
                        case 11:
                            busnum="5117";
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
                    .setPositiveButton("Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if(busnum=="0"){
                                        Toast.makeText( Profile.this, "Select a valid bus number", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(sharedPreferences.getString("Busnum",""));
                                        editor.putString("Busnum",busnum);
                                        FirebaseMessaging.getInstance().subscribeToTopic(busnum);
                                        busnumber.setText(busnum);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        mDatabase.child("users").child(user.getUid()).child("busnum").setValue(busnum);
                                        editor.apply();
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
        }
    }
}

package com.ymca.locator;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    EditText editTextrollnum;
    EditText editTextname;
    EditText editTextcomplaint;
    ProgressDialog progressDialog;
    ImageView viewImage;
    Button b;
    String picturePath;
    Button submit;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private int CAMERA_PERMISSION_CODE = 23;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        if(!isCameraAllowed()){
            //If the app has not the permission then asking for the permission
            requestStoragePermission();
        }
        editTextrollnum=(EditText)findViewById(R.id.editTextRoll);
        editTextname=(EditText)findViewById(R.id.editTextName);
        editTextcomplaint=(EditText)findViewById(R.id.editTextComplaint);
        b=(Button)findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextrollnum.getText().toString().isEmpty()||editTextname.getText().toString().isEmpty()||editTextcomplaint.getResources().toString().isEmpty()){
                    Toast.makeText(Feedback.this, "All fields are compoulsary", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendToServer();
                }
            }
        });
    }
    private void sendToServer(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        //getting the email entered
        final String rollno = editTextrollnum.getText().toString().trim();
        final String name=editTextname.getText().toString().trim();
        final String complaint=editTextcomplaint.getText().toString().trim();
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "sameepmago@gmail.com","magosameep@gmail.com" });
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Complaint"+"\t"+"From:"+"\n"+"Roll no."+rollno+"\n"+"Name:"+name);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,complaint);
        emailIntent.setType("image/jpeg");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + picturePath));
        startActivity(Intent.createChooser(emailIntent, "Send your email in:"));


        FirebaseUser user = mAuth.getCurrentUser();
        writeNewComplaint(user.getUid(),rollno,name,complaint);
        /*  //Creating a string request
        StringRequest req = new StringRequest(Request.Method.POST, Constants.COMPLAINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //if the server returned the string success
                        if (response.trim().equalsIgnoreCase("Records added successfully.")) {
                            //Displaying a success toast
                            Toast.makeText(Feedback.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Feedback.this,"Network Problem",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                params.put("rollnum", rollno);
                params.put("name", name);
                params.put("complaint",complaint);
                return params;
            }
        };

        //Adding the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);   */
    }
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);
        builder.setTitle("Upload a photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        picturePath=f.getAbsolutePath();
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    OutputStream outFile = null;
                    File  file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");


                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        //picturePath=file.getAbsolutePath();
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                viewImage.setImageBitmap(thumbnail);
            }
        }
    }

    private boolean isCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void writeNewComplaint(String userId,String userName,String rollNum,String complaint) {
        Complaint complaints = new Complaint(rollNum,userName,complaint);
        Date date = new Date();
        mDatabase.child("complaints").child(userId).child(String.valueOf(date.getTime())).setValue(complaints);
        //dismissing the progress dialog
        progressDialog.dismiss();
        //Displaying a success toast
        Toast.makeText(Feedback.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
        editTextrollnum.getText().clear();
        editTextcomplaint.getText().clear();
        editTextname.getText().clear();

    }

    //This method will be called when the user will tap on allow or deny
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        // PERMISSION GRANTED
                    } else {
                        // PERMISSION NOT GRANTED
                    }
                }
            }
    );

}
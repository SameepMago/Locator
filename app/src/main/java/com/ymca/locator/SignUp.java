package com.ymca.locator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends BaseActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    final Context context = this;
    private static final String TAG = "EmailPassword";
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editBusStop;
    private EditText editTextName;
    public String busnum;
    private Button SignUp;
    private TextView signin;
    private DatabaseReference mDatabase;
    private boolean signup_status=false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editBusStop=(EditText)findViewById(R.id.editBusStop);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        editTextName=(EditText)findViewById(R.id.editTextName);

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

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        signin=(TextView)findViewById(R.id.textSignIn);
        SignUp=(Button)findViewById(R.id.buttonRegister);
        SignUp.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonRegister:
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                break;
            case R.id.textSignIn:
                Intent i=new Intent(SignUp.this,Login.class);
                startActivity(i);
                finish();
                break;
        }
    }
    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError("Required.");
        }
        else if(editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError("Required.");
        }
        else if (editTextName.getText().toString().isEmpty()){
            editTextName.setError("Required.");
        }
        else if (editBusStop.getText().toString().isEmpty()){
            editBusStop.setError("Required.");
        }
        else if (busnum.isEmpty()){
            Toast.makeText(SignUp.this,"Select Valid Bus number",Toast.LENGTH_SHORT).show();
        }else {
            showProgressDialog();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                writeNewUser(user.getUid(), editTextName.getText().toString(), email, password, busnum, editBusStop.getText().toString());
                                signup_status = true;
                                if (signup_status == true) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                    alertDialogBuilder
                                            .setTitle("Locator")
                                            .setIcon(R.drawable.applogo)
                                            .setMessage("User Account successfully created. Press OK to Login")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setCancelable(false);

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();
                                }
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "User ID already exists",
                                        Toast.LENGTH_SHORT).show();
                            }
                            hideProgressDialog();
                        }
                    });
        }
    }
    private void writeNewUser(String userId,String username,String email,String password,String busnum,String busstop) {
        user_model user = new user_model(username, email,password,busnum,busstop);
        mDatabase.child("users").child(userId).setValue(user);
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}

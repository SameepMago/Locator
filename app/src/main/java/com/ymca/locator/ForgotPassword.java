package com.ymca.locator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ymca.locator.Views.PlayGifView;

public class ForgotPassword extends BaseActivity {
    EditText editTextEmail;
    Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        PlayGifView pGif = (PlayGifView) findViewById(R.id.imageView);
        pGif.setImageResource(R.drawable.fpass);
        editTextEmail=(EditText)findViewById(R.id.email);
        reset=(Button)findViewById(R.id.fpass);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().isEmpty()){
                    editTextEmail.setError("Required.");

                }else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    showProgressDialog();
                    auth.sendPasswordResetEmail(editTextEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPassword.this);

                                        alertDialogBuilder
                                                .setTitle("Locator")
                                                .setIcon(R.drawable.applogo)
                                                .setMessage("Check Email id for further steps")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false);

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // show it
                                        alertDialog.show();

                                    } else {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPassword.this);

                                        alertDialogBuilder
                                                .setTitle("Locator")
                                                .setIcon(R.drawable.applogo)
                                                .setMessage("Invalid Email id.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false);

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // show it
                                        alertDialog.show();
                                    }
                                    hideProgressDialog();
                                }
                            });
                }
            }
        });
    }
}

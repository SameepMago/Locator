package com.ymca.locator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import static android.content.ContentValues.TAG;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Task<InstallationTokenResult> refreshedToken = FirebaseInstallations.getInstance().getToken(false);
        sendRegistrationToServer(refreshedToken.toString());
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    private void sendRegistrationToServer(String refreshToken) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}



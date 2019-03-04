/*
 * Created by Sujoy Datta. Copyright (c) 2018. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.chattr.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static Socket socket;

//    public static boolean isUserOnline(Context context) {
//        @SuppressLint("ServiceCast") ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        try {
//            NetworkInfo networkInfo = null;
//            if (connectivityManager != null) {
//                networkInfo = connectivityManager.getActiveNetworkInfo();
//            } else {
//                Log.i("NetworkManger", "Connectivity Manager is null");
//            }
//            return networkInfo != null && networkInfo.isConnectedOrConnecting();
//        } catch (NullPointerException exception) {
//            Log.i("NetworkManger", "Exception: " + exception.getMessage());
//            return false;
//        }
//    }

    //Checks if device has internet access
    //https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
    public static boolean hasInternetAccess() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isConnectToSocket() {
        try {
            socket = IO.socket(ConstantManager.IP_LOCALHOST);
            socket.connect();
            return true;
        } catch (Exception e) {
            Log.i(TAG, "Connection failed: " + e.getMessage());
            return false;
        }
    }

    public static void disconnectFromSocket() {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public static void changeLoggedInStatus(Context context, String status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantManager.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(ConstantManager.PREF_TITLE_USER_USERNAME, "");
        JSONObject data = new JSONObject();
        try {
            data.put("displayName", username);
            if (socket == null) {
                socket = IO.socket(ConstantManager.IP_LOCALHOST);
                socket.connect();
            }
            if (status.equalsIgnoreCase(ConstantManager.OFF))
                socket.emit("statusOffline", data);
            else
                socket.emit("statusOnline", data);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

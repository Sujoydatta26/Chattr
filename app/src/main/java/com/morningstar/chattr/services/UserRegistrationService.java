/*
 * Created by Sujoy Datta. Copyright (c) 2019. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.chattr.services;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.morningstar.chattr.activities.MainActivity;
import com.morningstar.chattr.managers.ConstantManager;
import com.morningstar.chattr.managers.UtilityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import io.socket.client.Socket;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class UserRegistrationService {
    private static final String TAG = "UserRegistrationService";

    private static UserRegistrationService userRegistrationService;

    private final int REGISTRATION_SUCCESS = 0;
    private final int REGISTRATION_FAILURE = -1;
    private final int USER_ERROR_EMPTY_PASSWORD = 1;
    private final int USER_ERROR_EMPTY_EMAIL = 2;
    private final int USER_ERROR_PASSWORD_SHORT = 3;
    private final int USER_ERROR_EMAIL_BAD_FORMAT = 4;
    private final int USERNAME_ERROR_EMPTY_USERNAME = 5;
    private final int USER_ERROR_INVALID_MOBILE_NUMBER = 6;
    private final int USER_NO_ERRORS = 7;

    private String emailAddress;
    private String userName;
    private String mobileNumber;

    public static UserRegistrationService newInstance() {
        if (userRegistrationService == null) {
            userRegistrationService = new UserRegistrationService();
        }

        return userRegistrationService;
    }

    //send the registration info to node server
    public Subscription sendRegistrationInfo(EditText editTextUserEmail, EditText editTextPassword,
                                             EditText editTextUsername, EditText editTextMobileNumber, ActionProcessButton button, Socket socket) {

        ArrayList<String> userDetails = new ArrayList<>();
        userDetails.add(editTextUserEmail.getText().toString());
        userDetails.add(editTextPassword.getText().toString());
        userDetails.add(editTextUsername.getText().toString());
        userDetails.add(editTextMobileNumber.getText().toString());

        Observable<ArrayList<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<ArrayList<String>, Integer>() {
                    @Override
                    public Integer call(ArrayList<String> strings) {
                        String userEmail = strings.get(0);
                        String userPassword = strings.get(1);
                        String userUsername = strings.get(2);
                        String userMobileNumber = strings.get(3);

                        int RESULT_CODE = -1;

                        if (!userEmail.isEmpty() && !userPassword.isEmpty() && userPassword.length() >= 6
                                && !userUsername.isEmpty() && !userMobileNumber.isEmpty() && (userMobileNumber.length() == 10)) {

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put(ConstantManager.STRING_EMAIL, userEmail);
                                jsonObject.put(ConstantManager.STRING_PASSWORD, userPassword);
                                jsonObject.put(ConstantManager.STRING_USERNAME, userUsername);
                                jsonObject.put(ConstantManager.STRING_MOBILE_NUMBER, userMobileNumber);

                                socket.emit("userData", jsonObject);

                                RESULT_CODE = REGISTRATION_SUCCESS;
                                emailAddress = userEmail;
                                userName = userUsername;
                                mobileNumber = userMobileNumber;

                            } catch (JSONException e) {
                                Log.i(TAG, "JSON Exception: " + e.getMessage());
                                RESULT_CODE = REGISTRATION_FAILURE;
                            }
                        } else {
                            if (userEmail.isEmpty())
                                RESULT_CODE = USER_ERROR_EMPTY_EMAIL;
                            if (userPassword.isEmpty())
                                RESULT_CODE = USER_ERROR_EMPTY_PASSWORD;
                            if (userUsername.isEmpty())
                                RESULT_CODE = USERNAME_ERROR_EMPTY_USERNAME;
                            if (userPassword.length() < 6)
                                RESULT_CODE = USER_ERROR_PASSWORD_SHORT;
                            if (!UtilityManager.isEmailVaild(userEmail))
                                RESULT_CODE = USER_ERROR_EMAIL_BAD_FORMAT;
                            if (userMobileNumber.length() != 10)
                                RESULT_CODE = USER_ERROR_INVALID_MOBILE_NUMBER;
                        }

                        return RESULT_CODE;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)) {
                            editTextUserEmail.setError("Email cannot be empty");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)) {
                            editTextPassword.setError("Password cannot be empty");
                            button.setProgress(0);
                        } else if (integer.equals(USERNAME_ERROR_EMPTY_USERNAME)) {
                            editTextUsername.setError("Username cannot be empty");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_PASSWORD_SHORT)) {
                            editTextPassword.setError("Password should be more than 6 letters");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)) {
                            editTextUserEmail.setError("Please check email id");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_INVALID_MOBILE_NUMBER)) {
                            editTextMobileNumber.setError("Please enter 10 digits number");
                            button.setProgress(0);
                        } else if (integer.equals(REGISTRATION_FAILURE)) {
                            button.setProgress(-1);
                            Log.i(TAG, "Registration failed");
                        }

                        Log.i(TAG, "Result code: " + integer);
                    }
                });
    }

    //To get the response of registration from server
    public Subscription receiveRegistrationResponse(String response, Activity activity, ActionProcessButton actionProcessButton) {
        Observable<String> stringObservable = Observable.just(response);
        return stringObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String response) {
                        if (response.equalsIgnoreCase(ConstantManager.REGISTRATION_SUCCESS_MESSAGE)) {
                            actionProcessButton.setProgress(100);

                            SharedPreferences sharedPreferences = activity.getSharedPreferences(ConstantManager.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(ConstantManager.PREF_TITLE_USER_EMAIL, emailAddress);
                            editor.putString(ConstantManager.PREF_TITLE_USER_USERNAME, userName);
                            editor.putString(ConstantManager.PREF_TITLE_USER_MOBILE, mobileNumber);
                            editor.apply();

                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        } else {
                            actionProcessButton.setProgress(-1);
                            Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Subscription sendLoginInfo(EditText editTextUserEmail, EditText editTextPassword, Socket socket, Activity activity, ActionProcessButton button) {
        ArrayList<String> userDetails = new ArrayList<>();
        userDetails.add(editTextUserEmail.getText().toString());
        userDetails.add(editTextPassword.getText().toString());

        Observable<ArrayList<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<ArrayList<String>, Integer>() {
                    @Override
                    public Integer call(ArrayList<String> strings) {
                        String userEmail = strings.get(0);
                        String userPassword = strings.get(0);

                        if (userEmail.isEmpty())
                            return USER_ERROR_EMPTY_EMAIL;
                        if (userPassword.isEmpty())
                            return USER_ERROR_EMPTY_PASSWORD;
                        if (userPassword.length() < 6)
                            return USER_ERROR_PASSWORD_SHORT;
                        if (UtilityManager.isEmailVaild(userEmail))
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        else {
                            //Check if user exists.
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(activity, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                JSONObject sendData = new JSONObject();
                                                try {
                                                    sendData.put("email", userEmail);
                                                    socket.emit("userInfo", sendData);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });

                            //Should add custom token and not allow signing in using firebase generated token
                            FirebaseAuth.getInstance().signOut();
                            return USER_NO_ERRORS;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)) {
                            editTextUserEmail.setError("Email cannot be empty");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)) {
                            editTextPassword.setError("Password cannot be empty");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_PASSWORD_SHORT)) {
                            editTextPassword.setError("Password should be more than 6 letters");
                            button.setProgress(0);
                        } else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)) {
                            editTextUserEmail.setError("Please check email id");
                            button.setProgress(0);
                        }
                    }
                });
    }

    public Subscription getAuthToken(JSONObject jsonObject, Activity activity, ActionProcessButton button) {
        Observable<JSONObject> jsonObjectObservable = Observable.just(jsonObject);

        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<JSONObject, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call(JSONObject jsonObject) {
                        ArrayList<String> userDetails = new ArrayList<>();

                        try {
                            JSONObject serverData = jsonObject.getJSONObject("token");
                            String token = (String) serverData.get("authToken");
                            String email = (String) serverData.get("email");
                            String displayName = (String) serverData.get("displayName");

                            userDetails.add(token);
                            userDetails.add(email);
                            userDetails.add(displayName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return userDetails;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<String> strings) {
                        String token = strings.get(0);
                        String email = strings.get(1);
                        String displayname = strings.get(2);

                        if (!email.equalsIgnoreCase("error")) {
                            FirebaseAuth.getInstance().signInWithCustomToken(token)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(activity, "Could not sign in", Toast.LENGTH_SHORT).show();
                                                button.setProgress(-1);
                                            } else {
                                                button.setProgress(100);
                                                SharedPreferences sharedPreferences = activity.getSharedPreferences(ConstantManager.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(ConstantManager.PREF_TITLE_USER_EMAIL, email);
                                                editor.putString(ConstantManager.PREF_TITLE_USER_USERNAME, displayname);
                                                editor.apply();

                                                activity.startActivity(new Intent(activity, MainActivity.class));
                                                activity.finish();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
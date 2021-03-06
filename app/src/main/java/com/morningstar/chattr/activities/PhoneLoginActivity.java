///*
// * Created by Sujoy Datta. Copyright (c) 2018. All rights reserved.
// *
// * To the person who is reading this..
// * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
// * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
// */
//
//package com.morningstar.chattr.activities;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.dd.processbutton.iml.ActionProcessButton;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.TaskExecutors;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.morningstar.chattr.R;
//import com.morningstar.chattr.managers.ConstantManager;
//import com.morningstar.chattr.managers.ProfileManager;
//
//import java.util.concurrent.TimeUnit;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class PhoneLoginActivity extends AppCompatActivity {
//
//    private final String TAG = "PhoneLoginActivity";
//
//    private EditText editTextPhoneNumber;
//    private ActionProcessButton buttonVerify;
//    private TextView textView;
//    private TextView loginUisngEmail;
//
//    private LinearLayout rootLayout;
//
//    private String phoneNumber;
//    private String smsCode;
//    private String verificationId;
//
//    private FirebaseAuth firebaseAuth;
//    private SharedPreferences sharedPreferences;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            smsCode = phoneAuthCredential.getSmsCode();
//            Log.i(TAG, "Verification started: " + smsCode);
//            if (smsCode != null) {
//                verifySmsCode(smsCode);
//            } else {
//                buttonVerify.setProgress(-1);
//                textView.setText(getResources().getString(R.string.enter_phone_number_to_verify));
//                Snackbar snackbar = Snackbar.make(rootLayout, "Error while verifying phone. Please try signing up with email Id.", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Snackbar snackbar = Snackbar.make(rootLayout, "Sms verification failed", Snackbar.LENGTH_SHORT);
//            snackbar.show();
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationId = s;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone_login);
//
//        editTextPhoneNumber = findViewById(R.id.loginPhoneNumber);
//        buttonVerify = findViewById(R.id.loginVerifyButton);
//        textView = findViewById(R.id.textView1);
//        rootLayout = findViewById(R.id.loginRootLayout);
//        loginUisngEmail = findViewById(R.id.loginUsingEmail);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        buttonVerify.setProgress(0);
//        buttonVerify.setMode(ActionProcessButton.Mode.ENDLESS);
//
//        buttonVerify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                phoneNumber = editTextPhoneNumber.getText().toString();
//                if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() <= 10) {
//                    buttonVerify.setProgress(1);
//                    textView.setText("Please wait..");
//                    buttonVerify.setEnabled(false);
//                    Log.i(TAG, "Phone verification started");
//                    sendVerificationCode(phoneNumber);
//                } else {
//                    editTextPhoneNumber.setError("Enter a valid number");
//                }
//            }
//        });
//
//        loginUisngEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PhoneLoginActivity.this, LoginUsingEmailActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    private void verifySmsCode(String smsCode) {
//        //Creating the credential
//        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, smsCode);
//        //signing in the user
//        signInWithPhoneCreds(phoneAuthCredential);
//        Log.i(TAG, "Sms OTP sent: " + smsCode);
//    }
//
//    private void signInWithPhoneCreds(PhoneAuthCredential phoneAuthCredential) {
//        firebaseAuth.signInWithCredential(phoneAuthCredential)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        sharedPreferences = getSharedPreferences(ConstantManager.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString(ConstantManager.PREF_TITLE_USER_MOBILE, phoneNumber);
//                        editor.putString(ConstantManager.PREF_TITLE_USER_ID, firebaseAuth.getUid());
//                        editor.apply();
//                        ProfileManager.userMobile = phoneNumber;
//                        ProfileManager.userId = firebaseAuth.getUid();
//                        buttonVerify.setProgress(100);
//                        Log.i(TAG, "Signing in with phone creds");
//                        Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Snackbar snackbar = Snackbar.make(rootLayout, "Verification failed", Snackbar.LENGTH_SHORT);
//                        snackbar.show();
//                    }
//                });
//    }
//
//    private void sendVerificationCode(String phoneNumber) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, verificationStateChangedCallbacks);
//        Log.i(TAG, "Verification code sent");
//    }
//}

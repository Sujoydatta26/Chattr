/*
 * Created by Sujoy Datta. Copyright (c) 2018. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.chattr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.morningstar.chattr.R;
import com.morningstar.chattr.managers.ConstantManager;
import com.morningstar.chattr.utils.DrawerUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout rootLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    private String mobileNumber = "";
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mainActivityToolbar);
        rootLayout = findViewById(R.id.mainActivityRootLayout);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chattr");

        DrawerUtils.getDrawer(this, toolbar);

        sharedPreferences = getSharedPreferences(ConstantManager.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
        mobileNumber = sharedPreferences.getString(ConstantManager.PREF_TITLE_USER_MOBILE, null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (mobileNumber != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(ConstantManager.FIREBASE_PHONE_NUMBERS_TABLE)
                    .child(mobileNumber).child(ConstantManager.FIREBASE_IS_ONLINE_COLUMN);
            databaseReference.setValue(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, RegisterUsingEmail.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logOutUser();
                break;
        }
        return true;
    }

    private void logOutUser() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ConstantManager.FIREBASE_PHONE_NUMBERS_TABLE)
                .child(mobileNumber);
        databaseReference.child(ConstantManager.FIREBASE_IS_ONLINE_COLUMN)
                .setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(ConstantManager.FIREBASE_IS_LOGGED_IN_COLUMN).setValue(false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(MainActivity.this, LoginUsingEmailActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar.make(rootLayout, "Could not sign out at the moment", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("TRY AGAIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                logOutUser();
                            }
                        });
                        snackbar.show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ConstantManager.FIREBASE_PHONE_NUMBERS_TABLE).child(mobileNumber)
                .child(ConstantManager.FIREBASE_IS_ONLINE_COLUMN);
        databaseReference.setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }
}

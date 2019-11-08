package com.cmput301f19t09.vibes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f19t09.vibes.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        mStore.setFirestoreSettings(settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent;

        if (mAuth.getCurrentUser() != null) {
            User user = new User(mAuth.getCurrentUser().getUid());
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
    }
}

package com.example.vibes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private TextView emailTextView;
    private TextView passwordTextView;
    private TextView confirmPasswordTextView;
    private Button signUpButton;
    private Button loginButton;
    private FirebaseAuth mAuth;
    final private String TAG = "authentication";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email_field);
        passwordTextView = findViewById(R.id.password_field);
        confirmPasswordTextView = findViewById(R.id.confirm_password_field);
        signUpButton = findViewById(R.id.sign_up_button);
        loginButton = findViewById(R.id.login_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String confirmPassword = confirmPasswordTextView.getText().toString();

                if (validatePasswordMatch(password, confirmPassword)) {
                    createUser(email, password);
                } else {
                    // TOAST for mismatching passwords
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
    }

    public boolean validatePasswordMatch(String password, String confirmPassword) {
        return password != confirmPassword;
    }

    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}

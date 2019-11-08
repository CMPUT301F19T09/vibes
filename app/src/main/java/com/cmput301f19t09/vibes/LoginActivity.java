package com.cmput301f19t09.vibes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.function.Consumer;

public class LoginActivity extends AppCompatActivity {
    private TextView emailTextView;
    private TextView passwordTextView;
    private Button loginButton;
    private Button signUpButton;

    private FirebaseAuth mAuth;

    final private String TAG = "authentication";

    /**
     * Initialize login activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextView = findViewById(R.id.email_field);
        passwordTextView = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email field is empty", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password field is empty", Toast.LENGTH_LONG).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpActivity();
            }
        });
    }

    /**
     * Start sign up activity
     */
    public void startSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Start main activity
     */
    public void startMainActivity() {
        //User user = new User(mAuth.getCurrentUser().getUid());
        final String userId = mAuth.getCurrentUser().getUid();
        //UserManager.registerCurrentUser(userId);
        UserManager.registerCurrentUser(new Consumer<User>() {
            @Override
            public void accept(User currentUser) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user_id", userId);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * Authenticate user with email and password
     * @param email
     * @param password
     */
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "signInWithEmail:success");
                            startMainActivity();
                        } else {
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Start the home activity
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

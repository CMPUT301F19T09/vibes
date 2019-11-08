package com.cmput301f19t09.vibes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private TextView emailTextView;
    private TextView usernameTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView passwordTextView;
    private TextView confirmPasswordTextView;
    private Button signUpButton;
    private Button loginButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    private boolean isLoading = false;

    final private String TAG = "authentication";
    final private String USER_COLLECTION = "users";

    /**
     * Initialize the sign up activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailTextView = findViewById(R.id.email_field);
        usernameTextView = findViewById(R.id.username_field);
        firstNameTextView = findViewById(R.id.first_name_field);
        lastNameTextView = findViewById(R.id.last_name_field);
        passwordTextView = findViewById(R.id.password_field);
        confirmPasswordTextView = findViewById(R.id.confirm_password_field);
        signUpButton = findViewById(R.id.sign_up_button);
        loginButton = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                String username = usernameTextView.getText().toString();
                String firstName = firstNameTextView.getText().toString();
                String lastName = lastNameTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String confirmPassword = confirmPasswordTextView.getText().toString();

                if (!isLoading) {
                    if (validatePasswordMatch(password, confirmPassword)) {
                        createUserFirebaseAuth(email, username, firstName, lastName, password);
                        isLoading = true;
                    } else {
                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Please wait... creating your account", Toast.LENGTH_LONG).show();
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

    /**
     * Start the login activity
     */
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Check if the password and confirm password match
     * @param password
     * @param confirmPassword
     * @return
     */
    public boolean validatePasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * After user enters all the sign up fields and presses sign up button, this function creates
     * a user in Firebase Authentication.
     * @param email
     * @param username
     * @param firstName
     * @param lastName
     * @param password
     */
    public void createUserFirebaseAuth(String email, String username, String firstName, String lastName, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            createUserFirebaseStore(email, username, firstName, lastName);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            // Handle if user email address already exists
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            isLoading = false;
                        }
                    }
                });
    }

    /**
     * This function creates a Firestore document with name that matches the UID of Firebase
     * Authentication user inside the 'users' collection. The Firestore document stores
     * information about user
     * @param email
     * @param username
     * @param firstName
     * @param lastName
     */
    public void createUserFirebaseStore(String email, String username, String firstName, String lastName) {
        String picturePath = "image/" + username + ".png";

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);
        userData.put("first", firstName);
        userData.put("last", lastName);
        userData.put("profile_picture", picturePath);
        userData.put("following_list", new ArrayList<>());
        userData.put("moods", new ArrayList<>());
        userData.put("requested_list", new ArrayList<>());

        final CollectionReference collectionReference = mStore.collection(USER_COLLECTION);

        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data addition successful");

                        startLoginActivity();
                        isLoading = false;

                        Uri imageUri = Uri.parse("android.resource://com.cmput301f19t09.vibes/" + R.drawable.default_profile_picture);
                        mStorageReference = mStorage.getReference(picturePath);
                        mStorageReference
                                .putFile(imageUri)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Failed to store default profile picture");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data addition unsuccessful");
                        Toast.makeText(SignUpActivity.this, "There was an error creating your account", Toast.LENGTH_LONG).show();
                        isLoading = false;
                    }
                });
    }
}

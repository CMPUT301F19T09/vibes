package com.cmput301f19t09.vibes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f19t09.vibes.models.User;
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
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    final private String TAG = "authentication";
    final private String USER_COLLECTION = "users";

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

                if (validatePasswordMatch(password, confirmPassword)) {
                    createUserFirebaseAuth(email, username, firstName, lastName, password);
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
        startActivity(intent);
    }

    public boolean validatePasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

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
                        }
                    }
                });
    }

    public void createUserFirebaseStore(String email, String username, String firstName, String lastName) {
        User user = new User(username, firstName, lastName, email);

        // Maybe get this object from User class
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("username", user.getUserName());
        userData.put("first_name", user.getFirstName());
        userData.put("last_name", user.getLastName());
        userData.put("profile_picture_path", user.getPicturePath());
        userData.put("following", user.getFollowingList());
        userData.put("moods", user.getMoodEvents());
        userData.put("requested", user.getRequestedList());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection(USER_COLLECTION);

        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data addition successful");

                        startLoginActivity();

                        Uri imageUri = Uri.parse("android.resource://com.cmput301f19t09.vibes/" + R.drawable.default_profile_picture);
                        mStorageReference = mStorage.getReference(user.getPicturePath());
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
                    }
                });
    }
}

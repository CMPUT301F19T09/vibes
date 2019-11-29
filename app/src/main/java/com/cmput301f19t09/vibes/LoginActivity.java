package com.cmput301f19t09.vibes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    // Components for retrieving forgot password.
    private Button forgotPassword;
    private String m_Text;
    private Context context;

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

        context = this;
        emailTextView = findViewById(R.id.email_field);
        passwordTextView = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        forgotPassword = findViewById(R.id.forgotPassword);

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

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter your email to reset password");

                // Set up the input
                final EditText input = new EditText(context);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        mAuth.sendPasswordResetEmail(m_Text)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            new AlertDialog.Builder(context)
                                                    .setTitle("Email is sent!")
                                                    .setMessage("Check your inbox for a link to reset your password.")

                                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Continue with delete operation
                                                        }
                                                    })

                                                    // A null listener allows the button to dismiss the dialog and take no further action.
//                                                    .setNegativeButton(android.R.string.no, null)
//                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }else{
                                            Log.d("forgotPassword", "Retrieving password failed.");
                                        }
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
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

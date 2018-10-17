package com.rodrigues.pedroschwarz.awesomeplaces.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.AuthHelper;

public class LoginActivity extends AppCompatActivity {

    // Declare variables
    private ConstraintLayout logLayout;
    private ProgressBar logProg;
    private TextInputEditText logEmail;
    private TextInputEditText logPass;
    private Button logBtn;
    private Button logRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is logged in
        checkUser();

        // Set widgets
        logLayout = findViewById(R.id.log_layout);
        logProg = findViewById(R.id.log_prog);
        logEmail = findViewById(R.id.log_email);
        logPass = findViewById(R.id.log_pass);
        logBtn = findViewById(R.id.log_btn);
        logRegBtn = findViewById(R.id.log_reg_btn);

        // Add login btn click listener
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from inputs
                String email = logEmail.getText().toString();
                String pass = logPass.getText().toString();
                // Check values
                if (!(email.isEmpty()) && !(pass.isEmpty())) {
                    // Enable ProgressBar
                    logProg.setVisibility(View.VISIBLE);
                    // Login user
                    loginUser(email, pass);
                } else {
                    Snackbar.make(logLayout, "You must enter your email and password.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // Add logReg btn click listener
        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to register screen
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void checkUser() {
        // If user is logged in
        if (AuthHelper.getAuth().getCurrentUser() != null) {
            // Go to Main Screen
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void loginUser(String email, String pass) {
        AuthHelper.getAuth().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Verify task
                if (task.isSuccessful()) {
                    // Go to Main Screen
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Handle errors
                    String error = "";
                    // Disable ProgressBar
                    logProg.setVisibility(View.GONE);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Invalid email/password.";
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = "Account not found or was blocked.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(logLayout, error, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}

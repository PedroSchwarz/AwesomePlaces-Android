package com.rodrigues.pedroschwarz.awesomeplaces.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.AuthHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.StorageHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.model.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    // Declare variables
    private ConstraintLayout regLayout;
    private CircleImageView regImage;
    private FloatingActionButton regImageFab;

    private ProgressBar regProg;
    private TextInputEditText regUsername;
    private TextInputEditText regEmail;
    private TextInputEditText regPass;
    private Button regBtn;

    private String username = "";
    private String email = "";
    private String pass = "";
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set widgets
        regLayout = findViewById(R.id.reg_layout);
        regImage = findViewById(R.id.reg_image);
        regImageFab = findViewById(R.id.reg_image_fab);

        regProg = findViewById(R.id.reg_prog);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPass = findViewById(R.id.reg_pass);
        regBtn = findViewById(R.id.reg_btn);

        // Add image btn listener
        regImageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use crop library
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(RegisterActivity.this);
            }
        });

        // Add on click listener
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from inputs
                username = regUsername.getText().toString();
                email = regEmail.getText().toString();
                pass = regPass.getText().toString();
                // Check values
                if (!(username.isEmpty()) && !(email.isEmpty()) && !(pass.isEmpty())) {
                    if (imageUri != null) {
                        // Enable ProgressBar
                        regProg.setVisibility(View.VISIBLE);
                        // Call register method
                        registerUser();
                    } else {
                        Snackbar.make(regLayout, "You must chose a profile image.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(regLayout, "You must enter a username, email and password.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser() {
        AuthHelper.getAuth().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Verify task
                if (task.isSuccessful()) {
                    storeImage();
                } else {
                    // Handle errors
                    String error = "";
                    // Disable ProgressBar
                    regProg.setVisibility(View.GONE);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = "This email is already in use.";
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Weak password, try another one.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Invalid email, try another one.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(regLayout, error, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void storeImage() {
        // Get storage profile reference
        final StorageReference profilePath = StorageHelper.getPathStorage(StorageHelper.getUserImagePath());
        // Store image
        profilePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                // Verify task
                if (task.isSuccessful()) {
                    // Get image url
                    profilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Instantiate user
                            User user = new User(AuthHelper.getUserUid(), username, email, pass, uri.toString());
                            createUser(user);
                        }
                    });
                } else {
                    // Disable ProgressBar
                    regProg.setVisibility(View.GONE);
                    Snackbar.make(regLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(User user) {
        // Create user on database
        DatabaseHelper.getUsersRef().document(AuthHelper.getUserUid()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Verify task
                        if (task.isSuccessful()) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Disable ProgressBar
                            regProg.setVisibility(View.GONE);
                            Snackbar.make(regLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // Set value for imageUri
                imageUri = result.getUri();
                // Set profile image
                regImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.AuthHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.StorageHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.model.Place;
import com.rodrigues.pedroschwarz.awesomeplaces.model.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

public class NewPlaceActivity extends AppCompatActivity {

    private ConstraintLayout newPlaceLayout;
    private ImageView newPlaceImage;
    private FloatingActionButton newPlaceFab;
    private ProgressBar newPlaceProg;
    private TextInputEditText newPlaceTitle;
    private TextInputEditText newPlaceDesc;
    private TextInputEditText newPlacePrice;
    private Spinner newPlaceCountries;
    private Button newPlaceBtn;

    private User currentUser;

    private String title;
    private String desc;
    private Double price;
    private String country;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);

        newPlaceLayout = findViewById(R.id.new_place_layout);
        newPlaceImage = findViewById(R.id.new_place_image);
        newPlaceFab = findViewById(R.id.new_place_fab);
        newPlaceProg = findViewById(R.id.new_place_prog);
        newPlaceTitle = findViewById(R.id.new_place_title);
        newPlaceDesc = findViewById(R.id.new_place_desc);
        newPlacePrice = findViewById(R.id.new_place_price);
        newPlaceCountries = findViewById(R.id.new_place_countries);
        newPlaceBtn = findViewById(R.id.new_place_btn);

        configSpinner();

        newPlaceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(NewPlaceActivity.this);
            }
        });

        newPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String priceText = newPlacePrice.getText().toString();

                title = newPlaceTitle.getText().toString();
                desc = newPlaceDesc.getText().toString();
                price = priceText.isEmpty() ? 0.0 : Double.parseDouble(priceText);

                if (!(title.isEmpty()) && !(desc.isEmpty())) {
                    if (imageUri != null) {
                        if (!(country.equals("Select the country…"))) {
                            newPlaceProg.setVisibility(View.VISIBLE);
                            storeImage();
                        } else {
                            Snackbar.make(newPlaceLayout, "You must chose a country.", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(newPlaceLayout, "You must chose a image.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(newPlaceLayout, "You must enter the title and description.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void storeImage() {
        final StorageReference imagePath = StorageHelper.getPathStorage(StorageHelper.getPlaceImagePath());
        imagePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            createPlace(image);
                        }
                    });
                } else {
                    newPlaceProg.setVisibility(View.GONE);
                    Snackbar.make(newPlaceLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createPlace(String image) {
        DocumentReference placeRef = DatabaseHelper.getPlacesRef().document();
        Place place = new Place(placeRef.getId(), title, desc, price, country, image, currentUser);
        placeRef.set(place).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                } else {
                    newPlaceProg.setVisibility(View.GONE);
                    Snackbar.make(newPlaceLayout, "Something went wrong, try again later.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void configSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newPlaceCountries.setAdapter(adapter);
        newPlaceCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCurrentUser() {
        DatabaseHelper.getUsersRef().document(AuthHelper.getUserUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                newPlaceImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCurrentUser();
    }
}

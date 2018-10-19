package com.rodrigues.pedroschwarz.awesomeplaces.activity;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.adapter.CommentsAdapter;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.AuthHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.model.Comment;
import com.rodrigues.pedroschwarz.awesomeplaces.model.Place;
import com.rodrigues.pedroschwarz.awesomeplaces.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceActivity extends AppCompatActivity {

    private ImageView placeImage;
    private Toolbar placeToolbar;

    private ConstraintLayout placeLayout;
    private CircleImageView placeAuthorImage;
    private TextView placeAuthorName;
    private TextView placeCreatedAt;
    private TextView placePrice;
    private TextView placeCountry;
    private TextView placeDesc;

    private ImageButton placeComBtn;
    private RecyclerView placeComRv;
    private TextView placeNoCom;

    private CardView placeAddCom;
    private EditText placeComBody;
    private Button placeAddBtn;

    private String placeId;
    private String placeTitle;

    private boolean comEnabled = false;

    private User currentUser;

    private List<Comment> comments;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        placeId = getIntent().getExtras().getString("placeId");
        placeTitle = getIntent().getExtras().getString("placeTitle");

        placeLayout = findViewById(R.id.place_layout);
        placeImage = findViewById(R.id.place_image);
        placeToolbar = findViewById(R.id.place_toolbar);

        placeAuthorImage = findViewById(R.id.place_author_image);
        placeAuthorName = findViewById(R.id.place_author_name);
        placeCreatedAt = findViewById(R.id.place_created_at);
        placePrice = findViewById(R.id.place_price);
        placeCountry = findViewById(R.id.place_country);
        placeDesc = findViewById(R.id.place_desc);

        placeComBtn = findViewById(R.id.place_com_btn);
        placeComRv = findViewById(R.id.place_com_rv);
        placeNoCom = findViewById(R.id.place_no_com);

        placeAddCom = findViewById(R.id.place_add_com);
        placeComBody = findViewById(R.id.place_com_body);
        placeAddBtn = findViewById(R.id.place_add_btn);

        placeToolbar.setTitle(placeTitle);
        setSupportActionBar(placeToolbar);

        getPlace();
        onComBtnClick();
        configRecycler();
        onAddBtnClick();
    }

    private void getPlace() {
        DatabaseHelper.getPlaceRef(placeId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            Place place = doc.toObject(Place.class);

                            populatePlace(place);
                        }
                    }
                });
    }

    private void populatePlace(Place place) {
        Picasso.get().load(place.getImage()).placeholder(R.drawable.place_image).into(placeImage);
        Picasso.get().load(place.getAuthor().getImage()).placeholder(R.drawable.place_image).into(placeAuthorImage);
        placeAuthorName.setText(place.getAuthor().getUsername());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        placeCreatedAt.setText(formatter.format(place.getCreatedAt()));
        placePrice.setText(String.valueOf(place.getPrice()));
        placeCountry.setText(place.getCountry());
        placeDesc.setText(place.getDesc());
    }

    private void onComBtnClick() {
        placeComBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!comEnabled) {
                    fadeIn();
                } else {
                    fadeOut();
                }
            }
        });
    }

    private void fadeOut() {
        placeAddCom.animate().alpha(1).translationY(-600).setDuration(1000);
        comEnabled = !comEnabled;
    }

    private void fadeIn() {
        placeAddCom.animate().alpha(1).translationY(500).setDuration(1000);
        comEnabled = !comEnabled;
    }

    private void configRecycler() {
        comments = new ArrayList<>();
        adapter = new CommentsAdapter(comments);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        placeComRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        placeComRv.setHasFixedSize(true);
        placeComRv.setLayoutManager(manager);
        placeComRv.setAdapter(adapter);
    }

    private void onAddBtnClick() {
        placeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body = placeComBody.getText().toString();
                if (!(body.isEmpty())) {
                    createComment(body);
                } else {
                    Snackbar.make(placeLayout, "You must write a comment.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createComment(String body) {
        DocumentReference commentRef = DatabaseHelper.getCommentsRef().document();
        String comId = commentRef.getId();
        Comment comment = new Comment(comId, placeId, body, currentUser);
        commentRef.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fadeOut();
                placeComBody.setText("");
                getComments();
            }
        });
    }

    private void getComments() {
        DatabaseHelper.getCommentsRef()
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    comments.clear();
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        if (snapshot.get("placeId").equals(placeId)) {
                            Comment comment = snapshot.toObject(Comment.class);
                            comments.add(comment);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (comments.isEmpty()) {
                        placeNoCom.setVisibility(View.VISIBLE);
                    } else {
                        placeNoCom.setVisibility(View.GONE);
                    }
                }
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
    protected void onStart() {
        super.onStart();
        getComments();
        getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        if (comEnabled) {
            fadeOut();
        } else {
            super.onBackPressed();
        }
    }
}

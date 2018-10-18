package com.rodrigues.pedroschwarz.awesomeplaces.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.adapter.PlacesAdapter;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.AuthHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.DatabaseHelper;
import com.rodrigues.pedroschwarz.awesomeplaces.model.Place;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declare variables
    private Toolbar mainToolbar;
    private SwipeRefreshLayout mainSwipe;
    private RecyclerView mainRv;
    private FloatingActionButton mainFab;
    private ProgressBar mainProg;

    private List<Place> places;
    private PlacesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set widgets
        mainToolbar = findViewById(R.id.main_toolbar);
        mainSwipe = findViewById(R.id.main_swipe);
        mainRv = findViewById(R.id.main_rv);
        mainFab = findViewById(R.id.main_fab);
        mainProg = findViewById(R.id.main_prog);
        setSupportActionBar(mainToolbar);

        configSwipe();
        configRecycler();

        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewPlaceActivity.class));
            }
        });
    }

    private void configSwipe() {
        mainSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUi();
            }
        });
    }

    private void updateUi() {
        getPlaces();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainSwipe.setRefreshing(false);
            }
        }, 2000);
    }

    private void configRecycler() {
        places = new ArrayList<>();
        adapter = new PlacesAdapter(places, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);

        mainRv.setHasFixedSize(true);
        mainRv.setLayoutManager(manager);
        mainRv.setAdapter(adapter);
    }

    private void getPlaces() {
        if (places.isEmpty()) {
            mainProg.setVisibility(View.VISIBLE);
        }
        DatabaseHelper.getPlacesRef().orderBy("createdAt", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            places.clear();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Place place = doc.toObject(Place.class);
                                places.add(place);
                            }
                            adapter.notifyDataSetChanged();
                            mainProg.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void logoutUser() {
        // Sign out user
        AuthHelper.getAuth().signOut();
        // Go back to login screen
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPlaces();
    }
}

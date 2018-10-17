package com.rodrigues.pedroschwarz.awesomeplaces.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.helper.AuthHelper;

public class MainActivity extends AppCompatActivity {

    // Declare variables
    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set widgets
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
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
}

package com.rodrigues.pedroschwarz.awesomeplaces.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rodrigues.pedroschwarz.awesomeplaces.R;

public class LoginActivity extends AppCompatActivity {

    private Button logRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logRegBtn = findViewById(R.id.log_reg_btn);

        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}

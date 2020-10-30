package com.example.mostafa.talents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void user(View view) { startActivity(new Intent(splash.this,MainActivity.class));
    }

    public void scout(View view) {
       startActivity(new Intent(splash.this,scout.class));
    }
}

package com.example.restservice_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {

    CardView card1, card2, card3, card4, card5, card6, card7, card8, card9;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_landing);

        floatingActionButton = findViewById(R.id.cart);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              OpenHome();
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Coming Soon Stay on Hope!", Toast.LENGTH_SHORT).show();
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Coming Soon Stay on Hope!", Toast.LENGTH_SHORT).show();
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Coming Soon Stay on Hope!", Toast.LENGTH_SHORT).show();
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Coming Soon Stay on Hope!", Toast.LENGTH_SHORT).show();
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LandingActivity.this, CartActivity.class);
                startActivity(intent1);
            }
        });

    }

    private void OpenHome(){
        Intent intent = new Intent(LandingActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}

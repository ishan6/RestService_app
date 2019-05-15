package com.example.restservice_app;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //screen to full size
         Window g = getWindow();
         g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL);

        setContentView(R.layout.activity_main);

        //splash screen control
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);


    }
}

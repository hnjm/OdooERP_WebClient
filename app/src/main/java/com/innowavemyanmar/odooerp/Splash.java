package com.innowavemyanmar.odooerp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        final Button retry = findViewById(R.id.retry);

        doAnimation();

        if (isNetworkConnected()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            },1500);
        }
        else {
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
            retry.setVisibility(View.VISIBLE);
        }


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                retry.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void doAnimation() {
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
        logo.setAnimation(zoom);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo()!=null;
    }

}
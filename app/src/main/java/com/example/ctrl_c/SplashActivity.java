package com.example.ctrl_c;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(1500);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}


package com.ss.video.rtc.demo.quickstart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ss.rtc.demo.quickstart.R;

public class CoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);
        ActionBar actionBar = getSupportActionBar();
        if(null!=actionBar){
            actionBar.hide();
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                try{
                    Thread.sleep(5000);
                    Intent intent = new Intent(CoverActivity.this,LoginActivity.class);
                    startActivity(intent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
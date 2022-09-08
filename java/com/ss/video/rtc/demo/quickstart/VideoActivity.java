package com.ss.video.rtc.demo.quickstart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.VideoView;

import com.ss.rtc.demo.quickstart.R;

public class VideoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        int mark = intent.getIntExtra("isWeb",0);
        VideoView video = findViewById(R.id.final_video);
        WebView webView = (WebView) findViewById(R.id.web_view);
        if(mark == 0){
            webView.setVisibility(View.GONE);
            String path = intent.getStringExtra("Uri");
            Log.d("HHHH", path);
            video.setVideoPath(path);
            video.start();
        }else
        {
            video.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            String url = intent.getStringExtra("Uri");
            webView.loadUrl(url);
        }


// 向系统发起屏幕共享的权限请求

    }



}
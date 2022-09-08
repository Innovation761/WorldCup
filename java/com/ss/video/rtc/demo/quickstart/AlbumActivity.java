package com.ss.video.rtc.demo.quickstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ss.rtc.demo.quickstart.R;

import java.util.ArrayList;
import java.util.List;


public class AlbumActivity extends AppCompatActivity {
    private final static int CODE_REQUEST_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CODE_REQUEST_PERMISSION);
        }

        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor =
                contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null, null, null,
                        null);
        if (cursor == null){
            return;
        }
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()){
//            StringBuilder sbLog = new StringBuilder();
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
//            sbLog.append("id:").append(id).append(";");
//            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//            sbLog.append("title:").append(title).append(";");
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//            sbLog.append("uri:").append(album).append(";");
            list.add(uri);

        }
        MyAdapter adapter = new MyAdapter(list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Button web = (Button) findViewById(R.id.web);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumActivity.this, VideoActivity.class);
                intent.putExtra("Uri",((EditText) findViewById(R.id.input_web)).getText().toString());
                intent.putExtra("isWeb",1);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // ⽤户已授权
                Log.d("TAG", "onRequestPermissionsResult: true");
            } else {
                Log.d("TAG","onRequestPermissionsResult: false");
                // ⽤户拒绝了授权
            }
        }
    }
}
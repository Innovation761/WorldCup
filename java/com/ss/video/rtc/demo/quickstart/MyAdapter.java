package com.ss.video.rtc.demo.quickstart;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.rtc.demo.quickstart.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    private List<String> datas = null;


    static class myViewHolder extends RecyclerView.ViewHolder {
        //VideoView videoView;
        ImageView imageView;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            //videoView = itemView.findViewById(R.id.video_item);
            imageView = itemView.findViewById(R.id.image_item);
        }

    }

    public MyAdapter(List<String> data){
        this.datas = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        myViewHolder viewHolder = new myViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.video_view,parent,false));



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder,int position){
        String uriStr = datas.get(position);
        Log.d("TAG1", uriStr);
        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        mmr.setDataSource(uriStr);
        Bitmap bitmap = mmr.getFrameAtTime(0);
        mmr.release();
        viewHolder.imageView.setImageBitmap(bitmap);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(viewHolder.itemView.getContext(), VideoActivity.class);
                intent.putExtra("Uri",uriStr);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

//        viewHolder.videoView.setVideoPath(uriStr);
//        //viewHolder.videoView.start();
//        viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                Log.i("tag222", "--------------视频准备完毕,可以进行播放.......");
//            }
//        });
//        /**
//         * 视频播放完成时回调
//         */
//        viewHolder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.i("tag222", "------------------视频播放完毕..........");
//                /**播放完成时，再次循环播放*/
//                //viewHolder.videoView.start();
//            }
//        });
//
//
//        viewHolder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.i("tag222", "---------------------视频播放失败...........");
//                return false;
//            }
//        });


    }


    @Override
    public int getItemCount(){return datas.size();}




}

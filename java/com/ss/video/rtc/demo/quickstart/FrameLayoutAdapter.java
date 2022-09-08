package com.ss.video.rtc.demo.quickstart;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.rtc.demo.quickstart.R;

import java.util.List;

public class FrameLayoutAdapter extends RecyclerView.Adapter<FrameLayoutAdapter.ViewHolder>{
    private List<String> uids;
    
    private static final String TAG = "Adapter";

    static class ViewHolder extends RecyclerView.ViewHolder{
        View v;
        FrameLayout container;
        public ViewHolder(View view){
            super(view);
            v = view;
            container = v.findViewById(R.id.container);
        }
    }

    public FrameLayoutAdapter(List<String> uids){
        this.uids = uids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uid = uids.get(position);
        RTCRoomActivity parent = (RTCRoomActivity) holder.v.getContext();

//        if(uid != "0"){
//            parent.setRemoteRenderView(uid, holder.container);
//        }else{
//            Log.i(TAG, "onBindViewHolder: 00000");
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            holder.container.removeAllViews();
//            holder.container.addView(default_head, params);
//        }
        parent.setRemoteRenderView(uid, holder.container);
    }

    @Override
    public int getItemCount(){
        return uids.size();
    }

}

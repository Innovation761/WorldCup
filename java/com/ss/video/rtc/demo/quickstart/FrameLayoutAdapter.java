package com.ss.video.rtc.demo.quickstart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.rtc.demo.quickstart.R;

import java.util.List;

public class FrameLayoutAdapter extends RecyclerView.Adapter<FrameLayoutAdapter.ViewHolder>{
    private List<String> uids;
    private String room_id;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View v;
        FrameLayout container;
        public ViewHolder(View view){
            super(view);
            v = view;
            container = v.findViewById(R.id.container);
        }
    }

    public FrameLayoutAdapter(List<String> uids, String room_id){
        this.uids = uids;
        this.room_id = room_id;
    }

    @NonNull
    @Override
    public FrameLayoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FrameLayoutAdapter.ViewHolder holder, int position) {
        String uid = uids.get(position);
        RTCRoomActivity parent = (RTCRoomActivity) holder.v.getContext();
        parent.setRemoteRenderView(room_id, uid, holder.container);
    }

    @Override
    public int getItemCount(){
        return uids.size();
    }

}

//package com.ss.video.rtc.demo.quickstart;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ss.rtc.demo.quickstart.R;
//
//import java.util.List;
//
//public class FrameLayoutAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{
//    private List<String> uids;
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        View v;
//        TextView name;
//        public ViewHolder(View view){
//            super(view);
//            v = view;
//            name = view.findViewById(R.id.container_recycler_view);
//        }
//    }
//
//    public FrameLayoutAdapter(List<String> uids){
//        this.uids = uids;
//    }
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.container_item,parent,false);
////        container_item.xml还未建立
//        final ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position){
//
//    }
//
//    @Override
//    public int getItemCount(){
//        return uids.size();
//    }
//
//}

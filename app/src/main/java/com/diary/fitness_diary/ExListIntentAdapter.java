package com.diary.fitness_diary;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

import java.util.List;

public class ExListIntentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<ExListIntentVO>list;
    OnItemClickListener listener;
    boolean colorCheck;
    public ExListIntentAdapter(List<ExListIntentVO> list, OnItemClickListener listener,boolean colorCheck) {
        this.listener=listener;
        this.list=list;
        this.colorCheck=colorCheck;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_1,parent,false);
        return new ExListIntentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        ExListIntentHolder driveHolder=(ExListIntentHolder) holder;

        driveHolder.imageView.setImageResource(list.get(position).getId());
        String s=list.get(position).getName().toString();
        driveHolder.textView.setText(s);
        if(list.get(position).isStar()){
            Log.d("isStarCheck",list.get(position).getName());
            driveHolder.ex_star.setColorFilter(Color.parseColor("#FF1493"));
        }




        //
        driveHolder.layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(s);
            }
        });
        //즐찾 클릭시
        driveHolder.ex_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (listener.onStarClick(s)){
                   //즐찾 클릭
                   driveHolder.ex_star.setColorFilter(Color.parseColor("#FF1493"));
               }else{
                  //즐찾 해제
                   driveHolder.ex_star.setColorFilter(Color.parseColor("#3c3c3c"));
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String data);
        boolean onStarClick(String name);
    }
}

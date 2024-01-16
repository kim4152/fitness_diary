package com.diary.fitness_diary.list;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.R;

import java.util.ArrayList;

public class DriveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static ArrayList<ListFragment1.ItemVO> datas;
    DBHelper helper;
    SQLiteDatabase db;
    OnStarClick onStarClick;
    public DriveAdapter(ArrayList<ListFragment1.ItemVO> datas,OnStarClick onStarClick) {
        this.datas=datas;
        this.onStarClick = onStarClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ListFragment1.ItemVO.TYPE_HEADER){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header,parent,false);
            return new DriveHeaderHolder(view);
        }else{
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item,parent,false);
            return new DriveHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListFragment1.ItemVO itemVO=datas.get(position);
        if(itemVO.getType()== ListFragment1.ItemVO.TYPE_HEADER){
            DriveHeaderHolder driveHeaderHolder=(DriveHeaderHolder) holder;
            ListFragment1.HeaderItem headerItem=(ListFragment1.HeaderItem) itemVO;
            driveHeaderHolder.textView.setText(headerItem.headerTitle);
        }else{
            DriveHolder driveHolder=(DriveHolder) holder;
            ListFragment1.DataItem dataItem=(ListFragment1.DataItem) itemVO;
            driveHolder.textView.setText(dataItem.routineName);
            if (dataItem.star=="true"||dataItem.star.equals("true")){
                driveHolder.imageButton.setColorFilter(Color.parseColor("#FF1493"));
            }

            //즐찾 클릭시
            driveHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s="";
                    helper=new DBHelper(v.getContext());
                    db=helper.getWritableDatabase();

                    String name=driveHolder.textView.getText().toString();
                    Cursor  cursor=db.rawQuery("select * from routine where routineName=? ",new String[]{name});
                    while (cursor.moveToNext()){
                        s=cursor.getString(2);
                    }
                    s.trim();
                    if(s.equals("false")||s=="false"){
                        db.execSQL("update routine set star=? where routineName=?",new String[]{"true",name});
                        //색 변경
                        driveHolder.imageButton.setColorFilter(Color.parseColor("#FF1493"));
                    }else{
                        db.execSQL("update routine set star=? where routineName=?",new String[]{"false",name});
                        driveHolder.imageButton.setColorFilter(Color.parseColor("#3c3c3c"));
                    }

                    /////////////////////////////////////////////////
                    db.close();

                    //onStarClick.onstartClick();

                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    public interface OnStarClick {
        void onstartClick();
    }


}

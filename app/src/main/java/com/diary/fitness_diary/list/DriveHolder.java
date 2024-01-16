package com.diary.fitness_diary.list;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;


public class DriveHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public ConstraintLayout constraintLayout;
    public ImageButton imageButton;
    public RecyclerView recyclerView;


    public DriveHolder(View root){
        super(root);
        textView=root.findViewById(R.id.custom_item_title);
        constraintLayout=root.findViewById(R.id.list_constraint);
        imageButton=root.findViewById(R.id.lsititemstar);
        recyclerView=root.findViewById(R.id.listListView);
    }


}

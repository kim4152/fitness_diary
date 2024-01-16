package com.diary.fitness_diary.list.gallery;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

public class galleryHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;
    ImageButton imageButton;
    RecyclerView recyclerView;
    public galleryHolder(View root) {
        super(root);
        imageView=root.findViewById(R.id.imageView1);
        textView=root.findViewById(R.id.galleryHeader);
        imageButton=root.findViewById(R.id.galleryMoreVert);
        recyclerView=root.findViewById(R.id.galleryRecyclerView);
    }
}

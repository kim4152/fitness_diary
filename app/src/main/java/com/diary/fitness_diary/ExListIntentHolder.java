package com.diary.fitness_diary;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

public class ExListIntentHolder extends RecyclerView.ViewHolder{
    TextView textView;
    ImageView imageView,ex_star;
    Layer layer;
    public ExListIntentHolder(@NonNull View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.textView2);
        imageView=itemView.findViewById(R.id.imageView2);
        layer=itemView.findViewById(R.id.layer2);
        ex_star=itemView.findViewById(R.id.ex_star);
    }
}

package com.diary.fitness_diary.home;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

public class subHolder extends RecyclerView.ViewHolder{
    TextView setEdit;
    EditText weightEdit,timeEdit;

    public subHolder(@NonNull View itemView) {
        super(itemView);
        setEdit=itemView.findViewById(R.id.homeSubItemSet);
        weightEdit=itemView.findViewById(R.id.homeSubItemWeight);
        timeEdit=itemView.findViewById(R.id.homeSubItemTime);
    }
}

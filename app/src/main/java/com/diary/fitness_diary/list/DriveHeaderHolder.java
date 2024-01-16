package com.diary.fitness_diary.list;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.diary.fitness_diary.R;

public class DriveHeaderHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public DriveHeaderHolder(View root) {
        super(root);
        textView= root.findViewById(R.id.item_header);
    }
}

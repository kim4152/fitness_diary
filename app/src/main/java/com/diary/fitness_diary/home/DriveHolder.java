package com.diary.fitness_diary.home;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

import java.util.ArrayList;

public class DriveHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageButton imageButton;
    ConstraintLayout constraintLayout;
    Button deleteButton, addButton;
    RecyclerView subRecyclerView;
    SubAdapter subAdapter;

    public DriveHolder(@NonNull View root,DriveAdapter adapter) {
        super(root);
        textView=root.findViewById(R.id.mainItemTextView);
        imageButton=root.findViewById(R.id.mainItemImageButton);

        constraintLayout=root.findViewById(R.id.mainItemCons);
        deleteButton=root.findViewById(R.id.home_main_item_delete);
        addButton=root.findViewById(R.id.home_main_item_add);

        subRecyclerView=root.findViewById(R.id.home_sub_recyclerVeiw);
        subAdapter=new SubAdapter(new ArrayList<SubItem>(),adapter);

    }

}

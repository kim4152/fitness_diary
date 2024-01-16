package com.diary.fitness_diary.list.intent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.diary.fitness_diary.ExListIntent;
import com.diary.fitness_diary.R;

import java.util.ArrayList;

public class IntentDriverAdapter extends RecyclerView.Adapter<IntentDriverHolder> {
    ArrayList<IntentDriveVO> datas;
    Activity context;
    public IntentDriverAdapter(ArrayList<IntentDriveVO> datas, Activity context) {
        this.datas=datas;
        this.context=context;
    }

    @NonNull
    @Override
    public IntentDriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_intent_custom_item,parent,false);
        return new IntentDriverHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull IntentDriverHolder holder, @SuppressLint("RecyclerView") int position) {
        //holder.editText.setText(datas.get(position).toString());
        //datas.set(position,holder.editText.getText().toString());

        holder.editText.setText(datas.get(position).getExerciseName().toString());

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                datas.get(position).setExerciseName(String.valueOf(s));
            }
        });

        holder.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExListIntent.class);
                intent.putExtra("position",position+"");
                context.startActivityForResult(intent,19);

            }
        });

    }
    public void itemAdd(){
        datas.add(new IntentDriveVO(new String()));
        notifyItemInserted(datas.size()-1);
    }
    public ArrayList<String> save(ArrayList<String> saveList){
        for(IntentDriveVO vo : datas){
            if(!vo.getExerciseName().equals("")){
                saveList.add(vo.getExerciseName());
            }
        }
        return saveList;
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

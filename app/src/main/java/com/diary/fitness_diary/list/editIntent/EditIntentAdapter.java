package com.diary.fitness_diary.list.editIntent;

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

public class EditIntentAdapter extends RecyclerView.Adapter<EidtHolder>{
    ArrayList<EditIntentVO> datas;
    Activity context;
    public EditIntentAdapter(ArrayList<EditIntentVO> datas, Activity context) {
        this.datas=datas;
        this.context=context;
    }

    //저장버튼 이벤트 RoutineIntent에서 호출한다.
    public ArrayList<String> save(ArrayList<String> saveList){

        return saveList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public EidtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_edit_item,parent,false);
        return new EidtHolder(view,this);
    }
    @Override
    public void onBindViewHolder(@NonNull EidtHolder holder, @SuppressLint("RecyclerView") int position) {

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
    @Override
    public int getItemCount() {
        return datas.size();
    }

}

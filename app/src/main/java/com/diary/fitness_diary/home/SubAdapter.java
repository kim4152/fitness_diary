package com.diary.fitness_diary.home;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

import java.util.List;

public class SubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

     List<SubItem> subList;
    DriveAdapter driveAdapter;
    public SubAdapter(List subList,DriveAdapter driveAdapter) {
        this.subList = subList;
        this.driveAdapter=driveAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub_item,parent,false);
        return new subHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        subHolder subHolder = (subHolder) holder;
        subHolder.setEdit.setText(position+1+"set");
        subList.get(position).setSet(position+1+"set");

        String s= subList.get(position).getWeight()+"";
        if (!s.equals("0.0")){
            subHolder.weightEdit.setText(subList.get(position).getWeight()+"");
        }
        String s2 = subList.get(position).getTime()+"";
        if (!s2.equals("0")){
            subHolder.timeEdit.setText(subList.get(position).getTime()+"");
        }




//////////////////////////////////////////////////////////////////////////////////
        subHolder.weightEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    subList.get(position).setWeight(Float.parseFloat(subHolder.weightEdit.getText().toString().trim()));
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
/////////////////////////////////////////////////////////////////////////////////
        subHolder.timeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    subList.get(position).setTime(Integer.parseInt(subHolder.timeEdit.getText().toString().trim()));
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return subList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void add(List list){
        this.subList=list;
        subList.add(new SubItem());
        notifyItemInserted(subList.size()-1);
    }
    public void delete(List list){
        this.subList=list;
        subList.remove(subList.size()-1);
        notifyItemRemoved(subList.size()-1);
    }
}


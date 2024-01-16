package com.diary.fitness_diary.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.calendar.dayDesign.CalendarVO;
import com.diary.fitness_diary.R;

import java.util.List;

public class ModalSubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CalendarVO> list;
    String name;
    float totalWeight;
    float totalTime;

    public ModalSubAdapter(List<CalendarVO> list, String name) {
        this.list = list;
        this.name = name;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_modal_sub_item,parent,false);
        return new DriveHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DriveHolder driveHolder = (DriveHolder) holder;

        driveHolder.set.setText(list.get(position).getSet());
        driveHolder.weight.setText(list.get(position).getWeight());
        driveHolder.time.setText(list.get(position).getTime());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DriveHolder extends RecyclerView.ViewHolder{
        TextView set,weight,time;
        public DriveHolder(@NonNull View itemView, ModalSubAdapter subAdapter) {
            super(itemView);
            set=itemView.findViewById(R.id.modalSubItemSet);
            weight=itemView.findViewById(R.id.modalSubItemWeight);
            time=itemView.findViewById(R.id.modalSubItemTime);
        }
    }

}

package com.diary.fitness_diary.calendar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.calendar.dayDesign.CalendarVO;
import com.diary.fitness_diary.R;

import java.util.ArrayList;
import java.util.List;

public class DriveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<CalendarVO>list;
    List<String> mainList;
    List<CalendarVO> newList;

    public DriveAdapter(List<CalendarVO> list, List<String> mainList) {
        Log.d("setAdapter","true"+list.size());
        this.list = list;
        this.mainList = mainList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_main_item,parent,false);
        return new DriveHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DriveHolder driveHolder = (DriveHolder) holder;
        String name = mainList.get(position).toString();
        driveHolder.textView.setText(name);

        driveHolder.recyclerView.setLayoutManager(new LinearLayoutManager(
                driveHolder.recyclerView.getContext(),
                LinearLayoutManager.VERTICAL,false));
        newList=new ArrayList<>();
        for(int i=0; i<list.size();i++){
            if(list.get(i).getRoutineName().equals(name))
                newList.add(list.get(i));
        }

        driveHolder.recyclerView.setAdapter(new SubAdapter(newList,name));

    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public class DriveHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RecyclerView recyclerView;

        public DriveHolder(@NonNull View itemView, DriveAdapter driveAdapter) {
            super(itemView);
            textView=itemView.findViewById(R.id.calendarView_exerciseName);
            recyclerView=itemView.findViewById(R.id.calendarView_subRecyclerView);
        }
    }
}

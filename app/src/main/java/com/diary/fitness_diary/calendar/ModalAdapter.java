package com.diary.fitness_diary.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;
import com.diary.fitness_diary.calendar.dayDesign.CalendarVO;

import java.util.ArrayList;
import java.util.List;


public class ModalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<CalendarVO> list;
    List<String> mainList;
    List<CalendarVO> newList;
    float totalW,sum;
    int totalT;

    public ModalAdapter(List<CalendarVO> list, List<String> mainList) {
        this.list = list;
        this.mainList = mainList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_modal_main_item,parent,false);
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
        totalW=0;
        totalT=0;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getRoutineName().equals(name)) {
                newList.add(list.get(i));
                totalW += Float.parseFloat(list.get(i).getWeight().substring(0, list.get(i).getWeight().length() - 2));
                totalT += Integer.parseInt(list.get(i).getTime().substring(0, list.get(i).getTime().length() - 1));
                driveHolder.totalWeight.setText(totalW + "kg");
                driveHolder.totalSet.setText(totalT + "회");
                sum=totalT+totalW;
            }
        }
        ModalSubAdapter modalSubAdapter = new ModalSubAdapter(newList,name);
        driveHolder.recyclerView.setAdapter(modalSubAdapter);







    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public class DriveHolder extends RecyclerView.ViewHolder{
        TextView textView,totalWeight,totalSet;
        RecyclerView recyclerView;

        public DriveHolder(@NonNull View itemView, ModalAdapter driveAdapter) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.modalItemTextView);
            recyclerView=itemView.findViewById(R.id.modal_sub_recyclerVeiw);
            totalSet=itemView.findViewById(R.id.totalSet);
            totalWeight=itemView.findViewById(R.id.totalWeight);
        }
    }
}


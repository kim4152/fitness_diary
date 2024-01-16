package com.diary.fitness_diary.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;

import java.util.List;

public class DriveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<MainItem> list;
    DriveHolder holder;
    FragmentActivity context;
    RecyclerView recyclerView;
    public DriveAdapter(List<MainItem> list, FragmentActivity context,RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_main_item,parent,false);
        return new DriveHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MainItem mainItem = list.get(position);
        DriveHolder driveHolder = (DriveHolder) holder;
        this.holder=driveHolder;

        driveHolder.textView.setText(mainItem.getRoutineName().toString());

        driveHolder.constraintLayout.setVisibility(View.GONE);


        driveHolder.subRecyclerView.setLayoutManager(new LinearLayoutManager(driveHolder.subRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        SubAdapter subAdapter = new SubAdapter(mainItem.subItemList,this);
        driveHolder.subRecyclerView.setAdapter(subAdapter);

        //아이템 추가
        driveHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driveHolder.subRecyclerView.setLayoutManager(new LinearLayoutManager(
                        driveHolder.subRecyclerView.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                ));
                driveHolder.subAdapter.add(mainItem.subItemList);
                //driveHolder.subAdapter.notifyItemInserted(driveHolder.subAdapter.subList.size() - 1);
                //recyclerView.scrollToPosition(driveHolder.subAdapter.getItemCount()-1);
            }
        });
        //아이템 삭제
        driveHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainItem.subItemList.size()<1){
                }else{
                    driveHolder.subRecyclerView.setLayoutManager(new LinearLayoutManager(
                            driveHolder.subRecyclerView.getContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                    ));
                    driveHolder.subAdapter.delete(mainItem.subItemList);
                }
            }
        });
        //최소화 최대화
        driveHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=driveHolder.constraintLayout.getVisibility();
                if(i==0){
                    driveHolder.constraintLayout.setVisibility(View.GONE);
                    driveHolder.imageButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }else{
                    driveHolder.constraintLayout.setVisibility(View.VISIBLE);
                    driveHolder.imageButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }

            }
        });

        //키보드 내림
        driveHolder.subRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(driveHolder.subRecyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                return false;
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<MainItem> save(){
        return list;
    }
}

package com.diary.fitness_diary.calendar;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.calendar.dayDesign.CalendarVO;
import com.diary.fitness_diary.R;

import java.util.ArrayList;
import java.util.List;

public class MainDialog extends Dialog {
    String date;
    RecyclerView recyclerView;
    public MainDialog(@NonNull Context context,String date) {
        super(context);
        this.date=date;
        setContentView(R.layout.calendar_modal);
        recyclerView=findViewById(R.id.calendarModalVIewRecyclerView);
        loadRoutine();
    }

    public void loadRoutine(){
        //DB에서 운동기록 가져오기
        List<CalendarVO> list = new ArrayList<>();
        //ConstraintLayout constraintLayout = binding.bottomCons;
        DBHelper dbHelper= new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from details where date=?",new String[]{date});


        try{
                /////////////////////////////////////////////////////////////////////
                while (cursor.moveToNext()){
                    CalendarVO calendarVO = new CalendarVO();
                    calendarVO.setDateRoutineExercise(cursor.getString(0).toString());
                    calendarVO.setSet(cursor.getString(1).toString());
                    calendarVO.setWeight(cursor.getString(2).toString());
                    calendarVO.setTime(cursor.getString(3).toString());
                    calendarVO.setRoutineName(cursor.getString(4).toString());
                    calendarVO.setDate(cursor.getString(5).toString());
                    list.add(calendarVO);


                }

                Cursor cursor1=db.rawQuery("select distinct routineName from details where date=?",new String[]{date});
                List<String> main=new ArrayList();
                while (cursor1.moveToNext()){
                    main.add(cursor1.getString(0).toString());
                }
                //리사이클러뷰 설정
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new ModalAdapter(list,main));
                ////////////////////////////////////////////////////////////////////
        }catch (Exception e){
        }

    }
}

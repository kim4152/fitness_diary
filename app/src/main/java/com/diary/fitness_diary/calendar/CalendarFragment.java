package com.diary.fitness_diary.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.MainActivity;
import com.diary.fitness_diary.calendar.dayDesign.CalendarVO;
import com.diary.fitness_diary.calendar.dayDesign.Saturday;
import com.diary.fitness_diary.calendar.dayDesign.Thursday;
import com.diary.fitness_diary.calendar.dayDesign.Tuesday;
import com.diary.fitness_diary.calendar.dayDesign.Wednesday;
import com.diary.fitness_diary.calendar.dayDesign.Friday;
import com.diary.fitness_diary.calendar.dayDesign.Monday;
import com.diary.fitness_diary.calendar.dayDesign.Sunday;

import com.diary.fitness_diary.databinding.CalendarFragmentBinding;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    static MaterialCalendarView materialCalendarView;
    CalendarFragmentBinding binding;
    String mTime,todayDate;
    RecyclerView recyclerView;
    ConstraintLayout constraintLayout;
    TextView textTotal;
    float sum;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=CalendarFragmentBinding.inflate(inflater,container,false);
        binding.setData(this);
        textTotal = binding.totalSum;
        materialCalendarView = binding.calendarView;
        recyclerView=binding.calendarVIewRecyclerView;
        //오늘 날짜
        todayDate=new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
        loadRoutine(todayDate);

        //날짜 선택 디자인
        materialCalendarView.addDecorator(new MySelectorDecorator(getActivity()));

        //오늘 날짜 표시
        materialCalendarView.addDecorators(new OneDayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                        String year =  String.valueOf(date.getYear());
                        String month = String.valueOf(date.getMonth()+1);
                        if(month.length()<2) month = "0"+String.valueOf(date.getMonth()+1);
                        String day = String.valueOf(date.getDay());
                        if(day.length()<2) day = "0"+String.valueOf(date.getDay());
                        mTime = year+month+day;

                        loadRoutine(mTime);//리사이클러뷰 업뎃


            }
        });

        makDot(getContext());


        return  binding.getRoot();
    }



    public void makDot(Context context){
        Handler handler = new Handler();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            materialCalendarView.addDecorators(new Monday(),new Tuesday(),new Wednesday(),new Thursday(),new Friday(),new Saturday(),new Sunday());
                        }
                    });




                    //점찍기
                    Log.d("Hihi",context.toString()+"");
                    DBHelper dbHelper = new DBHelper(context);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select distinct date,realRoutineName from details ",null);
                    while (cursor.moveToNext()){
                        String date=cursor.getString(0).toString();
                        String name= cursor.getString(1).toString().substring(8);
                        if (name.equals("default")||name.equals("")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    CalendarDot(date,"#FF000000");
                                }
                            });
                        }else {
                            //루틴명이 default가 아닐때

                            SQLiteDatabase db2 = dbHelper.getReadableDatabase();
                            Cursor cursor2 = db2.rawQuery("select color from routine where routineName=?",new String[]{name});
                            while (cursor2.moveToNext() ){

                                String dotColor =cursor2.getString(0).toString();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        CalendarDot(date,dotColor);
                                    }
                                });
                            }

                        }

                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //월일요일구하기
    public String getDay(String s){
        final String[] re = {""};
                try {
                    Date date = new SimpleDateFormat("yyyyMMdd").parse(s);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    String str_week = "";
                    switch (dayWeek) {
                        case 1:
                            str_week = "일";
                            break;
                        case 2:
                            str_week = "월";
                            break;
                        case 3:
                            str_week = "화";
                            break;
                        case 4:
                            str_week = "수";
                            break;
                        case 5:
                            str_week = "목";
                            break;
                        case 6:
                            str_week = "금";
                            break;
                        case 7:
                            str_week = "토";
                            break;
                    }
                    String month = s.substring(4, 6);
                    if(month.substring(0,1).equals("0"))    month=month.substring(1);

                    String day = s.substring(6);
                    if(day.substring(0,1).equals("0")) day=day.substring(1);
                    re[0] = month + "월 " + day + "일 " + str_week;
                }catch (Exception e){}


        return re[0];
    }
    //운동일지 불러오기
    public void loadRoutine(String date){
                //DB에서 운동기록 가져오기
                List<CalendarVO> list = new ArrayList<>();
                List<String> ls = new ArrayList<>();
                constraintLayout = binding.bottomCons;
                DBHelper dbHelper= new DBHelper(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("select * from details where date=?",new String[]{date});


                        TextView textView = binding.dateEx;
                        textView.setText(getDay(date));//요일 구하기

                try{
                    if(cursor.getCount()>=1){

                        /////////////////////////////////////////////////////////////////////
                        while (cursor.moveToNext()){
                            CalendarVO calendarVO = new CalendarVO();
                            calendarVO.setDateRoutineExercise(cursor.getString(0).toString());
                            calendarVO.setSet(cursor.getString(1).toString());
                            String s=cursor.getString(2);
                            calendarVO.setWeight(s.toString());
                            String r = cursor.getString(3);
                            calendarVO.setTime(r);

                            calendarVO.setRoutineName(cursor.getString(4).toString());
                            calendarVO.setDate(cursor.getString(5).toString());
                            list.add(calendarVO);
                            ls.add((Float.parseFloat(s.substring(0,s.length()-2))*Float.parseFloat(r.substring(0,r.length()-1)))+"");
                        }

                        Cursor cursor1=db.rawQuery("select distinct routineName from details where date=?",new String[]{date});
                        List<String> main=new ArrayList();
                        while (cursor1.moveToNext()){
                            main.add(cursor1.getString(0).toString());
                        }
                        db.close();


                                //리사이클러뷰 설정
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new DriveAdapter(list,main));
                                constraintLayout.setVisibility(View.VISIBLE);


                        ////////////////////////////////////////////////////////////////////
                    }else{

                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new DriveAdapter(new ArrayList<>(),new ArrayList<>()));
                                constraintLayout.setVisibility(View.INVISIBLE);

                    }
                }catch (Exception e){
                }
                for(String s : ls){
                    try {
                        sum += Float.parseFloat(s.substring(0, s.length() - 2));
                    }catch (Exception e){
                        sum=0;
                    }
                }


                        textTotal.setText("총 중량: "+sum+"kg");


                ls.clear();
                sum=0;


    }
    //String to Date
    public void CalendarDot(String date,String color){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor(color), Collections.singleton(CalendarDay.from(cal))));
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("lli","pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        //makDot(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lli","resume");
    }

    //삭제버튼
    public void onClickDeleteButton(View view){
        constraintLayout.setVisibility(View.INVISIBLE);
        boolean removeCheck[]= {false};
        Snackbar.make(recyclerView,"실행 취소",Snackbar.LENGTH_LONG)
                .setAction("실행취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeCheck[0]=true;
                        constraintLayout.setVisibility(View.VISIBLE);
                    }
                })
                .addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (!removeCheck[0]) {
                            constraintLayout.setVisibility(View.VISIBLE);
                            DBHelper dbHelper = new DBHelper(getActivity());
                            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//
                            if (mTime==null) {
                                sqLiteDatabase.execSQL("delete  from details where date=?", new String[]{todayDate});
                                materialCalendarView.removeDecorators();
                                materialCalendarView.addDecorator(new MySelectorDecorator(getActivity()));
                                makDot(getContext());
                                materialCalendarView.addDecorators(new OneDayDecorator());
                                loadRoutine(todayDate);
                            } else {
                                sqLiteDatabase.execSQL("delete  from details where date=?", new String[]{mTime});

                                materialCalendarView.removeDecorators();
                                materialCalendarView.addDecorator(new MySelectorDecorator(getActivity()));
                                makDot(getContext());
                                materialCalendarView.addDecorators(new OneDayDecorator());
                                loadRoutine(mTime);
                            }
                            sqLiteDatabase.close();
                        }
                    }
                })
                .show();
    }



    //자세히 버튼
    public void onClickDetailButton(View view){
        if(mTime==null) {
            MainDialog mainDialog = new MainDialog(getActivity(), todayDate);
            mainDialog.show();
        }else{
            MainDialog mainDialog = new MainDialog(getActivity(),mTime);
            mainDialog.show();
        }
    }
    //edit button
    public void onClickEditButton(View view){

            // MainActivity에 어떤 동작이든 수행하도록 호출
            if (getActivity() instanceof MainActivity) {
                if (mTime==null){
                    ((MainActivity) getActivity()).fragmentMove(todayDate);
                }else{
                    ((MainActivity) getActivity()).fragmentMove(mTime);
                }

            }

    }
    // 해당 월의 첫째 날 구하기
    private static Calendar getFirstDayOfMonth(Calendar calendar) {
        Calendar firstDayOfMonth = (Calendar) calendar.clone();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1); // 현재 월의 첫째 날로 설정
        return firstDayOfMonth;
    }

    // 해당 월의 마지막 날 구하기
    private static Calendar getLastDayOfMonth(Calendar calendar) {
        Calendar lastDayOfMonth = (Calendar) calendar.clone();
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)); // 현재 월의 마지막 날로 설정
        return lastDayOfMonth;
    }



}

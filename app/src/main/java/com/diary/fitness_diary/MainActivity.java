package com.diary.fitness_diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.diary.fitness_diary.home.HomeFragment;
import com.diary.fitness_diary.home.TimerFragment;
import com.diary.fitness_diary.R;
import com.diary.fitness_diary.calendar.CalendarFragment;
import com.diary.fitness_diary.databinding.ActivityMainBinding;
import com.diary.fitness_diary.list.ListFragment1;
import com.diary.fitness_diary.databinding.CalendarFragmentBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 5;
    public static String EDITDATE="";
    ActivityMainBinding binding;
    CalendarFragmentBinding binding2;
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    TabLayout tabLayout;
    MaterialCalendarView calendarView;
    CalendarFragment myFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        //binding2= DataBindingUtil.setContentView(this, R.layout.calendar_fragment);
        binding2=CalendarFragmentBinding.inflate(getLayoutInflater());
        binding.setMain(this);

        relativeLayout=binding.lab2Container;
        viewPager=binding.lab2ViewPager;
        tabLayout=binding.lab2Tabs;

        calendarView =binding2.calendarView;
        //Log.d("hihi",calendarView.toString());

        setViewPager(1);


    }
    public void dotSet(String date){
        myFragment = (CalendarFragment) new MyPagerAdapter(getSupportFragmentManager()).getItem(0);

        if (myFragment != null) {
            dbColorCheck(date);
        }
    }
    //db에서 색 검색
    private void dbColorCheck(String date){

        Handler handler = new Handler();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select distinct date,realRoutineName from details where date=? ",new String[]{date});
                    while (cursor.moveToNext()){
                        String date=cursor.getString(0).toString();
                        String name= cursor.getString(1).toString().substring(8);
                        if (name.equals("default")||name.equals("")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    myFragment.CalendarDot(date,"#FF000000");
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
                                        myFragment.CalendarDot(date,dotColor);
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



    public void fragmentMove(String date) {
        EDITDATE=date;
        setViewPager(1);
    }
    private void setViewPager(int i){
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyPagerAdapter (getSupportFragmentManager()));
        viewPager.setCurrentItem(i,true);// 2페이지 시작
        tabLayout=binding.lab2Tabs;
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_calendar_month_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_fitness_center_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_edit_note_24);

    }



    //권한에 대한 응답이 있을때 작동하는 함수


    class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();
        private String title[]=new String[]{"운동기록","운동시작","루틴만들기"};



        public MyPagerAdapter(FragmentManager fm){
            super(fm);
            fragments.add(new CalendarFragment());
            fragments.add(new HomeFragment());
            fragments.add(new ListFragment1());
        }



        @NonNull
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        @Nullable
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }



    //뒤로가기 이벤트
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    @Override
    public void onBackPressed() { backKeyHandler.onBackPressed();}

    public void onFragmentChange(int index){
        if(index == 0 ){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new TimerFragment()).commit();
            FrameLayout frameLayout = findViewById(R.id.frameLayout);
            frameLayout.bringToFront();
        }
    }
    Fragment fragment;
    public void hideView(Fragment view){
        this.fragment=view;
        getSupportFragmentManager().beginTransaction().hide(view).commit();
    }
    public void showView(){
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }
    public void moveFragment(){

    }


}
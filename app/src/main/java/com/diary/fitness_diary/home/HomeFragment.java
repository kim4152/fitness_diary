package com.diary.fitness_diary.home;

import static com.diary.fitness_diary.MainActivity.EDITDATE;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.fitness_diary.ExListIntent;
import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.MainActivity;
import com.diary.fitness_diary.SharedViewModel;
import com.diary.fitness_diary.calendar.dayDesign.CalendarVO;
import com.diary.fitness_diary.R;
import com.diary.fitness_diary.databinding.HomeFragmentBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment  {
    HomeFragmentBinding binding;
    @NonNull
    int year,month,day;
    String finalRoutineName="";
    String[] routineName;
    HorizontalCalendar horizontalCalendar;
    EditText homeEditText1;
    TextView homeTextViewBehind;
    boolean isStop,isEdit;
    Intent intent;
    RecyclerView recyclerView, subRecyclerView;
    DriveAdapter driveAdapter;
    String saveDate;
    Button imageButton;
    ExtendedFloatingActionButton timerButton,saveButton,uploadButton,addButton;
    FloatingActionButton menuButton;
    View view;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=HomeFragmentBinding.inflate(inflater,container,false);
        binding.setHome(this);
         view = inflater.inflate(R.layout.home_fragment_timer, container, false);
        saveButton=binding.routineFinish;
        menuButton=binding.menuButton;
        timerButton=binding.timer;
        uploadButton=binding.routineSelect;
        addButton=binding.addButton;
        //가로모드 달력
        horizontalCalendar1();
        //default날짜
        saveDate=new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
        //editText사라짐
        recyclerView=binding.homeRecyclerView;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
        recyclerView = binding.homeRecyclerView;
        driveAdapter = new DriveAdapter(new ArrayList<MainItem>(),getActivity(),recyclerView);
        onSwiper(driveAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(driveAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        return binding.getRoot();
    }

    EditText strText;
    //운동 추가
    public void addExercise(){
        {
            hideFloatingbar();
            Dialog dialog=new Dialog(getContext());
            dialog.setContentView(R.layout.home_add_exercise_custom_dialog);
            WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
            params.width=WindowManager.LayoutParams.MATCH_PARENT;
            params.height=WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);//크기 조절
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //여백 없앰
            //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //배경 투명
            dialog.getWindow().setGravity(Gravity.CENTER); //상단 배치
            //dialog.setCancelable(false); //외부 터치 금지
            dialog.show();

            Button buttonOK = dialog.findViewById(R.id.dialogOK11);
            Button buttonNO = dialog.findViewById(R.id.dialogNO21);

            strText=dialog.findViewById(R.id.appCompatTextView2);
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(strText.getText().toString().equals("")){
                        //
                    }else {
                        driveAdapter.list.add(new MainItem(strText.getText().toString(), new ArrayList<>()));
                        driveAdapter.notifyItemInserted(driveAdapter.list.size() - 1);
                        recyclerView.scrollToPosition(driveAdapter.getItemCount()-1);
                        onSwiper(driveAdapter);

                    }
                    dialog.dismiss();
                }
            });
            buttonNO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ExListIntent.class);
                    startActivityForResult(intent,10);
                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==10 || resultCode==-1){
            if (data != null) {
                String replyMessage = data.getStringExtra("reply");
                strText.setText(replyMessage);
            }
        }
    }
    //운동추가
    public void onClickaddEx(View view){
        if (finalRoutineName.equals("")){
            finalRoutineName="default";
        }

        addExercise();
    }
    //저장버튼
    public void onClickSave(View view){
        setDialog();
    }
    //키보드 다운
    public void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null)
        {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //DB저장
    public void insertDB(String dateRoutineExerciseSet, String set1, String weight,String time,
                         String routineName,String date,String realRoutineName){


            DBHelper helper = new DBHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();



                ContentValues contentValues = new ContentValues();

                contentValues.put("date", date);
                contentValues.put("weight", weight);
                contentValues.put("routineName", routineName);
                contentValues.put("set1", set1);
                contentValues.put("time", time);
                contentValues.put("dateRoutineExerciseSet", dateRoutineExerciseSet);
                contentValues.put("realRoutineName", realRoutineName);
                db.insert("details", null, contentValues);

                db.close();

    }
    //루틴 불러오기
    public void routinOnClick(View view) {

        //DB가져오기
        dbSelect();
        if (routineName.length!=0) {
            new AlertDialog.Builder(getContext())

                    .setTitle("루틴 선택")
                    .setSingleChoiceItems(routineName, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalRoutineName = routineName[which];
                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //driveAdapter.list.add(new MainItem(strText.getText().toString(), new ArrayList<>()));
                            //driveAdapter.notifyItemInserted(driveAdapter.list.size() - 1);

                            //driveAdapter = new DriveAdapter(dbExercise(),getActivity());
                            for (Object m : dbExercise()){
                                driveAdapter.list.add((MainItem) m);
                                driveAdapter.notifyItemInserted(driveAdapter.list.size() - 1);
                            }
                           // onSwiper(driveAdapter);

                            driveAdapter.notifyDataSetChanged();
                            //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            //recyclerView.setAdapter(driveAdapter);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }else{
            Toast.makeText(getContext(),"루틴을 먼저 만들어주세요",Toast.LENGTH_SHORT).show();
        }
        hideFloatingbar();
    }
    //가로모드 달력
    private void horizontalCalendar1(){
        //시작 날짜
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH,-1);

        //종료날짜
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH,1);

        //가로 달력 실행
         horizontalCalendar=new HorizontalCalendar.Builder(binding.getRoot(),R.id.horizontalCalendar)
                .range(startDate,endDate)
                .configure()
                    .selectorColor(R.color.gray)
                .end()
                .datesNumberOnScreen(5)
                .build();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(2023, Calendar.DECEMBER, 16); // 특정 날짜로 설정


        //날짜 설정
        year = startDate.get(Calendar.YEAR);
        month=startDate.get(Calendar.MONTH)+1;
        day=startDate.get(Calendar.DAY_OF_MONTH);

        Log.d("dateWant",year+month+day+"");



        //날짜선택 이벤트
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                year = date.get(Calendar.YEAR);
                month=date.get(Calendar.MONTH)+1;
                day=date.get(Calendar.DAY_OF_MONTH);
                String sMonth,sDay;
                if((int)Math.log10(month)+1==1){
                    sMonth="0"+month;
                }else{
                    sMonth=month+"";
                }
                if((int)Math.log10(day)+1==1){
                    sDay="0"+day;
                }else {
                    sDay=day+"";
                }

                String s = year+""+sMonth+""+sDay;
                saveDate=s;

            }
        });
    }
    //DB 루틴명 가져오기
    private void dbSelect(){
        DBHelper dbHelper= new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from routine",null);
        routineName=new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext()){
            routineName[i]=cursor.getString(0).toString();
            i++;
        }
    }
    //DB 운동 가져오기
    private List dbExercise(){
        List<MainItem> list = new ArrayList<MainItem>();
        DBHelper dbHelper= new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from exercise where routineName=?",new String[]{finalRoutineName});
        while (cursor.moveToNext()){
            List<SubItem>subItemList=new ArrayList<>();
            list.add(new MainItem(cursor.getString(0),subItemList));
        }
        return list;
    }

    //스와이퍼
    public void onSwiper(DriveAdapter driveAdapter){
        SwipeController swipeController=new SwipeController(new SwipeController.SwipeControllerActions() {
            @Override
            public void onLeftClicked(int pos) {
                                driveAdapter.list.remove(pos);
                                driveAdapter.notifyItemRemoved(pos);
                                driveAdapter.notifyItemRangeChanged(pos,driveAdapter.list.size()-pos);
            }

            @Override
            public void onRightClicked(int pos) {
                driveAdapter.list.remove(pos);
                driveAdapter.notifyItemRemoved(pos);
                driveAdapter.notifyItemRangeChanged(pos,driveAdapter.list.size()-pos);

            }

            @Override
            public void onReset(int pos) {
                driveAdapter.notifyItemChanged(pos);
            }
        },getResources());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
    }
    //커스텀 다이얼로그
    public void setDialog(){
        hideFloatingbar();
        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.home_custom_dialog);
        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.width=WindowManager.LayoutParams.MATCH_PARENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);//크기 조절
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //여백 없앰
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //배경 투명
        dialog.getWindow().setGravity(Gravity.CENTER); //상단 배치
        //dialog.setCancelable(false); //외부 터치 금지
        dialog.show();

        Button buttonOK = dialog.findViewById(R.id.dialogOK1);
        Button buttonNO = dialog.findViewById(R.id.dialogNO2);


        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MainItem> saveList= driveAdapter.list;

                for(MainItem mainItem : saveList){
                    String dateRoutineExerciseSet,set,weight,time,routineName,realRoutineName;
                    routineName= mainItem.routineName;
                    for (SubItem subItem : mainItem.subItemList){
                        dateRoutineExerciseSet=saveDate+mainItem.routineName+subItem.getSet();
                        set=subItem.getSet();weight=subItem.getWeight()+"kg";time= subItem.getTime()+"회";
                        realRoutineName=saveDate+finalRoutineName;
                        Log.d("gogo","운동명:"+routineName+"|무게:"+weight+"회수:"+time);
                        editDelete();
                        insertDB(dateRoutineExerciseSet,set,weight,time,routineName,saveDate,realRoutineName);
                    }
                }

                driveAdapter = new DriveAdapter(new ArrayList<MainItem>(),getActivity(),recyclerView);
                onSwiper(driveAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(driveAdapter);
                dialog.dismiss();

                //저장시 캘린더에 점 하나 추가
                someEventOccurred();

                Log.d("interCheck","nope");

            }

        });
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    // 어떤 이벤트가 발생했을 때 호출
    private void someEventOccurred() {
        if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).dotSet(saveDate);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void editDelete(){
        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        if (isEdit){

            sqLiteDatabase.execSQL("delete  from details where date=?", new String[]{saveDate});

            isEdit=false;

        }
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM details WHERE date=?",new String[]{saveDate});
        while (cursor.moveToNext()){
            Log.d("hihi",cursor.getString(4));
        }

        sqLiteDatabase.close();

    }


    //타이머 버튼
    public void onClickTimerButton(View view)  {
        MainActivity mainActivity=(MainActivity) getContext();

            if (TimerFragment.timerServiceCheck.equals("true")){
                mainActivity.showView();
            }else{
                mainActivity.onFragmentChange(0);
            }
        hideFloatingbar();



    }
    //메뉴 버튼
    boolean vis=false;
    public void menuButtonClick(View view){
        if(!vis) {
            ObjectAnimator.ofFloat(saveButton, "translationY", -200f).start();
            saveButton.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(timerButton, "translationY", -400f).start();
            timerButton.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(uploadButton, "translationY", -600f).start();
            uploadButton.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(addButton, "translationY", -800f).start();
            addButton.setVisibility(View.VISIBLE);
            vis=true;
        }else {
            hideFloatingbar();
        }
    }
    //플로팅바 감추기
    private void hideFloatingbar(){
        ObjectAnimator.ofFloat(addButton, "translationY", 0f).start();
        ObjectAnimator.ofFloat(uploadButton, "translationY", 0f).start();
        ObjectAnimator.ofFloat(timerButton, "translationY", 0f).start();
        ObjectAnimator.ofFloat(saveButton, "translationY", 0f).start();


        addButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        timerButton.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);

        vis=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        String s = (String) EDITDATE;
        List<SubItem> subItemList1 = new ArrayList<>();
        List<MainItem> mainItems = new ArrayList<>();
        List<CalendarVO> list = new ArrayList<>();
        if (!s.equals("")){
                    DBHelper dbHelper = new DBHelper(getContext());
                    SQLiteDatabase db= dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM details WHERE date=?",new String[]{s});
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
            String routineName =list.get(0).getRoutineName();

            for (CalendarVO vo: list){

                if (!routineName.equals(vo.getRoutineName())){

                    MainItem mainItem =new MainItem(routineName,subItemList1);
                    mainItems.add(mainItem);

                    subItemList1=new ArrayList<>();


                }

                String set=vo.getSet();
                String weight=vo.getWeight();
                String time=vo.getTime();

                SubItem subItem = new SubItem();

                subItem.setSet(set); subItem.setWeight(Float.parseFloat(weight.substring(0,weight.length()-2))); subItem.setTime(Integer.parseInt(time.substring(0,time.length()-1)));
                subItemList1.add(subItem);

                //Log.d("adapterCheck",subItem.getSet()+"|"+subItem.getWeight()+"|"+subItem.getTime());

                routineName=vo.getRoutineName();
            }
            MainItem mainItem =new MainItem(routineName,subItemList1);
            mainItems.add(mainItem);

            saveDate=s;


            setAdapter(mainItems);
        }else{
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setAdapter(List<MainItem>  mainItems){

        for ( MainItem mainItem : mainItems){
            driveAdapter.list.add(mainItem);
        }
        driveAdapter.notifyDataSetChanged();


        isEdit=true;
    }
}
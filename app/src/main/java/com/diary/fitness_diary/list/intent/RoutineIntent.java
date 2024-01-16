package com.diary.fitness_diary.list.intent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.R;
import com.diary.fitness_diary.databinding.ListModalBinding;

import java.util.ArrayList;

public class RoutineIntent extends AppCompatActivity {
    ListModalBinding binding;

    IntentDriverAdapter adapter;
    ArrayList datas = new ArrayList<>();
    Dialog dialog;
    private RadioGroup mRgLine1,mRgLine2;
    public String selectedColor;
    ImageButton imageButton;
    ArrayList<String> saveList = new ArrayList<>();
    EditText editText;
    boolean sameTitle;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.list_modal);
        binding.setIntent(this);
        recyclerView=binding.intentListView;
        //기본 색 설정
        selectedColor="#DC143C";
        //색 선택 초기화 & 다이얼로그 설정
        onColorInit();
        //editText 내림 설정
        LinearLayout constraintLayout=binding.parentXML;
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
        RecyclerView recyclerView = binding.intentListView;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });


        adapter=new IntentDriverAdapter(datas,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    //아이템 추가 버튼
    public void onClickAddButton(View view){
        if(adapter.getItemCount()<=15){
            adapter.itemAdd();
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
        }else{
            Toast.makeText(this,"더 이상 추가할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==10 || resultCode==-1){
            if (data != null) {

                int pos=Integer.parseInt(data.getStringExtra("position"));
                adapter.datas.set(pos,new IntentDriveVO(data.getStringExtra("reply")));
                adapter.notifyDataSetChanged();
            }
        }
    }

    //키보드 내림
    public void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //색 선택
    @SuppressLint("ResourceType")
    public void onClickColor(View view){
        //위치값
        imageButton=binding.color;
        int[]locationView = new int[2];
        imageButton.getLocationOnScreen(locationView);
        //dialog위치 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.y= locationView[1]-600;
        dialog.getWindow().setAttributes(params);

        dialog.show();
    }

    //색 선택 시 닫힘
    public void onClickColorRadio(View view){
        dialog.cancel();
    }
    //색 선택 초기화 & 다이얼로그 설정
    private void onColorInit(){
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없애기
        dialog.setContentView(R.layout.list_color_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//dialog 배경색 없애기
        dialog.getWindow().setDimAmount(0); //밖에 있는 배경색 없애기

        mRgLine1=(RadioGroup) dialog.findViewById(R.id.rg_line1);
        //mRgLine1.clearCheck();
        mRgLine1.setOnCheckedChangeListener(listener1);
        mRgLine2=(RadioGroup) dialog.findViewById(R.id.rg_line2);
        mRgLine2.clearCheck();
        mRgLine2.setOnCheckedChangeListener(listener2);
    }

    //저장
   public void onClickSaveButton(View view){
        editText = binding.editTextRoutineName;
        ArrayList<String> nList=adapter.save(saveList);
        titleCheck(editText.getText().toString().trim());
        if(editText.getText().toString().equals("")||editText.getText().toString()==null){
            Toast.makeText(this,"루틴 이름을 입력해주세요!",Toast.LENGTH_SHORT).show();
        }else if(nList.size()==0){
            Toast.makeText(this,"하나 이상의 운동명을 입력해주세요!",Toast.LENGTH_SHORT).show();
        }else if(sameTitle){
            Toast.makeText(this,"중복된 루틴명입니다!",Toast.LENGTH_SHORT).show();
        }else {
            saveList.add(0,editText.getText().toString());
            saveList.add(1,selectedColor);
            setDB(saveList);
            setResult(RESULT_OK);
            finish();
        }
    }
    public void titleCheck(String name){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c=database.rawQuery("select * from routine where routineName=?",new String[]{name});
        if(c.getCount()>=1){
            sameTitle=true;
        }else{
            sameTitle=false;
        }
    }

    //DB처리
    public void setDB(ArrayList<String>list){
        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("routineName",list.get(0));
        contentValues.put("color",list.get(1));
        db.insert("routine",null,contentValues);


        for(int i=0; i<list.size()-2; i++){
            ContentValues contentValues2=new ContentValues();
            contentValues2.put("exerciseName",list.get(i+2));
            contentValues2.put("routineName",list.get(0));
            db.insert("exercise",null,contentValues2);
        }
        db.close();

    }

    //뒤로가기
    public void onClickShutdownButton(View view){
        setResult(RESULT_OK);
        finish();
    }

    //색 선택 초기화
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View radioButton=mRgLine1.findViewById(mRgLine1.getCheckedRadioButtonId());
            int radioId=mRgLine1.indexOfChild(radioButton);
            RadioButton btn=(RadioButton) mRgLine1.getChildAt(radioId);
            selectedColor=(String)btn.getText();
            if(checkedId != -1){
                mRgLine2.setOnCheckedChangeListener(null);
                mRgLine2.clearCheck();
                mRgLine2.setOnCheckedChangeListener(listener2);
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View radioButton=mRgLine2.findViewById(mRgLine2.getCheckedRadioButtonId());
            int radioId=mRgLine2.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) mRgLine2.getChildAt(radioId);
            selectedColor =(String) btn.getText();
            if(checkedId != -1){
                mRgLine1.setOnCheckedChangeListener(null);
                mRgLine1.clearCheck();
                mRgLine1.setOnCheckedChangeListener(listener1);
            }
        }
    };

}

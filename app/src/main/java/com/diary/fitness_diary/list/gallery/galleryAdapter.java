package com.diary.fitness_diary.list.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class galleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<galleryIntent.GalleryVO> datas;
    Context context;
    Activity activity;

    public galleryAdapter(ArrayList<galleryIntent.GalleryVO>datas, Activity activity,Context context) {
        this.datas=datas;
        this.activity=activity;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
            return new galleryHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        galleryIntent.GalleryVO itemVO=datas.get(position);
        galleryHolder galleryHolder=(galleryHolder) holder;
        byte[] b=  itemVO.getGalleryName();
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        galleryHolder.imageView.setImageBitmap(bitmap);
        galleryHolder.textView.setText(itemVO.getGalleryDate());

        galleryHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDialog(chkTouchInside(v),itemVO.galleryName,position,galleryHolder.recyclerView);

            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setDialog(int[]locatioin,byte[]b,int position,RecyclerView recyclerView){
        Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.list_gallery_custom_dialog);
        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.flags &=~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.x=locatioin[0];
        params.y=locatioin[1];

        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);//크기 조절
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //여백 없앰
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //배경 투명
        //dialog.getWindow().setGravity(Gravity.CENTER); //상단 배치

        dialog.show();

        Button buttonOK = dialog.findViewById(R.id.dialogBack);
        Button buttonNO = dialog.findViewById(R.id.dialogDelte);
        boolean removeCheck[]= {false};
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                saveBitmapToJpeg(bitmap);
                dialog.dismiss();
            }
        });
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBDelte(datas.get(position).galleryIndex);
                datas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,datas.size()-position);
                dialog.dismiss();

            }
        });
    }
    //DB삭제
    public void DBDelte(String index){
        Log.d("DBH","delete");
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM gallery WHERE galleryIndex=?",new String[]{index});
        db.close();
    }

    //위치 값 구하기
    public int[] chkTouchInside(View view) {
        Display display = activity.getWindowManager().getDefaultDisplay();  // in Activity
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)

        int height = size.y;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        location[0]=location[0]-600;
        location[1]=height-location[1]-400;
        Log.d("getLoc","X:"+location[0]+",Y:"+location[1]);
        return location;

    }
    //이미지 파일 저장
    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[]data=stream.toByteArray();

        DBHelper helper=new DBHelper(activity);
        SQLiteDatabase db=helper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("galleryName",data);
        contentValues.put("galleryDate",getDate());
        db.insert("gallery",null,contentValues);
        db.close();

    }
    //현재시간날짜 구하기
    public String getDate(){
        Date currentTime= Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(currentTime);
    }
}

package com.diary.fitness_diary.list.gallery;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.R;


import java.util.ArrayList;
import java.util.List;

public class galleryIntent extends AppCompatActivity {
    galleryAdapter galleryAdapter;
    RecyclerView recyclerView;
    List<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_gallery_intent);


        recyclerView=findViewById(R.id.galleryRecyclerView);
        ArrayList<GalleryVO> datas = galleryDBSelect();



        galleryAdapter=new galleryAdapter(datas,galleryIntent.this,getApplicationContext());

        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(5,16f* Resources.getSystem().getDisplayMetrics().density);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.addItemDecoration(gridSpacingItemDecoration);

        //뒤로가기 이벤트
        ImageButton imageButton = findViewById(R.id.btn_shutdown2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });



    }


    //갤러리 이미지 조회
    public ArrayList<GalleryVO> galleryDBSelect(){
        ArrayList<GalleryVO>galleryList=new ArrayList<>();
        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor1=db.rawQuery("select * from gallery order by galleryIndex desc",null);
        if(cursor1.getCount()!=0){
            while (cursor1.moveToNext()){
                GalleryVO vo = new GalleryVO();
                vo.setGalleryIndex(cursor1.getString(0));
                vo.setGalleryName(cursor1.getBlob(1));
                vo.setGalleryDate(cursor1.getString(2));
                galleryList.add(vo);
            }
            return galleryList;
        }else{
            return galleryList;
        }

    }


    //갤러리 만들기
     class GalleryVO {
        String galleryDate;
        String galleryIndex;
        byte[] galleryName;

        public String getGalleryDate() {
            return galleryDate;
        }

        public void setGalleryDate(String galleryDate) {
            this.galleryDate = galleryDate;
        }

        public String getGalleryIndex() {
            return galleryIndex;
        }

        public void setGalleryIndex(String galleryIndex) {
            this.galleryIndex = galleryIndex;
        }

        public byte[] getGalleryName() {
            return galleryName;
        }

        public void setGalleryName(byte[] galleryName) {
            this.galleryName = galleryName;
        }
    }

}
class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
    int spanCount;
    int spacing;
    public GridSpacingItemDecoration(int spanCount,float spacing) {
        this.spanCount=spanCount;
        this.spacing=(int)spacing;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        c.drawRect(0,0,parent.getWidth(),parent.getHeight(),paint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,20);
        ViewCompat.setElevation(view,20.0f);



    }
}



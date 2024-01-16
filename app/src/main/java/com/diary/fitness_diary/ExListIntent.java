package com.diary.fitness_diary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diary.fitness_diary.R;
import com.diary.fitness_diary.databinding.ExListIntentBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExListIntent extends AppCompatActivity implements ExListIntentAdapter.OnItemClickListener {
    ExListIntentBinding binding;
    RecyclerView recyclerView;
    ExListIntentAdapter adapter;
    List<ExListIntentVO> gkcp,rktma,emd,djRo,vkf;
    EditText editText;
    List<ExListIntentVO> combinedList;
    String message="";
    ImageButton button,star_ex;
    boolean colorCheck;
    Button[] b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.ex_list_intent);
        binding.setEx(this);
        star_ex=binding.starEx;
        b= new Button[]{binding.gkcp, binding.rktma, binding.emd, binding.djRo, binding.vkf};
        recyclerView=binding.exListIntent;
        editText=binding.searchEx;
        gkcp = new ArrayList<>();
        String[] gkcpString ={"바벨 백스쿼트",
                "프론트 스쿼트",
                "저처 스쿼트",
                "바벨 불가리안 스플릿 스쿼트",
                "덤벨 불가리안 스플릿 스쿼트",
                "덤벨 스플릿 스쿼트",
                "맨몸 스플릿 스쿼트",
                "에어 스쿼트",
                "점프 스쿼트",
                "글루트 브릿지",
                "글루트 킥백 머신",
                "노르딕 햄스트링 컬",
                "덤벨 고블릿 스쿼트",
                "덤벨 런지",
                "덤벨 레그 컬",
                "덤벨 레터럴 런지",
                "덤벨 루마니안 데드리프트",
                "덤벨 스모 데드리프트",
                "덤벨 스모 스쿼트",
                "덤벨 스쿼트",
                "덤벨 스티프 레그 데드리프트",
                "덤벨 원레그 데드리프트",
                "덩키 킥",
                "데피싯 데드리프트",
                "라잉 힙 어브덕션",
                "런지 트위스트",
                "런지",
                "레그 익스텐션",
                "레그 컬",
                "레그 프레스",
                "루마니안 데드리프트",
                "리버스 브이 스쿼트",
                "맨몸 레터럴 런지",
                "맨몸 오버헤드 스쿼트",
                "맨몸 원레그 데드리프트",
                "맨몸 카프레이즈",
                "몬스터 글루트 머신",
                "바벨 런지",
                "바벨 레터럴 런지",
                "바벨 박스 스쿼트",
                "바벨 스모 스쿼트",
                "바벨 스탠딩 카프 레이즈",
                "바벨 스플릿 스쿼트",
                "바벨 원레그 데드리프트",
                "바벨 점프 스쿼트",
                "바벨 프론트 랙 런지",
                "바벨 핵 스쿼트",
                "바벨 힙 쓰러스트",
                "브이 스쿼트",
                "사이드 라잉 클램",
                "수평 레그 프레스",
                "수평 원레그 프레스",
                "스모 데드리프트",
                "스모 에어 스쿼트",
                "스미스머신 데드리프트",
                "스미스머신 스쿼트",
                "스미스머신 스플릿 스쿼트",
                "스탠딩 카프 레이즈",
                "스텝업",
                "스티프 레그 데드리프트",
                "시티드 레그 컬",
                "시티드 원레그 컬",
                "시티드 카프 레이즈",
                "싱글 레그 글루트 브릿지",
                "원레그 익스텐션",
                "원레그 컬",
                "원레그 프레스",
                "이너 싸이 머신",
                "정지 데드리프트.",
                "정지 데드리프트",
                "정지 백 스쿼트",
                "정지 스모 데드리프트",
                "중량 스텝업",
                "컨벤셔널 데드리프트",
                "케이블 덩키 킥",
                "케이블 풀 스루",
                "케이블 힙 어브덕션",
                "케틀벨 고블릿 스쿼트",
                "케틀벨 데드리프트",
                "케틀벨 런지 트위스트",
                "케틀벨 레터럴 런지",
                "케틀벨 스모 데드리프트",
                "케틀벨 스모 스쿼트",
                "트랩바 데드리프트",
                "피스톨 박스 스쿼트",
                "피스톨 스쿼트",
                "핵 스쿼트 머신",
                "힙 쓰러스트 머신",
                "힙 쓰러스트",
                "힙 어브덕션 머신",
        };
        for(int i=1; i<91; i++){
            if(i!=69){
                gkcp.add(new ExListIntentVO(getResources().getIdentifier("h"+i,"drawable","com.example.fitness_diary"),gkcpString[i-1]));
            }
        }

        //##############################################################
        rktma = new ArrayList<>();
        String[] rktmaString ={"벤치프레스",
                "인클라인 벤치프레스",
                "덤벨 벤치프레스",
                "딥스",
                "중량 딥스",
                "덤벨 플라이",
                "인클라인 덤벨 플라이",
                "푸시업",
                "로우 풀리 케이블 플라이",
                "덤벨 스퀴즈 프레스",
                "덤벨 풀오버",
                "디클라인 덤벨 벤치프레스",
                "디클라인 덤벨 플라이",
                "디클라인 벤치프레스",
                "디클라인 체스트 프레스 머신",
                "바벨 플로어 프레스",
                "스미스머신 벤치프레스",
                "스미스머신 인클라인 벤치프레스",
                "스탠딩 케이블 플라이",
                "스포토 벤치프레스",
                "시티드 딥스 머신",
                "아처 푸시업",
                "어시스트 딥스 머신",
                "인클라인 덤벨 벤치프레스",
                "인클라인 덤벨 트위스트 프레스",
                "인클라인 벤치프레스 머신",
                "인클라인 체스트 프레스 머신 ",
                "인클라인 케이블 플라이",
                "인클라인 푸시업",
                "중량 푸시업",
                "체스트 프레스 머신",
                "케이블 크로스오버",
                "클랩 푸쉬업",
                "클로즈그립 푸쉬업",
                "파이크 푸쉬업",
                "펙덱 플라이 머신",
                "해머 벤치프레스",
                "힌두 푸시업"};
        for(int i=1; i<39; i++){
            rktma.add(new ExListIntentVO(getResources().getIdentifier("g"+i,"drawable","com.example.fitness_diary"),rktmaString[i-1]));
        }
        //##############################################################
        emd = new ArrayList<>();
        String[] emdString ={"굿모닝 엑서사이즈",
                "풀업",
                "중량 풀업",
                "친업",
                "중량 친업",
                "바벨로우",
                "펜들레이 로우",
                "인클라인 덤벨 로우",
                "인클라인 바벨 로우",
                "덤벨로우",
                "라잉 바벨 로우",
                "랫풀다운",
                "레터럴 와이드 풀다운",
                "로우 머신",
                "맥그립 랫풀다운",
                "바벨 풀오버",
                "익스텐션",
                "비하인드 넥 풀다운",
                "스미스머신 로우",
                "시티드 로우 머신",
                "시티드 케이블 로우",
                "어시스트 풀업 머신",
                "언더그립 랫풀다운",
                "언더그립 바벨 로우",
                "언더그립 하이 로우 머신",
                "원암 덤벨 로우",
                "원암 레터럴 와이드 풀다운",
                "원암 로우 로우 머신",
                "원암 시티드 케이블 로우",
                "원암 케이블 풀다운",
                "원암 하이 로우 머신",
                "인버티드 로우",
                "정지 바벨 로우",
                "중량 하이퍼 익스텐션",
                "케이블 암 풀다운",
                "티바 로우 머신",
                "패러럴그립 랫풀다운",
                "플로어 시티드 케이블 로우",
                "하이 로우 머신",
                "하이퍼 익스텐션"};
        for(int i=1; i<41; i++){
            emd.add(new ExListIntentVO(getResources().getIdentifier("d"+i,"drawable","com.example.fitness_diary"),emdString[i-1]));
        }
        //##############################################################
        djRo = new ArrayList<>();
        String[] djRoString ={"오버헤드 프레스",
                "덤벨 숄더 프레스",
                "아놀드 덤벨 프레스",
                "숄더 프레스 머신",
                "비하인드 넥 프레스",
                "덤벨 프론트 레이즈",
                "덤벨 레터럴 레이즈",
                "벤트오버 덤벨 레터럴 레이즈",
                "덤벨 슈러그",
                "Y 레이즈",
                "덤벨 Y 레이즈",
                "덤벨 업라이트 로우",
                "랜드마인 프레스",
                "레터럴 레이즈 머신",
                "리어 델토이드 플라이 머신",
                "바벨 슈러그",
                "바벨 업라이트 로우",
                "바벨 프론트 레이즈",
                "숄더 탭",
                "슈러그 머신",
                "스미스머신 슈러그",
                "스미스머신 오버헤드 프레스",
                "시티드 덤벨 리어 레터럴 레이즈",
                "시티드 덤벨 숄더 프레스",
                "시티드 바벨 숄더 프레스",
                "원암 랜드마인 프레스",
                "원암 케이블 레터럴 레이즈",
                "이지바 업라이트 로우",
                "이지바 프론트 레이즈",
                "케이블 레터럴 레이즈",
                "케이블 리버스 플라이",
                "케이블 슈러그",
                "케이블 익스터널 로테이션",
                "케이블 인터널 로테이션",
                "케이블 프론트 레이즈",
                "페이스 풀",
                "푸시 프레스",
                "플레이트 숄더 프레스",
                "핸드 스탠드 푸쉬업",
                "핸드 스탠드"};
        for(int i=1; i<41; i++){
            djRo.add(new ExListIntentVO(getResources().getIdentifier("d"+i,"drawable","com.example.fitness_diary"),djRoString[i-1]));
        }
        //#########################################################
        vkf = new ArrayList<>();
        String[] vkfString ={"바벨 컬",
                "이자바 컬",
                "덤벨 컬",
                "덤벨 해머 컬",
                "케이블 컬",
                "클로즈 그립 벤치프레스",
                "덤벨 트라이셉 익스텐션",
                "시티드 덤벨 트라이셉 익스텐션",
                "케이블 트라이셉 익스텐션",
                "프리쳐 컬 머신",
                "덤벨 리스트 컬",
                "덤벨 킥백",
                "덤벨 프리쳐 컬",
                "리버스 덤벨 리스트 컬",
                "리버스 바벨 리스트 컬",
                "리버스 바벨 컬",
                "리스트 롤러",
                "바벨 라잉 트라이셉 익스텐션",
                "바벨 리스트 컬",
                "바벨 프리쳐 컬",
                "벤치 딥스",
                "스컬 크러셔",
                "암 컬 머신",
                "이지바 리스트 컬",
                "이지바 프리쳐 컬",
                "인클라인 덤벨 컬",
                "케이블 라잉 트라이셉 익스텐션",
                "케이블 오버헤드 트라이셉 익스텐션",
                "케이블 푸시 다운",
                "케이블 해머컬",
                "트라이셉 익스텐션 머신"
        };
        for(int i=1; i<32; i++){
            vkf.add(new ExListIntentVO(getResources().getIdentifier("p"+i,"drawable","com.example.fitness_diary"),vkfString[i-1]));
        }
        combinedList = new ArrayList<>(gkcp);
        combinedList.addAll(rktma);
        combinedList.addAll(emd);
        combinedList.addAll(djRo);
        combinedList.addAll(vkf);

    }

    //검색기능
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            if(intent.getStringExtra("position")!=null){
                message = intent.getStringExtra("position");
            }
        }


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<ExListIntentVO> list = new ArrayList<>();
                String news=s+"";
                if(!news.equals("")) {
                    for (ExListIntentVO vo : combinedList) {
                        if (vo.getName().replace(" ","").contains(news.replace(" ",""))) {
                            list.add(vo);
                        }
                    }
                }
                drawAdapter(list);
            }
        });
    }
    //drawAdapter
    public void drawAdapter(List<ExListIntentVO> list){
        adapter=new ExListIntentAdapter(list,this,colorCheck);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void setBackground(int i){

        b[i].setBackgroundColor(Color.parseColor("#FFB400"));
        for (int a=0; a<b.length; a++){
            if(a!=i){Log.d("colorCheck",a+"");
                b[a].setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }
        }
    }
    //별 색 초기화
    private void initStarColor(){

            colorCheck=false;
        star_ex.setColorFilter(Color.parseColor("#3c3c3c"));
    }
    //하체 클릭
    public void onClickgkcp(View view){
            drawAdapter(checkDB(gkcp));
            setBackground(0);       //버튼 클릭 시 색 지정
        initStarColor();            //버튼 클릭 시 즐찾 색 없애기
    }
    //가슴
    public void onClickrktma(View view){
            drawAdapter(checkDB(rktma));
        setBackground(1);
        initStarColor();
    }
    //등
    public void onClickemd(View view){
            drawAdapter(checkDB(emd));
        setBackground(2);
        initStarColor();
    }
    //어꺠
    public void onClickdjRo(View view){
            drawAdapter(checkDB(djRo));
        setBackground(3);
        initStarColor();
    }
    //팔
    public void onClickvkf(View view){
            drawAdapter(checkDB(vkf));
        setBackground(4);
        initStarColor();

    }
    //adapter에서 데이터 받기
    @Override
    public void onItemClick(String data) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra("reply", data);
        if(!message.equals(""))
            replyIntent.putExtra("position",message);

        setResult(RESULT_OK, replyIntent);
        finish();
    }
    //adapter에서 별 클릭 시
    @Override
    public boolean onStarClick(String name) {
        boolean b =insertDB(name);
        dbCheck();
       return b;
    }

    //메인 별 클릭
    public void onStartClick(View view){
        button=(ImageButton)view;
        if (colorCheck) {
            colorCheck=false;
            button.setColorFilter(Color.parseColor("#3c3c3c"));
            drawAdapter(checkDB(combinedList));
        }else {
            colorCheck=true;
            button.setColorFilter(Color.parseColor("#FF1493"));

            List<ExListIntentVO> l = new ArrayList<>();
            Iterator<ExListIntentVO> iterator = checkDB(combinedList).iterator();
            while (iterator.hasNext()){
                ExListIntentVO i = iterator.next();
                if(i.isStar()){
                    l.add(i);
                }
            }
            drawAdapter(l);
        }
        buttonColorInit();
    }
    //별 클릭시 버튼 색 없앰
    private void buttonColorInit(){
        for (int i=0; i<b.length; i++){
            b[i].setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    //즐찾 저장
    private boolean insertDB(String string){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from exColor where exName=?",new String[]{string});
        if (cursor.getCount()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("exName", string);
            sqLiteDatabase.insert("exColor",null,contentValues);
            Cursor c=sqLiteDatabase.rawQuery("select * from exColor",null);
            while (c.moveToNext()){
            }
            sqLiteDatabase.close();
            c.close();cursor.close();dbHelper.close();
            return true;
        }else{
            sqLiteDatabase.execSQL("delete from exColor where exName=?",new String[]{string});
            sqLiteDatabase.close();
            Log.d("isStarCheck",string+"=>즐찾에서 삭제");
            cursor.close();
            dbHelper.close();

            DBHelper d = new DBHelper(this);
            SQLiteDatabase sd = d.getWritableDatabase();
            Cursor c = sd.rawQuery("select * from exColor",null);
            Log.d("isStarCheck1","db쳌"+c.getCount());

            c.close(); sd.close(); d.close();
            return false;
        }

    }
    private void dbCheck(){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from exColor",null);
        while (cursor.moveToNext()){
        }
    }
    //즐찾 검사
    private List<ExListIntentVO> checkDB(List<ExListIntentVO> list){
        List<ExListIntentVO> newList = new ArrayList<>();
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
                for(ExListIntentVO vo : list){
                    Cursor cursor=sqLiteDatabase.rawQuery("select * from exColor ",null);
                    if (cursor.getCount()==0) vo.setStar(false);
                    while (cursor.moveToNext()){
                        String s= cursor.getString(0);
                        if (vo.getName().equals(s)){
                            vo.setStar(true);
                            //Log.d("isStarCheck2","vo.isStar IN TRUE:"+vo.isStar());
                            break;
                        }else{
                            vo.setStar(false);
                            //Log.d("isStarCheck2","vo.isStar IN FALSE:"+vo.isStar());
                        }
                    }
                    cursor.close();
                    //if (vo.isStar())
                    newList.add(vo);
                }
                sqLiteDatabase.close(); dbHelper.close();

        return newList;
    }

}

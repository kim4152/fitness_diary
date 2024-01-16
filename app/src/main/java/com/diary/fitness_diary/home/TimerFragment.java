package com.diary.fitness_diary.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.fitness_diary.MainActivity;
import com.diary.fitness_diary.MyCounterService;
import com.diary.fitness_diary.R;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class TimerFragment extends Fragment {
    public static String timerServiceCheck="false";

    SharedPreferences sharedPreferences;
    CircularSeekBar circularSeekBar;
    int min,sec;
    TextView textView;
    com.shawnlin.numberpicker.NumberPicker numberPicker1,numberPicker2;
    Button start,stop,delete;
    ConstraintLayout constraintLayout;
    CountDownTimer countDownTimer,CircleCountDownTimer;
    long timerText,max,circleProgress;
    boolean timerRunning;
    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_timer, container, false);

        circularSeekBar=view.findViewById(R.id.timer);
        textView=view.findViewById(R.id.ttv);
        constraintLayout=view.findViewById(R.id.consNumberPicker);

        sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        numberPicker1=view.findViewById(R.id.number_picker1);
        numberPicker2=view.findViewById(R.id.number_picker2);

        start=(Button) view.findViewById(R.id.timerStart);
        stop=view.findViewById(R.id.timerStop);
        delete=view.findViewById(R.id.timerDelete);

        intent = new Intent(getContext(), MyCounterService.class);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClick();

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopClick();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClick();
            }
        });



        ImageView hideButton = view.findViewById(R.id.horizontalRule);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                constraintLayout = view.findViewById(R.id.timerCons);
//                getView().setVisibility(View.GONE);
                MainActivity mainActivity=(MainActivity) getContext();
                mainActivity.hideView(TimerFragment.this);
            }
        });

        numberPickerMethod();

        try {
            numberPicker1.setValue(sharedPreferences.getInt("min", 0));
            min = sharedPreferences.getInt("min", 0);
            numberPicker2.setValue(sharedPreferences.getInt("sec", 0));
            sec = sharedPreferences.getInt("sec", 0);
        }catch (Exception e){

        }
        return view;
    }


    private void numberPickerMethod(){
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                min=newVal;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("min", min);
                        editor.apply();
                    }
                }).start();

            }
        });
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sec=newVal;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("sec", sec);
                        editor.apply();
                    }
                }).start();

            }
        });
    }

    private void updateCountDownText(){
        int min1=(int)(timerText/1000)/60;
        int sec1=(int)(timerText/1000)%60;
        String s=String.format(Locale.getDefault(),"%02d:%02d",min1,sec1+1);
        textView.setText(s);
    }

    private void startClick(){
        max=(min*60+sec)*1000;
        timerText=(long) max;
        circleProgress=max;

        start.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);

        circularSeekBar.setVisibility(View.VISIBLE);
        circularSeekBar.setMax(circleProgress/60);
        circularSeekBar.setProgress(0);


        textView.setVisibility(View.VISIBLE);

        timerServiceCheck="true";
        intent.putExtra("time",timerText+"");
        //getContext().startService(intent);
        getContext().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        new Thread(new GetCountThread()).start();
        timerRunning=true;
    }

    MyCounterService yourService;
    // ServiceConnection 객체 생성
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Service와 연결되었을 때 호출
            MyCounterService.MyBinder binder = (MyCounterService.MyBinder) service;
            yourService = binder.getService();
            Log.d("serviceCheck","serviceStart");
            // Service의 메서드 호출
                Log.d("serviceCheck","control:act");
                yourService.controlService(timerText);


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // Service와 연결이 끊겼을 때 호출
            Log.d("serviceCheck","disCOnnected");
        }
    };


    private void stopClick(){
        if(timerRunning==true) {
            countDownTimer.cancel();
            CircleCountDownTimer.cancel();
            stop.setText("계속");
            timerRunning = false;
            getContext().unbindService(serviceConnection);
        }else{
            new Thread(new GetCountThread()).start();
            stop.setText("정지");
            timerRunning=true;
            getContext().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    private void deleteClick(){
        countDownTimer.cancel();
        CircleCountDownTimer.cancel();
        if (timerRunning) getContext().unbindService(serviceConnection);
        timerServiceCheck="false";
        finish();
    }

    private void soundCheck(){
        AudioManager manager;
        manager=(AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        switch (manager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                //소리일때
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone=RingtoneManager.getRingtone(getContext(),uri);
                ringtone.play();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                //무음일떄
                Toast.makeText(getContext(),"타이머 끝",Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                //진동일떄
                Vibrator vibrator=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                break;
        }
    }

    private void finish(){


        numberPicker1.setValue(min);
        numberPicker2.setValue(sec);

        constraintLayout.setVisibility(View.VISIBLE);
        start.setVisibility(View.VISIBLE);
        stop.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        circularSeekBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    private class GetCountThread implements Runnable{
        private Handler handler = new Handler();
        @Override
        public void run() {


            handler.post(new Runnable() {
                @Override
                public void run() {
                    CircleCountDownTimer=new CountDownTimer(circleProgress,1){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            circleProgress=millisUntilFinished;
                            circularSeekBar.setProgress(circleProgress/60);
                        }
                        @Override
                        public void onFinish() {
                            finish();
                        }
                    }.start();

                    countDownTimer = new CountDownTimer(timerText,1) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timerText=millisUntilFinished;
                            updateCountDownText();
                        }
                        @Override
                        public void onFinish() {
                            soundCheck();
                        }
                    }.start();
                }
            });
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
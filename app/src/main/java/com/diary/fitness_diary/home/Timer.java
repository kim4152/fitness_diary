package com.diary.fitness_diary.home;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.diary.fitness_diary.MyCounterService;
import com.diary.fitness_diary.R;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class Timer extends Dialog {
    CircularSeekBar circularSeekBar;
    int min,sec;
    TextView textView;
    com.shawnlin.numberpicker.NumberPicker numberPicker1,numberPicker2;
    Button start,stop,delete;
    ConstraintLayout constraintLayout;
    CountDownTimer countDownTimer,CircleCountDownTimer;
    long timerText,max,circleProgress;
    boolean timerRunning;

    public Timer(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_timer_custom_dialog);
        circularSeekBar=findViewById(R.id.timer);
        textView=findViewById(R.id.ttv);
        constraintLayout=findViewById(R.id.consNumberPicker);

        numberPicker1=findViewById(R.id.number_picker1);
        numberPicker2=findViewById(R.id.number_picker2);

        start=(Button) findViewById(R.id.timerStart);
        stop=findViewById(R.id.timerStop);
        delete=findViewById(R.id.timerDelete);

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


        numberPickerMethod();

    }
    //넘버피커 값
    private void numberPickerMethod(){
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                min=newVal;
            }
        });
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sec=newVal;
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

        Intent intent = new Intent(getContext(), MyCounterService.class);
       // getContext().bindService(intent,connection,Context.BIND_AUTO_CREATE);
        new Thread(new GetCountThread()).start();


        timerRunning=true;
    }

    private void stopClick(){
        if(timerRunning==true) {
            countDownTimer.cancel();
            CircleCountDownTimer.cancel();
            stop.setText("계속");
            timerRunning = false;
        }else{
            countDownTimer = new CountDownTimer(timerText,1) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerText=millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {

                }
            }.start();

            circularSeekBar.setProgress(circleProgress/60);
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
            stop.setText("정지");
            timerRunning=true;
        }
    }

    private void deleteClick(){
        countDownTimer.cancel();
        CircleCountDownTimer.cancel();
        finish();
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

                        }
                    }.start();
                }
            });
        }
    }

    private IMyCounterService binder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder=IMyCounterService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}

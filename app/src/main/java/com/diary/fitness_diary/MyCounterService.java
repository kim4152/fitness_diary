package com.diary.fitness_diary;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyCounterService extends Service {
    Context context;
    int count;
    boolean running;
    public MyCounterService() {
        context=this;
    }

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public MyCounterService getService() {
            return MyCounterService.this;
        }
    }

    public void controlService(Long timer){
        this.count = timer.intValue()/1000;
        Log.d("serviceCheck","controlservice:"+count);
        running=true;
        Thread counter = new Thread(new Counter());
        counter.start();
    }


    @Override
    public void onDestroy() {
        running=false;
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("serviceCheck","onBind");
        return binder;
    }

    private class Counter implements Runnable{
        private Handler handler = new Handler();
        @Override
        public void run() {
            //
            for(int i=count; i>=0 ; i--){
                if(!running){
                    break;
                }
                int finalI = i;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("serviceCheck", finalI +"");
                    }
                });

                //try-catch 시작
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){   //InterruptedException
                    e.printStackTrace();
                }
                // try-catch 끝

            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(),"service 종료", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

}
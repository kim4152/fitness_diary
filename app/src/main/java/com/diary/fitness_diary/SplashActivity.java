package com.diary.fitness_diary;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.diary.fitness_diary.databinding.SplashBinding;

public class SplashActivity extends AppCompatActivity {
    SplashBinding binding;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        binding=SplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.splash);
        try{
            Intent intent=new Intent(this,MainActivity.class );
            startActivity(intent);
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        finish();
    }
}

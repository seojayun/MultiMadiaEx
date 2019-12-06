package com.example.multimadiaex;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;


//1. xml 작업 후 res 폴더에 raw 폴더 생성 (new - > andriod Resource Directory 에서 생성)



public class MainActivity extends AppCompatActivity {

    Switch swPlayer;
    MediaPlayer mp;  //2.미디어 플레이어 클래스 변수 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swPlayer = (Switch)findViewById(R.id.swPlayer);
        mp=MediaPlayer.create(this,R.raw.like2); //3. mp 클래스에 가져올 파일을 create 한다.
        swPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  //4. 스위치는 체크박스하고 원리가 동일하다
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    mp.start();
                }else {
                    mp.stop();
                }
            }
        });
    }
}

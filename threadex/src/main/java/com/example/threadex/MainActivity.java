package com.example.threadex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar sb1,sb2;
    Button btnStart,btnStop;
    TextView tv1,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb1= (SeekBar)findViewById(R.id.sb1);
        sb2=(SeekBar)findViewById(R.id.sb2);
        btnStart=(Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);
        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);


        //4.5 스레드는 익명과 이름을 생성하는 2가지 방법이 있다.

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //쓰레드는 이름을 주어서 만드는것과 익명으로 도 사용이 가능하다.
                new Thread() {  //4.익명으로 생성
                    public void run() {
                        for (int i = 0; i < 100; i=i+2) {
                            runOnUiThread(new Runnable() {//7.  runOnUiThread 생성  :안정적인 스레드다 왠만하면 이것을사용한다. ☆☆☆☆
                                @Override
                                public void run() {
                                    sb1.setProgress(sb1.getProgress() + 2); //1.sb1(자기)가 갖고있는 getProgress 값을 가져오고 올릴때마다 2씩 증가한다.
                                    tv1.setText("1번 진행률 : " +sb1.getProgress()+"%");  //6.텍스트뷰에 각 쓰레드의 진행상황을 표기되도록 작성
                                }
                            });
                            SystemClock.sleep(100); //2.     0.1초쉬고 for문이 진행되도록 하는 문구이다.
                        }
                    }
                }.start();

//                new Thread() {  //4.익명으로 생성
//                    public void run() {
//                        for (int i = 0; i < 100; i++) {
//                            sb2.setProgress(sb2.getProgress() + 1); //1.sb1(자기)가 갖고있는 getProgress 값을 가져오고 올릴때마다 2씩 증가한다.
//                            SystemClock.sleep(100); //2.     0.1초쉬고 for문이 진행되도록 하는 문구이다.
//                            tv2.setText("1번 진행률 : " +sb2.getProgress()+"%");  //6.텍스트뷰에 각 쓰레드의 진행상황을 표기되도록 작성
//                        }
//                    }
//                }.start();

                Thread thread = new Thread(new Runnable() {  //5. 익명이 아닌이름을 주어 생성
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sb2.setProgress(sb2.getProgress()+1);
                                    tv2.setText("2번 진행률 : " +sb2.getProgress()+"%");  //6.텍스트뷰에 각 쓰레드의 진행상황을 표기되도록 작성
                                }
                            });
                            SystemClock.sleep(100);
                        }
                    }
                });
                thread.start();






            }
        });
    }
}

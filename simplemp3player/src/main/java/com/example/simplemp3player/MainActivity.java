package com.example.simplemp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewMp3;
    Button btnPlay, btnStop, btnMiniStop;

    TextView tvPlayName, tvTime;
    //    ProgressBar pbPlayStatus;
    SeekBar seekBar1;
    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");   //SimpleDateFormat 형식을 주는 클래스
    static boolean PAUSED = false;


    //1.manifests 폴더안의 AndroidManifest.xml 열기     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>   추가


    ArrayList<String> mp3List;   //2. 동적 배열 선언
    String selectedMP3;  //3. 노래 선택 시 하단의 textView 에 뜰 수 있도록 적용하는 변수
    //    String sdcardpath = Environment.getExternalStorageDirectory().getPath()+"/music";  //절대경로 (나는 sd카드를 사용하기 때문에, 위의 변수로 사용
    String sdcardpath = "/storage/2A2B-DA07/music/"; //4.경로 변수 선언    ---> 이건 나중에 선언해도 된다.
    MediaPlayer mp;   //음악재생하는 클래스


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMp3 = (ListView) findViewById(R.id.listViewMp3);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnMiniStop = (Button) findViewById(R.id.btnMiniStop);
        tvPlayName = (TextView) findViewById(R.id.tvPlayName);
        tvTime = (TextView) findViewById(R.id.tvTime);
//        pbPlayStatus = (ProgressBar)findViewById(R.id.pbPlayStatus);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);

        int parmissimCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);  //5.6.0 버전이후 보안으로 인해 이중으로 설정해야 한다.

        if (parmissimCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        } else {
            sdcardProcess();  //8.메소드 로 이동 될 수 있도록 메소드 설정
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {  //  boolean  fromUser   = 유저가 시크바를 움직이면 그 값을 저 파라멘타 변수에 보내준다. int progress-시크바를 움직인 값을 저 파라멘타 변수에 보내준다.
                if(fromUser) {//현재는 true값이 들어옴--유저가 강제로 시크바를 움직였다.
                    mp.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        listViewMp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //16. 리스트에서 선택을 하면 수행하는 메소드 생성
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMP3 = mp3List.get(position);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(selectedMP3.equals("")) {
//                    Toast.makeText(getApplicationContext(),"듣고싶은 음악을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
//                }

                try {
                    mp = new MediaPlayer();  //인스턴스 객체 생성
                    mp.setDataSource(sdcardpath + selectedMP3);
                    mp.prepare();
                    mp.start();
                    btnMiniStop.setEnabled(true);
                    btnStop.setEnabled(true);
                    btnPlay.setEnabled(false);
                    tvPlayName.setText("실행중인 음악 : " + selectedMP3);

                    makeThread();
//                    pbPlayStatus.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "노래를 재생 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnMiniStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(PAUSED==false) {
//                    mp.pause();
//                    btnMiniStop.setText("이어 듣기");
//                    PAUSED=true;
//                    pbPlayStatus.setVisibility(View.INVISIBLE);
//
//                }else {
//                    mp.start();
//                    btnMiniStop.setText("일시 정지");
//                    PAUSED=false;
//                    pbPlayStatus.setVisibility(View.VISIBLE);
//                }

                if (btnMiniStop.getText().toString().equals("일시 정지")) {
                    mp.pause();
                    btnMiniStop.setText("이어 듣기");
                    PAUSED = true;
//                    pbPlayStatus.setVisibility(View.INVISIBLE);
                } else {
                    mp.start();
                    btnMiniStop.setText("일시 정지");
                    PAUSED = false;
                        makeThread();
                }

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.reset();
                btnPlay.setEnabled(true);
                btnStop.setEnabled(false);
                tvPlayName.setText("실행중인 음악 : ");
//                    pbPlayStatus.setVisibility(View.INVISIBLE);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {    //6. 처음 퍼미션 체크를 허락하면 이 메소드로 온다.(메소드 생성)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sdcardProcess();
    }

    void sdcardProcess() { //7. 이미 퍼미션체크가 완료되있다면, 이 메소드로 넘어온다. (메소드 생성)
        mp3List = new ArrayList<String>();

        File listFiles[] = new File(sdcardpath).listFiles();  //9.배열선언
        Log.i("File_Path", sdcardpath);
        String fileName, extName, extName2;   //9.파일이름,확장자이름 담을 변수 선언

        for (File file : listFiles) { //10.향상된 for문
            fileName = file.getName(); //11.해당경로에 첫번째 들어가 있는 파일
            extName = fileName.substring(fileName.length() - 3); //12.첫번째 파일 이름에서 확장자만 가져옴
            extName2 = fileName.substring(fileName.length() - 4);
            if (extName.equals("mp3") || extName.equals("wav") || extName2.equals("flac") || extName.equals("m4a")) {  // 13. 확장자명이 mp3이면
                mp3List.add(fileName); //14. 동적배열에 추가해준다.
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);//15. 동적배열 생성한다.
        listViewMp3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMp3.setAdapter(adapter);

        listViewMp3.setItemChecked(0, true);
        selectedMP3 = mp3List.get(0);
    }
private void makeThread() {
    Thread thread = new Thread(new Runnable() {  //스레드 생성
        @Override
        public void run() {
            if (mp == null) { //만약 mp  이 mp3등 확장자지만, 음악이 아니라면,
                return; //되돌아간다.
            } else {
                seekBar1.setMax(mp.getDuration());  //mp의 재생시간을 가져온다.
                while (mp.isPlaying()) {//mp가 재생중이라면 수행
                    runOnUiThread(new Runnable() {  //안정적인 ui 스레드
                        @Override
                        public void run() {
                            seekBar1.setProgress(mp.getCurrentPosition());//mp의 현재 재생위치 값만큼 얻어온다.
                            tvTime.setText("진행시간 : "+timeFormat.format(mp.getCurrentPosition()));  //textview 에 현재 진행시간을 보여준다. 보여주는데, timeFormat 클래스(형식을지정)에 안에 설정한다.
                        }
                    });
                }
            }
        }
    });
    thread.start();
}


}

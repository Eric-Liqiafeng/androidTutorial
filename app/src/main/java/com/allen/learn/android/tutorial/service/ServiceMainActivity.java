package com.allen.learn.android.tutorial.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class ServiceMainActivity extends AppCompatActivity {

    private static final int UPDATE_PROGRESS= 0;

    private Button playMusicBtn;
    private SeekBar musicSeekBar;

    private MyForegroundMusicService.MyMusicBinder musicControl;
    private MyConnection myConn;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    updateProgress();
                    break;
            }

        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        init();

        Intent intent = new Intent(this,MyForegroundMusicService.class);
        myConn = new MyConnection();
        startService(intent);
        bindService(intent,myConn,BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //进入到界面后开始更新进度条
        if (musicControl != null){
            handler.sendEmptyMessage(UPDATE_PROGRESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出应用后与service解除绑定
        unbindService(myConn);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止更新进度条的进度
        handler.removeCallbacksAndMessages(null);

    }

   private void init(){
        playMusicBtn = findViewById(R.id.playMusicBtn);
        playMusicBtn.setOnClickListener(v->{
            musicControl.play();
            musicSeekBar.setMax(musicControl.getDuration());
            musicSeekBar.setProgress(musicControl.getCurrentPosition());
            updatePlayText();
        });

        musicSeekBar = findViewById(R.id.musicSeekBar);
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser){
                    musicControl.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void updatePlayText(){
        if (musicControl.isPlaying()) {
            playMusicBtn.setText("Pause Music");
            handler.sendEmptyMessage(UPDATE_PROGRESS);
        } else {
            playMusicBtn.setText("Play Music");
        }

    }


    private void updateProgress() {
        int currentPosition = musicControl.getCurrentPosition();
        musicSeekBar.setProgress(currentPosition);
        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
    }



    private class MyConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MyForegroundMusicService.MyMusicBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

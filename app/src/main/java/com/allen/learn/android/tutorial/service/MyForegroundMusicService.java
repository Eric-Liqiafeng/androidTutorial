package com.allen.learn.android.tutorial.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.allen.learn.android.tutorial.BuildConfig;

import java.io.File;
import java.io.IOException;

public class MyForegroundMusicService extends Service {

    private String filePath = "/netease/cloudmusic/Music/Against the Current - Legends Never Die.mp3";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        try {
            String absoluteFilePath = Environment.getExternalStorageDirectory().getPath() + filePath;
            Uri parse = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(absoluteFilePath));
            mediaPlayer.setDataSource(this, parse);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyMusicBinder();
    }


    public class MyMusicBinder extends Binder {

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public void play() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
            Log.e("MyForegroundMusicService", "music start");
        }

        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        public void seekTo(int mesc) {
            mediaPlayer.seekTo(mesc);
        }

    }
}

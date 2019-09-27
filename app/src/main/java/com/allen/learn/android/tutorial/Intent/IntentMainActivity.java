package com.allen.learn.android.tutorial.Intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class IntentMainActivity extends AppCompatActivity {

    private Button goToSubActivityBtn;

    private Button openAlarmBtn;
    private Button openMusicBtn;
    private Button openCustomBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_main);
        goToSubActivityBtn = findViewById(R.id.goToSubActivityBtn);
        goToSubActivityBtn.setOnClickListener(e -> {
            Intent intent = new Intent(IntentMainActivity.this, IntentSubActivity.class);
            startActivity(intent);
        });

        openAlarmBtn = findViewById(R.id.intentAlarmBtn);
        openAlarmBtn.setOnClickListener(e -> {
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                    .putExtra(AlarmClock.EXTRA_MESSAGE, "test")
                    .putExtra(AlarmClock.EXTRA_HOUR, 12)
                    .putExtra(AlarmClock.EXTRA_MINUTES,30);
            if (intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }
        });

        openMusicBtn = findViewById(R.id.intentOpenMusicBtn);
        openMusicBtn.setOnClickListener(e->{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file:\\storage\\emulated\\0\\netease\\cloudmusic\\Music\\Against the Current - Legends Never Die.mp3");
            intent.setDataAndType(uri,"audio/mp3");
            if (intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);
            }
        });

        openCustomBtn = findViewById(R.id.intentCustomBtn);
        openCustomBtn.setOnClickListener(e->{
            Intent intent = new Intent("android.intent.action.test");
            startActivity(intent);
        });


    }
}

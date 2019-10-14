package com.allen.learn.android.tutorial.service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class ServiceMainActivity extends AppCompatActivity {

    private Button goToMusicWithStartedAndBoundServiceBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);

        goToMusicWithStartedAndBoundServiceBtn = findViewById(R.id.goToMusicWithStartedAndBoundServiceBtn);
        goToMusicWithStartedAndBoundServiceBtn.setOnClickListener(v->{
            Intent intent = new Intent(ServiceMainActivity.this,MusicWithStartedAndBoundServiceActivity.class);
            startActivity(intent);
        });

    }
}

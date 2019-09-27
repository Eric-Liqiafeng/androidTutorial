package com.allen.learn.android.tutorial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class ActivityMainActivity extends AppCompatActivity {

    private Button standardActivityBtn;
    private Button singleTopActivityBtn;
    private Button singleTaskActivityBtn;
    private Button singleInstanceActivityBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        standardActivityBtn = findViewById(R.id.standardActivityBtn);
        standardActivityBtn.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, StandardActivity.class);
            startActivity(intent);
        });

        singleTopActivityBtn = findViewById(R.id.singleTopActivityBtn);
        singleTopActivityBtn.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, SingleTopActivity.class);
            startActivity(intent);
        });

        singleTaskActivityBtn = findViewById(R.id.singleTaskActivityBtn);
        singleTaskActivityBtn.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, SingleTaskActivity.class);
            startActivity(intent);
        });

        singleInstanceActivityBtn = findViewById(R.id.singleInstanceActivityBtn);
        singleInstanceActivityBtn.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, SingleInstanceActivity.class);
            startActivity(intent);
        });




    }
}

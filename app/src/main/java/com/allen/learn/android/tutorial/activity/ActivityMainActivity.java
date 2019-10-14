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
    private Button lifecycleComponentActivityBtn;

    private Button goToSaveUIState;
    private Button goToNoSaveUIState;

    private Button goToStartActivityForResult;


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

        lifecycleComponentActivityBtn = findViewById(R.id.lifecycleComponentActivityBtn);
        lifecycleComponentActivityBtn.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, LifecycleComponentActivity.class);
            startActivity(intent);
        });

        goToSaveUIState = findViewById(R.id.goToSaveUIBtn);
        goToSaveUIState.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, SaveUIStateActivity.class);
            startActivity(intent);
        });

        goToNoSaveUIState = findViewById(R.id.goToNoSaveUIState);
        goToNoSaveUIState.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, NoSaveUIActivity.class);
            startActivity(intent);
        });

        goToStartActivityForResult = findViewById(R.id.goToStartActivityForResultBtn);
        goToStartActivityForResult.setOnClickListener(v->{
            Intent intent = new Intent(ActivityMainActivity.this, StartActivityForResultActivity.class);
            startActivity(intent);
        });


    }
}

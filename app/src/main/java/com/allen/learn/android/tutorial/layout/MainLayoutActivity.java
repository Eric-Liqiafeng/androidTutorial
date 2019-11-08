package com.allen.learn.android.tutorial.layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class MainLayoutActivity extends AppCompatActivity {

    private Button toToLinearLayoutBtn;
    private Button toToRelativeLayoutBtn;
    private Button goToFrameLayoutBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_main);

        toToLinearLayoutBtn = findViewById(R.id.goToLinearLayoutBtn);
        toToLinearLayoutBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainLayoutActivity.this, LinearLayoutActivity.class);
            startActivity(intent);
        });

        toToRelativeLayoutBtn = findViewById(R.id.goToRelativeLayoutBtn);
        toToRelativeLayoutBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainLayoutActivity.this, RelativeLayoutActivity.class);
            startActivity(intent);
        });

        goToFrameLayoutBtn = findViewById(R.id.goToFrameLayoutBtn);
        goToFrameLayoutBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainLayoutActivity.this, FrameLayoutActivity.class);
            startActivity(intent);
        });
    }
}

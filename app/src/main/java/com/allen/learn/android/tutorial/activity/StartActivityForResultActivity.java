package com.allen.learn.android.tutorial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class StartActivityForResultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private Button openActivityForResultBtn;
    static final int START_FOR_RESULT_CODE =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_for_result);
        resultTextView = findViewById(R.id.resultTextView);
        openActivityForResultBtn = findViewById(R.id.openActivityForResult);
        openActivityForResultBtn.setOnClickListener(e->{
            Intent intent = new Intent(StartActivityForResultActivity.this,ResultActivity.class);
            startActivityForResult(intent,START_FOR_RESULT_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==START_FOR_RESULT_CODE){
            if (resultCode==-2){
            resultTextView.setText(data.getExtras().getCharSequence("resultText"));
            }
        }
    }
}

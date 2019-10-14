package com.allen.learn.android.tutorial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class ResultActivity extends AppCompatActivity {

    private EditText editText;
    private Button goBackToStartForResultBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        editText = findViewById(R.id.forResultEditText);
        goBackToStartForResultBtn = findViewById(R.id.goBackToStartForResultBtn);
        goBackToStartForResultBtn.setOnClickListener(e->{
            Intent intent = new Intent();
            intent.putExtra("resultText",editText.getText());
            setResult(RESULT_OK,intent);
            finish();
        });


    }



}

package com.allen.learn.android.tutorial.Intent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

public class IntentSubActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_sub);
        button = findViewById(R.id.goBackToMainActivityBtn);
        button.setOnClickListener(e->{
            Intent intent = new Intent(IntentSubActivity.this,IntentMainActivity.class);
            startActivity(intent);
        });

    }
}

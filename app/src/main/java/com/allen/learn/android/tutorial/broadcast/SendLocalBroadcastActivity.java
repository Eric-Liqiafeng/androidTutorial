package com.allen.learn.android.tutorial.broadcast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.allen.learn.android.tutorial.R;
import com.allen.learn.android.tutorial.util.Constant;

public class SendLocalBroadcastActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);
        button = findViewById(R.id.sendLocalBroadcastBtn);
        button.setOnClickListener(e->{
            Intent intent = new Intent();
            intent.setAction(Constant.LOCAL_BROADCASE_TEST);
            intent.putExtra("data","Notice me senpai!");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        });
    }
}

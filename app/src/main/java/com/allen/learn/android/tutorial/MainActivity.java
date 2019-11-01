package com.allen.learn.android.tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.allen.learn.android.tutorial.Intent.IntentMainActivity;
import com.allen.learn.android.tutorial.activity.ActivityMainActivity;
import com.allen.learn.android.tutorial.fragment.MainFragment;
import com.allen.learn.android.tutorial.service.MusicWithStartedAndBoundServiceActivity;
import com.allen.learn.android.tutorial.service.ServiceMainActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToIntentBtn;

    private Button goToActivityBtn;

    private Button gotToServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToIntentBtn= findViewById(R.id.goToIntentBtn);
        goToIntentBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, IntentMainActivity.class);
            startActivity(intent);
        });

        goToActivityBtn = findViewById(R.id.goToActivityBtn);
        goToActivityBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ActivityMainActivity.class);
            startActivity(intent);
        });

        gotToServiceBtn = findViewById(R.id.goToserviceBtn);
        gotToServiceBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ServiceMainActivity.class);
            startActivity(intent);
        });

        MainFragment mainFragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,mainFragment);
        fragmentTransaction.commit();
    }
}

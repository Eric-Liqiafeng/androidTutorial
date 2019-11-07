package com.allen.learn.android.tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;

import com.allen.learn.android.tutorial.Intent.IntentMainActivity;
import com.allen.learn.android.tutorial.activity.ActivityMainActivity;
import com.allen.learn.android.tutorial.broadcast.MyLocalBroadcastReceiver;
import com.allen.learn.android.tutorial.broadcast.SendLocalBroadcastActivity;
import com.allen.learn.android.tutorial.layout.MainLayoutActivity;
import com.allen.learn.android.tutorial.service.ServiceMainActivity;
import com.allen.learn.android.tutorial.util.Constant;

public class MainActivity extends AppCompatActivity {

    private Button goToIntentBtn;

    private Button goToActivityBtn;

    private Button gotToServiceBtn;
    private Button gotToBroadcastBtn;
    private Button goToLayoutBtn;

    private MyLocalBroadcastReceiver myLocalBroadcastReceiver;

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

        gotToServiceBtn = findViewById(R.id.goToServiceBtn);
        gotToServiceBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ServiceMainActivity.class);
            startActivity(intent);
        });

        gotToBroadcastBtn = findViewById(R.id.goToBroadcastBtn);
        gotToBroadcastBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SendLocalBroadcastActivity.class);
            startActivity(intent);
        });

//        MainFragment mainFragment = new MainFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.container,mainFragment);
//        fragmentTransaction.commit();

        goToLayoutBtn = findViewById(R.id.goToLayoutBtn);
        goToLayoutBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, MainLayoutActivity.class);
            startActivity(intent);
        });


        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.LOCAL_BROADCASE_TEST);
        myLocalBroadcastReceiver = new MyLocalBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }
}

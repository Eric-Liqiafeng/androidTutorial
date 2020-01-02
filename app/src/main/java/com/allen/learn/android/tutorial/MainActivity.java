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
import com.allen.learn.android.tutorial.notification.NotificationActivity;
import com.allen.learn.android.tutorial.service.ServiceMainActivity;
import com.allen.learn.android.tutorial.util.Constant;
import com.allen.learn.android.tutorial.view.adapater.widget.ListViewActivity;
import com.allen.learn.android.tutorial.view.SimpleWidgetActivity;
import com.allen.learn.android.tutorial.view.adapater.widget.RecyclerViewActivity;
import com.allen.learn.android.tutorial.view.adapater.widget.TabLayoutActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToIntentBtn;

    private Button goToActivityBtn;

    private Button gotToServiceBtn;
    private Button gotToBroadcastBtn;
    private Button goToLayoutBtn;
    private Button goToNotificationBtn;
    private Button goToSimpleWidgetBtn;

    private Button goToListViewBtn;
    private Button goToRecyclerViewBtn;
    private Button goToTabLayoutBtn;
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

        goToNotificationBtn = findViewById(R.id.goToNotificationBtn);
        goToNotificationBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        goToSimpleWidgetBtn = findViewById(R.id.goToSimpleWidgetBtn);
        goToSimpleWidgetBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SimpleWidgetActivity.class);
            startActivity(intent);
        });

        goToListViewBtn = findViewById(R.id.goToListViewBtn);
        goToListViewBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
            startActivity(intent);
        });

        goToRecyclerViewBtn = findViewById(R.id.goToRecyclerViewBtn);
        goToRecyclerViewBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
            startActivity(intent);
        });

        goToTabLayoutBtn = findViewById(R.id.goToTabLayoutBtn);
        goToTabLayoutBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
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

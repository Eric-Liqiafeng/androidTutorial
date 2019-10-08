package com.allen.learn.android.tutorial.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LifeCycleActivity extends AppCompatActivity {
    
    String TAG = this.getClass().getName();

    private TextView textView;
    private Button button;
    private Button goToSelfbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(this.getClass().getSimpleName());
        container.addView(textView,params);


        button = new Button(this);
//        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setText("go to Activity Main");
        button.setOnClickListener(v->{
            Intent intent = new Intent(this,ActivityMainActivity.class);
            startActivity(intent);
        });
        container.addView(button);

//        goToSelfbutton = new Button(this);
//        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        button.setLayoutParams(params1);
//        button.setText("go to Activity Main");
//        button.setOnClickListener(v->{
//            Intent intent = new Intent(this,ActivityMainActivity.class);
//            startActivity(intent);
//        });
//        container.addView(button);

        setContentView(container,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        Log.d(TAG, "onCreate:");
        Log.d(TAG, getLifecycle().getCurrentState().name());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
        Log.d(TAG, getLifecycle().getCurrentState().name());
    }
}

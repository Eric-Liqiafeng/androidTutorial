package com.allen.learn.android.tutorial.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class LifecycleComponentActivity extends LifeCycleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new MyObserver());
    }
}

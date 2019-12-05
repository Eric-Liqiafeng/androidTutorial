package com.allen.learn.android.tutorial.view.adapater.widget;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.learn.android.tutorial.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private ListView listView;
    private AnimalAdapter animalAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = findViewById(R.id.lv);
        List<Animal> animalList = initData();
        animalAdapter = new AnimalAdapter(this, animalList);
        listView.setAdapter(animalAdapter);

    }


    List<Animal> initData(){
        List<Animal> animalList = new ArrayList<>();
        for (int i = 1;i<200;i++){
            animalList.add(new Animal(String.valueOf(i),i));
        }
        return animalList;
    }

}

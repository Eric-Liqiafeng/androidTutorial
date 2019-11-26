package com.allen.learn.android.tutorial.view;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.learn.android.tutorial.R;

public class SimpleWidgetActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;
    private ImageButton imageButton;

    private SeekBar seekBar;

//    private

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_widget);

        buildToolbar();

        textView = findViewById(R.id.simpleWidgetTextView);
        textView.setHint("hints");

        editText = findViewById(R.id.simpleWidgetEditText);

        button = findViewById(R.id.simpleWidgetButton);
        button.setOnClickListener(v-> {
            Toast.makeText(this, "button", Toast.LENGTH_LONG).show();
        });

        imageButton = findViewById(R.id.simpleWidgetImageButton);
        imageButton.setOnClickListener(v-> {
            Toast.makeText(this, "image button", Toast.LENGTH_LONG).show();
        });

        seekBar = findViewById(R.id.simpleWidgetSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(SimpleWidgetActivity.this, "seek bar"+progress, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void buildToolbar() {
        Toolbar mToolbar = findViewById(R.id.tl_costom);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_manage);
        mToolbar.setOnMenuItemClickListener(item -> {
            Toast.makeText(SimpleWidgetActivity.this, "menu id:"+item.getTitle()+" be clicked", Toast.LENGTH_LONG).show();
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_widget_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}

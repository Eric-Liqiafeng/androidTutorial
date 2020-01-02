package com.allen.learn.android.tutorial.view.adapater.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allen.learn.android.tutorial.R;
import com.google.android.material.tabs.TabLayout;

public class TabLayoutActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            TextView tv = new TextView(TabLayoutActivity.this);
            tv.setTextSize(30.f);
            tv.setText(mData[position]);
            ((ViewPager) container).addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };


    private String[] titles = new String[]{"最新","热门","我的"};
    private String[] mData = new String[]{"最新页","热门页","我的页"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        init();
    }

    private void init() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        for(int i=0;i<titles.length;i++){
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(titles[i]);
            tabLayout.addTab(tab);
        }
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        final TabLayout.TabLayoutOnPageChangeListener listener =
//                new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
//        viewPager.addOnPageChangeListener(listener);

    }
}

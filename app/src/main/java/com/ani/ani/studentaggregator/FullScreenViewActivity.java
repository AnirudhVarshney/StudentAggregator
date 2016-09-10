package com.devionlabs.ray.studentaggregator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class FullScreenViewActivity extends Activity{
    private Utils utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        String id=i.getStringExtra("Id");
        Log.d("mylog","fsva pos "+position);
        utils = new Utils(getApplicationContext(),id);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                utils.getFilePaths(),id);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
        Log.d("mylog","fsva last"+position);
    }
}


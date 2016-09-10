package com.devionlabs.ray.studentaggregator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    String id;
    static final String FOLDER_NAME = "studentaggregator";

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths, String Id) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.id = Id;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imgDisplay;
        final Button btnsave;
        final Button btnClose;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);


        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        btnsave = (Button) viewLayout.findViewById(R.id.save);
        Glide.with(_activity).load(_imagePaths.get(position)).into(imgDisplay);

        btnsave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisplay.buildDrawingCache();
                Bitmap bm = imgDisplay.getDrawingCache();
                OutputStream fOut = null;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + FOLDER_NAME + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "event " + id + "_" + position + ".png");
                    fOut = new FileOutputStream(sdImageMainDirectory);
                } catch (Exception e) {
                    Toast.makeText(_activity, "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }

                try {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                }

                btnsave.setVisibility(View.GONE);
                Toast.makeText(_activity, "Image saved to " + FOLDER_NAME + " folder",
                        Toast.LENGTH_SHORT).show();
            }
        }));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}

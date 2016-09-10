package com.devionlabs.ray.studentaggregator;

/**
 * Created by ABHINAV on 24-05-2016.
 */


import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Locale;

public class Utils {
    ImageView mImage;
    String url1_4="http://studentaggregator.org/events/4_1.png";
    String url2_4="http://studentaggregator.org/events/4_2.png";
    String url3_4="http://studentaggregator.org/events/4_3.png";
    String url4_4="http://studentaggregator.org/events/4_5.png";
    // String url1_1="http://studentaggregator.org/events/1_1.png";
    String url1_1="http://studentaggregator.org/events/1_1.png";
    String url2_1="http://studentaggregator.org/events/1_2.png";
    String url3_1="http://studentaggregator.org/events/1_3.png";
    String url4_1="http://studentaggregator.org/events/1_4.png";
    String url5_1="http://studentaggregator.org/events/1_5.png";
    String[] urls = {url1_1, url2_1, url3_1, url4_1,url5_1};
    String[] urls1={url1_4,url2_4,url3_4,url4_4};
    private Context _context;
    String _id;
    // constructor
    public Utils(Context context,String id) {
        this._context = context;
        this._id=id;
        Log.d("aaa","id"+_id);
    }

    // Reading file paths from SDCard
    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();
        if (_id.equals("1")) {
            String[] urls = {url1_1, url2_1, url3_1, url4_1, url5_1};
            for (int i = 0; i < urls.length; i++) {

                // get file path
                String filePath = urls[i];

                // check for supported file extension
                // Add image path to array list
                filePaths.add(filePath);
            }
        }
        else if(_id.equals("4")) {
            String[] urls1 = {url1_4, url2_4, url3_4, url4_4};
            for (int i = 0; i < urls1.length; i++) {
                // get file path
                String filePath = urls1[i];
                // check for supported file extension
                // Add image path to array list
                filePaths.add(filePath);
            }
        }
        return filePaths;
    }


    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstant.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
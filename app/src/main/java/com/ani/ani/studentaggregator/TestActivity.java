package com.devionlabs.ray.studentaggregator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.devionlabs.ray.studentaggregator.helper.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

public class TestActivity extends AppCompatActivity {

    private static String FILE_URL = "http://studentaggregator.org/downloadfeerecipt.php";
    private static String FOLDER_NAME = "studentaggregator";
    private static String FILE_NAME = "Fee_Recipt.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }

    public void download() {
        GregorianCalendar date = new GregorianCalendar();
        new DownloadFile().execute(FILE_URL, FILE_NAME);
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, FOLDER_NAME);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

}

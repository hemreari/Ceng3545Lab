package com.example.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    EditText txtUrl;
    Button btnDownload;
    ImageView imgView;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUrl = (EditText) findViewById(R.id.txtURL);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        imgView = (ImageView) findViewById(R.id.imgView);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                );
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }

                DownloadTask task = new DownloadTask();
                String[] urls = new String[1];
                urls[0] = txtUrl.getText().toString();
                task.execute(urls);
            }
        });
    }

    class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String fileName = "temp.jpg";
            String imgPath = (Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS)).toString() + "/" + fileName;
            downloadFile(strings[0], imgPath);
            Bitmap image = BitmapFactory.decodeFile(imgPath);
            float w = image.getWidth();
            float h = image.getHeight();
            int W = 400;
            int H = (int) ( (h*W)/w);
            Bitmap b = Bitmap.createScaledBitmap(image, W, H, false); return b;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            imgView.setImageBitmap(b);
        }

        void downloadFile(String strUrl, String imgPath) {
            try {
                URL url = new URL(strUrl);
                URLConnection conn = url.openConnection();
                conn.connect();
                int fileSize = conn.getContentLength();
                InputStream inStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outStream = new FileOutputStream(imgPath);
                byte data[] = new byte[1024];

                long total = 0;
                int count;
                while ((count = inStream.read(data)) != -1) {
                    outStream.write(data, 0, count);
                    total += count;

                    publishProgress((int)((total*100)/fileSize));
                }
                outStream.flush();
                outStream.close();
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void preview(String imgPath) {
        Bitmap img = BitmapFactory.decodeFile(imgPath);
        float width = img.getWidth();
        float height = img.getHeight();
        int W = 400;
        int H = (int) ((height * W)/width);
        Bitmap bitmap = Bitmap.createScaledBitmap(img, W, H, false);
        imgView.setImageBitmap(bitmap);
    }
}
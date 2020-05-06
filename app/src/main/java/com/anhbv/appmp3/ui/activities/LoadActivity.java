package com.anhbv.appmp3.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anhbv.appmp3.R;
import com.anhbv.appmp3.constant.RequestCode;

import java.io.File;

public class LoadActivity extends AppCompatActivity {
    private ImageView imvLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        createFolder();
    }

    public void createFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() + "/Zing MP3");
        boolean success = true;
        if (!folder.exists()){
            success = folder.mkdir();
        }
        if (success){

        }else {

        }
    }

    public void checkPermission() {
        if (!isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        permission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public boolean isGranted(String permission) {
        if (isMarshMallow()) {
            return ContextCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private boolean isMarshMallow() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    private void permission(String... strings) {
        ActivityCompat.requestPermissions(this, strings, RequestCode.REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestCode.REQUEST_CODE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    finish();
                }
                return;
            }

        }
    }
}

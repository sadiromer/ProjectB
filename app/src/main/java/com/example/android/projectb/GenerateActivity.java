package com.example.android.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GenerateActivity extends AppCompatActivity {

    //Define Variables
    private static final int REQUEST_ID = 1;
    private static final int HALF = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
    } //onCreate


    /**
     * Method to create a 2nd activity to Generate QR code
     */
    public void ImageDecodeButton (View v) {
        Intent intent = new Intent(GenerateActivity.this, GenerateImageActivity.class);
        startActivity(intent);
    }

    public void FileDecodeButton (View v) {
        Intent intent2 = new Intent(GenerateActivity.this, GenerateFileActivity.class);
        startActivity(intent2);
    }

    public void TestButton (View v) {
        Intent intent2 = new Intent(GenerateActivity.this, TestActivity.class);
        startActivity(intent2);
    }

} //GenerateActivity

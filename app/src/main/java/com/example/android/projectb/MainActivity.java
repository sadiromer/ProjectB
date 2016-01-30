package com.example.android.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Define Variables here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } //onCreate

//-----------------------------------------------------------
//MY CODE STARTS FROM HERE-----------------------------------
//-----------------------------------------------------------


    /**
     * Method to create a 2nd activity to Generate QR code
     */
    public void generateButton (View v) {
        Intent intent = new Intent(MainActivity.this, GenerateActivity.class);
        startActivity(intent);
    }

    public void scanButton (View v) {
        Intent intent2 = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent2);
    }

} //MainActivity


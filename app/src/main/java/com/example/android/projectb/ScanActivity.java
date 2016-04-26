package com.example.android.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    //-----------------------------------------------------------
//MY CODE STARTS FROM HERE-----------------------------------
//-----------------------------------------------------------


    /**
     * Method to create a 2nd activity to Generate QR code
     */
    public void buttonDecodeIntent (View v) {
        Intent intent = new Intent(ScanActivity.this, ScanIntentActivity.class);
        startActivity(intent);
    }

    public void buttonDecodeContinuous (View v) {
        Intent intent2 = new Intent(ScanActivity.this, ScanContinousActivity.class);
        startActivity(intent2);
    }


    public void buttonRecord (View v) {
        Intent intent3 = new Intent(ScanActivity.this, CameraRecordActivity.class);
        startActivity(intent3);
    }

    //--------------------------------------------------------------------------------------

    //Go to surfaceview

    public void recordButtonSurfaceView (View view){
        Intent intent2 = new Intent(ScanActivity.this, ScanCameraAcivity.class);
        startActivity(intent2);
    }

}

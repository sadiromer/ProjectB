package com.example.android.projectb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GenerateFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_file);
    }//onCreate

    /*   public void browseButton(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivity(intent);
        }
        */

}

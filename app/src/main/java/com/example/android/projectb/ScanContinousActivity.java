package com.example.android.projectb;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ScanContinousActivity extends AppCompatActivity {

    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_continous);

    }


    @Override
    protected void onStart() {
        super.onStart();
        Button recordButton =
                (Button) findViewById(R.id.recordButton);

        if (!hasCamera())
            recordButton.setEnabled(false);

    }

    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        } else {
            return false;
        }
    }

    public void startRecording(View view) {
        File mediaFile = new
                File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Capture" + File.separator + "/myvideo.mp4");

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startExtracting(View view) throws IOException {
        File videoFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Capture/","myvideo.mp4");

        Uri videoFileUri=Uri.parse(videoFile.toString());

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile.getAbsolutePath());
        ArrayList<Bitmap> rev=new ArrayList<Bitmap>();

        //Create a new Media Player
        MediaPlayer mp = MediaPlayer.create(getBaseContext(), videoFileUri);

        int millis = mp.getDuration();

        for(int i=1000000;i<millis*1000;i+=1000000)
        {
            Bitmap bitmap=retriever.getFrameAtTime(i,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            rev.add(bitmap);
        }

        saveFrames(rev);
    }









    public void saveFrames(ArrayList<Bitmap> saveBitmapList) throws IOException {


        String folder = Environment.getExternalStorageDirectory()+"/Capture/frames/";
        File saveFolder=new File(folder);
        if(!saveFolder.exists()){
            saveFolder.mkdirs();
        }

        int i=1;
        for (Bitmap b : saveBitmapList){
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            File f = new File(saveFolder,("frame"+i+".jpg"));

            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            fo.flush();
            fo.close();

            i++;
        }
        Toast.makeText(getApplicationContext(),"Frames Generated", Toast.LENGTH_LONG).show();

    }

    public void recordButtonSurfaceView (View view){
        Intent intent2 = new Intent(ScanContinousActivity.this, ScanCameraAcivity.class);
        startActivity(intent2);
    }
    }

package com.example.android.projectb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class GenerateFileActivity extends AppCompatActivity {

    private static final int REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_file);
    }//onCreate

      /* public void browseFileButton(View v) {
            Intent intentFile = new Intent();
            intentFile.setAction(Intent.ACTION_GET_CONTENT);
            intentFile.setType("file/*");
            startActivity(intentFile);
           startActivityForResult(intentFile, REQUEST_ID);
        }
*/



    public void browseFileButton(View v) {
        Intent intentImage = new Intent();
        intentImage.setAction(Intent.ACTION_GET_CONTENT);
        intentImage.addCategory(Intent.CATEGORY_OPENABLE);
        intentImage.setType("image/*");
        startActivityForResult(intentImage, REQUEST_ID);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_ID && resultCode == Activity.RESULT_OK) {
            try {
                Uri uri = data.getData(); //path of the image file chosen
                stream = getContentResolver().openInputStream(uri); //streams in the image
                Bitmap original = BitmapFactory.decodeStream(stream); //converts the image to bitmap
                //((ImageView)findViewById(R.id.image_holder)).setImageBitmap(Bitmap.createScaledBitmap(original,
                //original.getWidth() / HALF, original.getHeight() / HALF, true));

                //adding me code to convert to base64
                String Base64 = encodeTobase64(original);

                /*ByteArrayOutputStream stream2 = QRCode.from(Base64).stream();
                Bitmap myBitmap = QRCode.from(Base64).bitmap();
                ImageView myImage = (ImageView) findViewById(R.id.fileToQRImage);
                myImage.setImageBitmap(myBitmap);*/

                //Splitting the base64 strings into parts
                String Base64Parts[] = splitInParts(Base64, 1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }


    //Used to split strings
    public String[] splitInParts(String s, int partLength) {
        int len = s.length();

        // Number of parts
        int nparts = (len + partLength - 1) / partLength;
        String parts[] = new String[nparts];

        // Break into parts
        int offset = 0;
        int i = 0;
        while (i < nparts) {
            parts[i] = s.substring(offset, Math.min(offset + partLength, len));
            offset += partLength;
            i++;
        }

        return parts;
    }

}

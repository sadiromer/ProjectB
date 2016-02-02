package com.example.android.projectb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;

public class GenerateImageActivity extends AppCompatActivity {

    //Define Variables
    private static final int REQUEST_ID = 1;
    private static final int HALF = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_image);
    }


    public void browseButton(View v) {
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

                //Splitting the base64 strings into parts
                int splitStringLength = 1000;
                String Base64Parts[] = splitInParts(Base64, splitStringLength);

                //Set it in textview
                TextView displayView = (TextView) findViewById(R.id.base64text);
                displayView.setMovementMethod(new ScrollingMovementMethod());
                displayView.setText(String.valueOf(Base64));
                //displayView.setText(String.valueOf(Base64Parts[1]));

                //Getting the length of the string and displaying it
                int length = Base64.length();

                //Total number of parts being splitted into
                int numberOfPartsSplit = (length % splitStringLength + 1);

                //int length = Base64Parts[1].length();
                TextView displayView2 = (TextView) findViewById(R.id.base64details);
                displayView2.setText(String.valueOf(length));


                //Generate QR code

                QRCodeWriter writer = new QRCodeWriter();

                //starting on the for loop
                //for (int i = 0; i < bitmapArray.size(); i++)
                for (int i = 0; i < numberOfPartsSplit; i++)
                {
                    // in your code change Base64Parts[0] to Base64Parts[i]
                    // also make sure you use 'i' in the filename of your output imag
                    try {
                        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                        BitMatrix bitMatrix = writer.encode(Base64Parts[i], BarcodeFormat.QR_CODE, 512, 512, hintMap);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        ((ImageView) findViewById(R.id.image_holder)).setImageBitmap(bmp);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
            }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }//onActivityResult

    //The following is to encode the the image to Base64

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    //The following is to decode. CAn use it in the decoding part
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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


    //
    //
    //
    //
    //


    //
    //
    //
}

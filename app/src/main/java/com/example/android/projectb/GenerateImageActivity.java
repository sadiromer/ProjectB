package com.example.android.projectb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class GenerateImageActivity extends AppCompatActivity {

    //Define Variables
    private static final int REQUEST_ID = 1;
    private static final int HALF = 2;
    public int number = 0;
    public EditText picture;
    public ArrayList<Bitmap> bmp_images = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_image);
        picture = (EditText) findViewById(R.id.pictureNumber);
    }

    //Using this to search only for Image Types
    public void browseButton(View v) {
        Intent intentImage = new Intent();
        intentImage.setAction(Intent.ACTION_GET_CONTENT);
        intentImage.addCategory(Intent.CATEGORY_OPENABLE);
        intentImage.setType("image/*");
        startActivityForResult(intentImage, REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream imageStream = null;
        if (requestCode == REQUEST_ID && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData(); //path of the image file chosen
                imageStream = getContentResolver().openInputStream(selectedImage); //streams in the image
                Bitmap bmpImage = BitmapFactory.decodeStream(imageStream); //converts the image to bitmap

                //adding me code to convert to base64
                String Base64 = encodeTobase64(bmpImage);

                //Splitting the base64 strings into parts
                int splitStringLength = 1000; //Number of parts base64 is to be split
                ArrayList<String> Base64Parts = splitEqually(Base64, splitStringLength);

                //Set it in textview
                TextView displayView = (TextView) findViewById(R.id.base64text);
                displayView.setMovementMethod(new ScrollingMovementMethod());
                displayView.setText(String.valueOf(Base64));
                displayView.setTextIsSelectable(true);

                //Getting the length of the string and displaying it
                int length = Base64.length();

                //Total number of parts being split into
                int numberOfPartsSplit = (length / splitStringLength) + 1;

                //int length = Base64Parts[1].length();
                TextView displayView2 = (TextView) findViewById(R.id.base64details);
                displayView2.setText(String.valueOf(length));

                TextView displayView3 = (TextView) findViewById(R.id.base64details2);
                displayView3.setText(String.valueOf(numberOfPartsSplit));


                //-------------------Generating a QR Code-------------------------------------------

                //Declaring QR code generator
                QRCodeWriter writer = new QRCodeWriter();

                /*
                //Declaring Array
                ArrayList<Bitmap> bmp_images = new ArrayList<Bitmap>();
                */


                //For loop for generating multiple QR codes
                for (int i = 0; i <= numberOfPartsSplit; i++) {
                    try {
                        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                        BitMatrix bitMatrix = writer.encode(Base64Parts.get(i), BarcodeFormat.QR_CODE, 512, 512, hintMap);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                        //Creating the bitmap matrix for QR code Image
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }//for loop to generate QR Code Bitmap

                        //the code added for arraylist of images
                        bmp_images.add(i, bmp);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }//forloop
                //----------------------------------------------------------------------------------


                //-------------------Generating a GIF file------------------------------------------
                ByteArrayOutputStream byteOutStreamGIF = new ByteArrayOutputStream();
                AnimatedGifEncoder encoderGIF = new AnimatedGifEncoder();
                encoderGIF.start(byteOutStreamGIF);

                for (int j = 0; j < numberOfPartsSplit; j++) {
                    encoderGIF.addFrame(bmp_images.get(j));
                }
                encoderGIF.finish();

                OutputStream fOut = null;
                Uri outputFileUri;
                try {
                    File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Capture" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "myPicName.gif");
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);
                    fOut.write(byteOutStreamGIF.toByteArray());
                    fOut.flush();
                    fOut.close();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                //----------------------------------------------------------------------------------


                /*
                //-------------------Generating GIF Animation---------------------------------------
                AnimationDrawable animDrawable = new AnimationDrawable();
                Drawable startFrame = (BitmapDrawable)getResources().getDrawable(R.drawable.start_frame);
                animDrawable.addFrame(startFrame, 1600);

                for (int k = 0; k < numberOfPartsSplit; k++) {
                    Drawable frame = new BitmapDrawable(bmp_images.get(k));
                    animDrawable.addFrame(frame, 1600);
                }


                ImageView imageAnim = (ImageView) findViewById(R.id.image_holder);
                imageAnim.setBackgroundDrawable(animDrawable);
                animDrawable.start();
                //----------------------------------------------------------------------------------
                */

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }//if as requested from button
    }//onActivityResult

    //-------------------Generating Scroll Image----------------------------------------

    public void okButton (View view){
        number = Integer.parseInt(picture.getText().toString());
        Bitmap image = bmp_images.get(number);

        ImageView imageAnim = (ImageView) findViewById(R.id.image_holder);
        imageAnim.setImageBitmap(image);
    }
    //----------------------------------------------------------------------------------


    //___________________________________FUNCTIONS USED_____________________________________________

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


    //Function to Split String
    public static ArrayList<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with. You could use an array
        // instead if you wanted.
        ArrayList<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }


    //___________________________________FUNCTIONS USED_____________________________________________

}//GenerateImageActivity

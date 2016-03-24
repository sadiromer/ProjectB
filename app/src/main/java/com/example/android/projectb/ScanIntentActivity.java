package com.example.android.projectb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class ScanIntentActivity extends AppCompatActivity {

    //Variable defined
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_intent);
    }


    //-----------------------------------------------------------

//MY CODE STARTS FROM HERE-----------------------------------
//-----------------------------------------------------------

    /**
     * This method is called when the Scan Button is clicked
     * Uses Intent to find a Zxing Scanning application, and goes to that appliccation
     */
    public void buttonDecode(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(ScanIntentActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }//Scan QR button

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");

                displayForQR(contents,format,errorCorrectionLevel);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    } //public void onActivityResults


    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


    public void displayForQR(String qrData, String format, String errorCorrectionLevel) {
        TextView displayView = (TextView) findViewById(R.id.decodeData);
        displayView.setMovementMethod(new ScrollingMovementMethod());


        String qrSummary = "Format: " + format;
        qrSummary += "\nError Correction Level: " + errorCorrectionLevel;
        qrSummary += "\n\nReceive Data: \n";
        qrSummary += qrData;
        displayView.setText(String.valueOf(qrSummary));
    }//public void displayForQR

}//ScanIntentActivity

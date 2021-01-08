package hussainshaikh.com.hussainqrcodegeneraterorscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button scan,create;
    private long backPressed;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = (Button) findViewById(R.id.scan_btn);
        create = (Button) findViewById(R.id.create_btn);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });


        //below for qr code generate
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateQR.class);
                startActivity(intent);
            }
        });


    }

    //for scanned result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null && result.getContents() != null){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Your Scan Result")
                    .setMessage(result.getContents())
                    .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("result",result.getContents());
                            clipboardManager.setPrimaryClip(clipData);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //exit Toast


    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressed = System.currentTimeMillis();
    }
}
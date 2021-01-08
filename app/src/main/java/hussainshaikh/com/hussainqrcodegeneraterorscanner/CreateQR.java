package hussainshaikh.com.hussainqrcodegeneraterorscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateQR extends AppCompatActivity {
    EditText enterText;
    Button createbutton,savebtn;
    ImageView qrImage;
    BitmapDrawable drawable;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_q_r);

        enterText = (EditText) findViewById(R.id.enter_text);
        createbutton = (Button) findViewById(R.id.generate_qrcode);
        qrImage = (ImageView) findViewById(R.id.qr_image);
        savebtn = (Button) findViewById(R.id.save_btn);


        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = enterText.getText().toString();
                if (text!= null && !text.isEmpty()){
                    try {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                        qrImage.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        //This code for Image save
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        SaveQRImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
            }
        });


    }

    private void SaveQRImage() {
        drawable = (BitmapDrawable)qrImage.getDrawable();
        bitmap = drawable.getBitmap();

        FileOutputStream outputStreem = null;

        File SdCard = Environment.getExternalStorageDirectory();
        File directory = new File(SdCard.getAbsolutePath()+ "/HussainQRCodeGenerator");
        directory.mkdir();
        String fileName = String.format("%d.jpg",System.currentTimeMillis());
        File outFile = new File(directory,fileName);

        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();

        try {
            outputStreem = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStreem);
            outputStreem.flush();
            outputStreem.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void Resetbtn(View view) {
        qrImage.setImageResource(R.drawable.img);
        Toast.makeText(this, "Your QR Code has been deleted", Toast.LENGTH_SHORT).show();

    }
}
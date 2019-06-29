package com.xxx.qrapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private Button button;
    private Button scanButton;
    private ImageView image;
    private QRCodeUtil qrCodeUtil;
    private static final int REQUEST_SCAN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button)findViewById(R.id.scanid);
        button = (Button) findViewById(R.id.createid);
        image = (ImageView) findViewById(R.id.qrid);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createQr();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpScanPage();
            }
        });
        getRuntimeRight();
        qrCodeUtil = new QRCodeUtil();
    }

    private void createQr() {
        Bitmap bitmap = qrCodeUtil.createQrBitmap("http://www.baidu.com",300,0);
        if (image != null) {
            image.setImageBitmap(bitmap);
        }
    }

    /**
     * 获得运行时权限
     */
    private void getRuntimeRight() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    /**
     * 跳转到扫码页
     */
    private void jumpScanPage() {
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), REQUEST_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, data.getStringExtra("barCode"), Toast.LENGTH_LONG).show();
        }
    }

}

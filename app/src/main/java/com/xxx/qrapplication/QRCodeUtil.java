package com.xxx.qrapplication;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QRCodeUtil {

    public Bitmap createQrBitmap(String url,int size) {
        Bitmap bitmapQr = null;
        try {
            Hashtable<EncodeHintType,String> hins = new Hashtable<EncodeHintType,String>();
            hins.put(EncodeHintType.CHARACTER_SET,"utf-8");
            //设置容错等级，默认是L
            hins.put(EncodeHintType.ERROR_CORRECTION, ""+ErrorCorrectionLevel.H);
            BitMatrix matrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE,size,size,hins);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width*height];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //优化接口
    public Bitmap createQrBitmap(String url,int size,int padding) {
        Bitmap bitmapQr = null;
        try {
            Hashtable<EncodeHintType,String> hins = new Hashtable<EncodeHintType,String>();
            hins.put(EncodeHintType.CHARACTER_SET,"utf-8");
            BitMatrix matrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE,size,size,hins);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width*height];
            //
            boolean isFirstBlackPoint = false;
            int startX = 0;
            int startY = 0;
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (matrix.get(x, y)) {
                        if (isFirstBlackPoint == false) {
                            isFirstBlackPoint = true;
                            startX = x;
                            startY = y;
                        }
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);

            // 剪切中间的二维码区域，减少padding区域
            if (startX <= padding) {
                return bitmap;
            }

            int x1 = startX - padding;
            int y1 = startY - padding;
            if (x1 < 0 || y1 < 0) {
                return bitmap;
            }

            int w1 = width - x1 * 2;
            int h1 = height - y1 * 2;

            bitmapQr = Bitmap.createBitmap(bitmap, x1, y1, w1, h1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmapQr;
    }
}

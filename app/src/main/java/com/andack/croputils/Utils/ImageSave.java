package com.andack.croputils.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.path;

/**
 * 项目名称：CropUtils
 * 项目作者：anDack
 * 项目时间：2017/1/28
 * 邮箱：    1160083806@qq.com
 * 描述：    图片保存的工具类
 */

public class ImageSave {
    public static void saveImage(Bitmap bmp, Context context) {
        File appDir = new File(Environment.getExternalStorageDirectory(),"ImageUtils");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {

            Log.i("err", "saveImage: "+e.toString());
        } catch (IOException e) {
            Log.i("err", "saveImage: "+e.toString());
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

}

package net.kamfat.omengo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import net.kamfat.omengo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by cjx on 2016/6/1.
 */
public class Tools {
    private static String cachPath;

    /**
     * 获取缓存文件保存的路径
     *
     * @param c
     * @return
     */
    public synchronized static String getCachPath(Context c) {
        if (cachPath == null) {
            cachPath = getDiskCacheDir(c) + "/";
        }
        File f = new File(cachPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return cachPath;
    }

    private static String tempPath;

    public synchronized static String getTempPath(Context c) {
        if (tempPath == null) {
            tempPath = getCachPath(c) + "temp/";
        }
        File f = new File(tempPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return tempPath;
    }

    private static String getDiskCacheDir(Context context) {
        if (context == null) {
            return null;
        }
        boolean hasExternal = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        File cacheDir;
        if (hasExternal) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        if (cacheDir == null) {
            return null;
        }
        return cacheDir.getAbsolutePath();
    }

    private static String expcetionPath;

    public synchronized static String getExceptionPath(Context c) {
        if (expcetionPath == null) {
            expcetionPath = getCachPath(c) + "exception/";
        }
        File f = new File(expcetionPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return expcetionPath;
    }

    public static boolean compressImage(Bitmap src, String savePath, int quality) {
        boolean result = false;
        if (src != null) {
            FileOutputStream fos = null;
            try {
                File f = new File(savePath);
                fos = new FileOutputStream(f);
                result = src.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                src.recycle();
            }
        }
        return result;
    }

    // 将一组日期整理成一个字符串
    public static String formatDate(String[] date) {
        if (date == null) {
            return null;
        }
        if (date.length == 1) {
            return date[0];
        }
        StringBuilder sb = new StringBuilder();
        HashSet<String> keys = new HashSet<>();
        for (String str : date) {
            String key = str.substring(0, 7);
            if (keys.contains(key)) {
                String value = str.substring(8, 10);
                sb.append("、").append(value);
            } else {
                keys.add(key);
                if (sb.length() == 0) {
                    sb.append(str);
                } else {
                    sb.append("/").append(str);
                }
            }
        }

        return sb.toString();
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

    public static void saveToFile(Context context, String filename, String content) {
        File meterCache = new File(getExceptionPath(context), filename + ".txt");
        try {
            FileOutputStream fout = new FileOutputStream(meterCache, true);
            byte[] bytes = content.getBytes();
            fout.write("\n\r\n".getBytes());
            fout.write(("=========== " + sdf.format(new Date()) + "============").getBytes());
            fout.write("\n\r\n".getBytes());
            fout.write(bytes);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 返回服务器图片完整路径
    public static void setImage(Activity context, ImageView imageView, String path) {
        if (context.isFinishing()) {
            return;
        }
        if (!TextUtils.isEmpty(path)) {
            Glide.with(context).load(path).into(imageView);
        } else {
            imageView.setImageBitmap(null);
        }
    }

    // 返回服务器图片完整路径
    public static void setHeadImage(final Activity context, final ImageView imageView, String path) {
        if (context.isFinishing()) {
            return;
        }
        if (!TextUtils.isEmpty(path)) {
            Glide.with(context).load(path).asBitmap().centerCrop().error(R.drawable.user_head).into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            imageView.setImageResource(R.drawable.user_head);
        }
    }
}

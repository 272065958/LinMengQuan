package net.kamfat.omengo.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import net.kamfat.omengo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cjx on 2016/8/30.
 */
public class BitmapDealView extends SurfaceView implements SurfaceHolder.Callback, Handler.Callback {
    HandlerThread thread;
    Handler handler;
    Path drawPath;
    Paint drawPaint;

    String bitmapPath;
    SurfaceHolder holder;
    Bitmap bitmap;

    String name, time;

    public BitmapDealView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                draw();
                break;
            case 1:
                initMatrix();
                break;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("TAG", "surfaceCreated");
        thread = new HandlerThread("draw");
        thread.start();
        handler = new Handler(thread.getLooper(), this);
        if (bitmapPath != null) {
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("TAG", "surfaceChanged " + width + ", " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.quit();
    }

    private void initView() {
        holder = getHolder();
        holder.addCallback(this);
        drawPaint = new Paint();
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(getResources().getDimension(R.dimen.small_space));
        drawPaint.setColor(ContextCompat.getColor(getContext(), R.color.notice_color));
        drawPath = new Path();
    }

    private void draw() {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas();
            drawOnCanvas(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawOnCanvas(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        canvas.drawPath(drawPath, drawPaint);
    }

    private void initMatrix() {
        // 计算图片旋转的度数
//        int degree = 0;
//        try {
//            ExifInterface exifInterface = new ExifInterface(bitmapPath);
//            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    degree = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    degree = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    degree = 270;
//                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 获取图片的参数, 设置压缩属性
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapPath, opt);
        final int xSpace, ySpace;
        float bitmapWidth, bitmapHeight;
//        if (degree == 90 || degree == 270) {
            bitmapWidth = opt.outHeight * 1.0f;
            bitmapHeight = opt.outWidth * 1.0f;
//        } else {
//            bitmapWidth = opt.outWidth * 1.0f;
//            bitmapHeight = opt.outHeight * 1.0f;
//        }
        float width = getWidth() * 1.0f;
        float height = getHeight() * 1.0f;
        float bitmapScale = bitmapWidth / bitmapHeight;
        float viewScale = width / height;
        final int bWidth, bHeight;

        float scale;
        if (bitmapScale > viewScale) {
            scale = bitmapWidth / width;
            xSpace = 0;
            ySpace = (int) ((height - bitmapHeight / scale) / 2);
            bWidth = (int) width;
            bHeight = (int) (bitmapHeight / scale);
        } else {
            scale = bitmapHeight / height;
            xSpace = (int) ((width - bitmapWidth / scale) / 2);
            ySpace = 0;
            bHeight = (int) height;
            bWidth = (int) (bitmapWidth / scale);
        }
        if (bitmapWidth > width || bitmapHeight > height) {
            opt.inSampleSize = (int) scale;
        }
        // 获取图片大小, 设置压缩程度
        File f = new File(bitmapPath);
        long size = f.length();
        if (size > 1048576 && size < 1572564) {
            opt.inDither = false;
            opt.inPreferredConfig = null;
        } else if (size >= 1572564) {
            opt.inDither = false;
            opt.inPreferredConfig = null;
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        opt.inJustDecodeBounds = false;
        // 创建图片对象
        try {
            Bitmap b = BitmapFactory.decodeFile(bitmapPath, opt);
            int bw, bh;
//            if (degree == 90 || degree == 270) {
                bw = bHeight;
                bh = bWidth;
//            } else {
//                bw = bWidth;
//                bh = bHeight;
//            }
            bitmap = Bitmap.createScaledBitmap(b, bw, bh, true);
//            if (degree > 0) {
                Matrix degreeMatrix = new Matrix();
//                degreeMatrix.postRotate(degree);
                degreeMatrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bw, bh, degreeMatrix, true);
//            }
            drawText();
            post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
                    if (lp != null) {
                        lp.width = bWidth;
                        lp.height = bHeight;// - getResources().getDimensionPixelSize(R.dimen.button_bottom_height);
                        lp.leftMargin = xSpace;
                        lp.topMargin = ySpace;
                        setLayoutParams(lp);
                        draw();
                    }
                }
            });
        } catch (OutOfMemoryError e) {
            System.out.println("compress out of memory");
        }
    }

    private void drawText() {
        if (bitmap == null) {
            return;
        }
        int padding = getResources().getDimensionPixelSize(R.dimen.auto_space);
        int bottomPadding = getResources().getDimensionPixelSize(R.dimen.button_bottom_height);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(getResources().getDimension(R.dimen.text_size_small));
        paint.setColor(Color.WHITE);
        Paint.FontMetrics metrice = paint.getFontMetrics();
        float nameLength = paint.measureText(name);
        float timeLength = paint.measureText(time);
        float maxLength = Math.max(nameLength, timeLength);
        float nameX = (maxLength - nameLength) / 2;
        float timeX = (maxLength - timeLength) / 2;

        Paint bgPaint = new Paint();
        bgPaint.setColor(ContextCompat.getColor(getContext(), R.color.shadow_white_color));
        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(1);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float halfPadding = 0.5f * padding;
        float oneHalfPadding = 1.5f * padding;
        float bottom = height - bottomPadding;
        float top = bottom - 2 * metrice.descent + 2 * metrice.ascent;
        float left = width - maxLength;
        float right = width;
        canvas.drawRect(left - oneHalfPadding, top - padding, right - halfPadding, bottom, bgPaint);
        canvas.drawRect(left - oneHalfPadding, top - padding, right - halfPadding, bottom, strokePaint);
        canvas.drawText(name, left + nameX - padding, bottom - 2 * metrice.descent + metrice.ascent - halfPadding, paint);
        canvas.drawText(time, left + timeX - padding, bottom - metrice.descent - halfPadding, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();    //获取手指移动的x坐标
        int y = (int) event.getY();    //获取手指移动的y坐标

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                drawPath.lineTo(x, y);
                break;
        }
        handler.sendEmptyMessage(0);
        return true;
    }

    public void setBitmap(String path, String name, String time) {
        Log.e("TAG", "setBitmap");
        bitmapPath = path;
        this.name = name;
        this.time = time;
        if (handler != null) {
            handler.sendEmptyMessage(1);
        }
    }

    // 重置画布
    public void reset() {
        drawPath.reset();
        handler.sendEmptyMessage(0);
    }

    public Bitmap save(File saveFile) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawOnCanvas(canvas);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}

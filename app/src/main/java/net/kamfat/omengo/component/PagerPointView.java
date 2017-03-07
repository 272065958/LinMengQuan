package net.kamfat.omengo.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cjx on 2016/11/30.
 */
public class PagerPointView extends View {
    Paint paint;
    Bitmap normal, select, bitmap; //导航图标
    int pointCount = 0;
    int xOffUnit;

    public PagerPointView(Context context) {
        super(context);
    }

    public PagerPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPoint(int pointSize, int pointSpace, int selRes, int norRes, int count) {
        if(paint == null){
            paint = new Paint();
            xOffUnit = pointSize + pointSize;
            normal = BitmapFactory.decodeResource(getResources(),
                    norRes);
            normal = Bitmap.createScaledBitmap(normal, pointSize, pointSize, true);
        }
        if(select == null && selRes > 0){
            select = BitmapFactory.decodeResource(getResources(),
                    selRes);
            select = Bitmap.createScaledBitmap(select, pointSize, pointSize, true);
        }
        if(pointCount != count){
            int width = count * xOffUnit - pointSpace;
            pointCount = count;
            if(getLayoutParams() != null){
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = pointSize;
                lp.width = width;
            }

            if(bitmap != null){
                bitmap.recycle();
            }

            bitmap = Bitmap.createBitmap(width, pointSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            for (int i = 0; i < count; i++) {
                canvas.drawBitmap(normal, xOffUnit * i, 0, paint);
            }
        }
    }

    int selectXOff;

    public void setPosition(int position) {
        selectXOff = position * xOffUnit;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap != null){
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
        if(select != null){
            canvas.drawBitmap(select, selectXOff, 0, paint);
        }
    }
}

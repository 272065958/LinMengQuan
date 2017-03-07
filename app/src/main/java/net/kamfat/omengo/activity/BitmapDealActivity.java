package net.kamfat.omengo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.component.BitmapDealView;
import net.kamfat.omengo.util.Tools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cjx on 2016/8/30.
 */
public class BitmapDealActivity extends BaseActivity {
    BitmapDealView bitmapDealView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_deal);
        setToolBar(true, null, -1);

        bitmapDealView = (BitmapDealView) findViewById(R.id.surface_view);
        String name = "王小帅";
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        bitmapDealView.setBitmap(getIntent().getStringExtra("photo"), name, time);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.action_back:
                finish();
                break;
            case R.id.action_return:
                bitmapDealView.reset();
                break;
            case R.id.deal_finish_button:
                String path = Tools.getTempPath(this) + System.currentTimeMillis() + "deal_tempbitmap.jpeg";
                File f = new File(path);
                try {
                    f.createNewFile();
                    bitmapDealView.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, new Intent(path));
                finish();
                break;
        }
    }
}

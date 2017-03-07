package net.kamfat.omengo.activity;

import android.os.Bundle;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2017/2/28.
 */
public class LauncherActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        timeStart();
    }

    @Override
    public void onBackPressed() {

    }

    private void timeStart() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }.start();
    }
}

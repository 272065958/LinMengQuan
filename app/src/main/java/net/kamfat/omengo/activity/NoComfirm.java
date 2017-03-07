package net.kamfat.omengo.activity;

import android.os.Bundle;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/7.
 */
public class NoComfirm extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_comfirm);

        setToolBar(true, null, -1);
        setToolbarTitle("功能未定");
    }
}

package net.kamfat.omengo.property.activity;

import android.os.Bundle;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/12.
 */
public class VisitorsCodeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_code);

        setToolBar(true, null, R.string.visitors_code);

    }

}

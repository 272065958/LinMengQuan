package net.kamfat.omengo.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/9.
 */
public class PropertyNoticationDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_notication_detail);

        setToolBar(true, null, R.string.property_notication);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String detail = intent.getStringExtra("detail");
        String time = intent.getStringExtra("time");

        TextView titleView = (TextView) findViewById(R.id.notication_title);
        TextView detailView = (TextView) findViewById(R.id.notication_detail);
        TextView timeView = (TextView) findViewById(R.id.notication_time);
        titleView.setText(title);
        detailView.setText(detail);
        timeView.setText(time);
    }
}

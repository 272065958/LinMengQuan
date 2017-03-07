package net.kamfat.omengo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/12.
 */
public class FeedbackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setToolBar(true, null, R.string.myself_feedback);

        TextView bottomBtn = (TextView) findViewById(R.id.bottom_button);
        bottomBtn.setText(R.string.button_sure);
    }

    public void onClick(View view){
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}

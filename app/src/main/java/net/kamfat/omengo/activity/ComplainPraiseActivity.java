package net.kamfat.omengo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/9.
 */
public class ComplainPraiseActivity extends BaseActivity {

    TextView praiseView, complainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_praise);

        setToolBar(true, null, R.string.property_complain_praise);
        TextView bottomBtn = (TextView) findViewById(R.id.bottom_button);
        bottomBtn.setText(R.string.button_sure);

        praiseView = (TextView) findViewById(R.id.praise_view);
        complainView = (TextView) findViewById(R.id.complain_view);
    }

    public void onClick(View view){
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void praise(View view){
        praiseView.setBackgroundResource(R.drawable.main_frame_bg);
        complainView.setBackgroundResource(R.drawable.black_frame_bg);
    }

    public void complain(View view){
        complainView.setBackgroundResource(R.drawable.main_frame_bg);
        praiseView.setBackgroundResource(R.drawable.black_frame_bg);
    }
}

package net.kamfat.omengo.balance.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/8.
 */
public class BalancePasswordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_password);

        setToolBar(true, null, R.string.balance_password);

        TextView change = (TextView) findViewById(R.id.balance_password_change);
        change.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    public void passwordSetting(View v){ // 支付密码设置
        v.setSelected(!v.isSelected());
    }

    public void changePassword(View v){ // 支付密码设置
        Toast.makeText(this, getString(R.string.only_demo), Toast.LENGTH_SHORT).show();
    }
}

package net.kamfat.omengo.balance.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/8.
 */
public class BalanceRechargeActivity extends BaseActivity {

    View alipyContent, weixinContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_recharge);

        setToolBar(true, null, R.string.balance_recharge);

        alipyContent = findViewById(R.id.pay_alipy_content);
        weixinContent = findViewById(R.id.pay_weixin_content);

        TextView buttonView = (TextView) findViewById(R.id.bottom_button);
        buttonView.setText("确认充值");
    }

    public void payAlipay(View v){
        alipyContent.setSelected(true);
        weixinContent.setSelected(false);
    }

    public void payWeixin(View v){
        alipyContent.setSelected(false);
        weixinContent.setSelected(true);
    }

    public void onClick(View view){
        Toast.makeText(this, getString(R.string.only_demo), Toast.LENGTH_SHORT).show();
    }
}

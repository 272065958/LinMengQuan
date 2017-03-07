package net.kamfat.omengo.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.balance.activity.BalanceDeductionActivity;
import net.kamfat.omengo.balance.activity.BalancePasswordActivity;
import net.kamfat.omengo.balance.activity.BalanceRechargeActivity;
import net.kamfat.omengo.balance.activity.BalanceRecordActivity;

/**
 * Created by cjx on 2016/9/8.
 */
public class PropertyBalanceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_property_balance);
        setContentView(R.layout.activity_my_property);

        setToolBar(true, null, R.string.myself_balance_title);
    }

    public void balanceClick(View v){
        switch (v.getId()){
//            case R.id.balance_deduction: // 自动扣费设置
//                Intent deductionIntent = new Intent(this, BalanceDeductionActivity.class);
//                startActivity(deductionIntent);
//                break;
            case R.id.balance_record: // 账单记录
                Intent recordIntent = new Intent(this, BalanceRecordActivity.class);
                startActivity(recordIntent);
                break;
//            case R.id.balance_password: // 支付密码设置
//                Intent passwordIntent = new Intent(this, BalancePasswordActivity.class);
//                startActivity(passwordIntent);
//                break;
            case R.id.balance_recharge: // 充值
                Intent rechargeIntent = new Intent(this, BalanceRechargeActivity.class);
                startActivity(rechargeIntent);
                break;
        }
    }
}

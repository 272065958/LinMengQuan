package net.kamfat.omengo.balance.activity;

import android.os.Bundle;
import android.view.View;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/8.
 */
public class BalanceDeductionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_deduction);

        setToolBar(true, null, R.string.balance_deduction);
    }

    public void deduction(View v){
        v.setSelected(!v.isSelected());
    }
}

package net.kamfat.omengo.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.fragment.ShopCartFragment;

/**
 * Created by cjx on 2016/9/18.
 */
public class ShopCartActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        FragmentManager fm = getSupportFragmentManager();
        ShopCartFragment scf = (ShopCartFragment) fm.findFragmentById(R.id.shop_cart_fragment);

        registerReceiver(new IntentFilter(OmengoApplication.ACTION_CART_BUY));
    }

    // 收到广播回调
    @Override
    protected void onBroadcastReceive(Intent intent){
        finish();
    }
}

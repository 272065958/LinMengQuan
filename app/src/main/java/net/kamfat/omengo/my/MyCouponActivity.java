package net.kamfat.omengo.my;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseTabActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.CouponBean;
import net.kamfat.omengo.http.HttpUtils;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/12/27.
 * 我的优惠券
 */
public class MyCouponActivity extends BaseTabActivity {

    String[] value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBar(true, null, R.string.myself_coupon);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected String[] initTitle() {
        value = new String[]{"0", "1"};
        return new String[]{
                getString(R.string.coupon_no_use),
                getString(R.string.coupon_use), getString(R.string.coupon_outtime)};
    }

    @Override
    protected void onLoadResult(int position, ArrayList<?> list){
        if(list != null){
            for(Object obj : list){
                ((CouponBean)obj).initPrice();
            }
        }
        super.onLoadResult(position, list);
    }

    // 加载数据
    @Override
    protected void loadData(int position) {
        if(position == 2){ // 已过期
            HttpUtils.getInstance().postEnqueue(this, getMyCallbackInterface(position, new TypeToken<ArrayList<CouponBean>>() {
            }.getType()), "coupon/index", "useDate", "overdate");
        }else{
            HttpUtils.getInstance().postEnqueue(this, getMyCallbackInterface(position, new TypeToken<ArrayList<CouponBean>>() {
            }.getType()), "coupon/index", "searchProperty", "is_used", "searchValue", value[position]);
        }
    }



    @Override
    protected MyBaseAdapter getMyBaseAdapter(int position, ArrayList<?> list) {
        return new CouponAdapter(list, this, position);
    }

}

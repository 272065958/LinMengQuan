package net.kamfat.omengo.util;

import android.content.Intent;

import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.my.MyOrderActivity;
import net.kamfat.omengo.server.PayActivity;

/**
 * Created by cjx on 17-3-8.
 * 工单操作工具
 */

public class OrderOperateUtil {
    private static OrderOperateUtil util = new OrderOperateUtil();

    public static OrderOperateUtil getUtil(){
        return util;
    }

    private OrderOperateUtil() {
    }

    public void cancelOrder(final CancelInterface cancelInterface, BaseActivity activity, String sn){
        cancelInterface.beforeCancel();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                cancelInterface.cancelSuccess(response.message);
            }

            @Override
            public void error() {
                cancelInterface.cancelFail();
            }
        };
        HttpUtils.getInstance().postEnqueue(activity, callbackInterface, "order/cancel", "sn", sn);
    }

    public void goToPay(BaseActivity activity, String id, String price){
        Intent payIntent = new Intent(activity, PayActivity.class);
        payIntent.putExtra("id", id);
        payIntent.putExtra("price", price);
        // String sn = jsonObject.getString("orderSn");
        payIntent.putExtra("title", "订单支付");
        activity.startActivity(payIntent);
    }

    public interface CancelInterface{
        void beforeCancel();
        void cancelSuccess(String message);
        void cancelFail();
    }
}

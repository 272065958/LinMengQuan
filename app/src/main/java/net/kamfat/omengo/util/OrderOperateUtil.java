package net.kamfat.omengo.util;

import android.content.Intent;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.dialog.TipDialog;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.server.PayActivity;

/**
 * Created by cjx on 17-3-8.
 * 工单操作工具
 */

public class OrderOperateUtil {
    private TipDialog cancelDialog;
    private DialogClickListener listener;

    public OrderOperateUtil() {
    }

    private void cancelOrder(final CancelInterface cancelInterface, BaseActivity activity, String sn) {
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

    public void goToPay(BaseActivity activity, String id, String price) {
        Intent payIntent = new Intent(activity, PayActivity.class);
        payIntent.putExtra("id", id);
        payIntent.putExtra("price", price);
        // String sn = jsonObject.getString("orderSn");
        payIntent.putExtra("title", "订单支付");
        activity.startActivity(payIntent);
    }

    // 提示取消订单的对话框
    public void showCancelTipDialog(CancelInterface cancelInterface, BaseActivity activity, String sn) {
        if (cancelDialog == null) {
            cancelDialog = new TipDialog(activity);
            cancelDialog.setText(R.string.tip_title, R.string.cancel_order_tip, R.string.button_sure,
                    R.string.button_cancel);
            listener = new DialogClickListener(cancelInterface, activity, sn);
            cancelDialog.setTipComfirmListener(listener);
        } else {
            listener.setParams(cancelInterface, activity, sn);
        }
        cancelDialog.show();
    }

    class DialogClickListener implements TipDialog.ComfirmListener {
        CancelInterface cancelInterface;
        BaseActivity activity;
        String sn;

        public DialogClickListener(CancelInterface cancelInterface, BaseActivity activity, String sn) {
            setParams(cancelInterface, activity, sn);
        }

        public void setParams(CancelInterface cancelInterface, BaseActivity activity, String sn) {
            this.sn = sn;
            this.cancelInterface = cancelInterface;
            this.activity = activity;
        }

        @Override
        public void comfirm() {
            cancelOrder(cancelInterface, activity, sn);
        }

        @Override
        public void cancel() {
            cancelDialog.dismiss();
        }
    }

    public interface CancelInterface {
        void beforeCancel();

        void cancelSuccess(String message);

        void cancelFail();
    }
}

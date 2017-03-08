package net.kamfat.omengo.my;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.BaseClassAdapter;
import net.kamfat.omengo.base.BaseListActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.OrderDetailBean;
import net.kamfat.omengo.bean.OrderItemBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.OrderOperateUtil;
import net.kamfat.omengo.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/14.
 */
public class OrderDetailActivity extends BaseListActivity implements View.OnClickListener {
    String orderId, orderAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        setToolBar(true, null, R.string.shop_pay_detail);
        initView(null);
        loadData();
    }

    @Override
    protected MyBaseAdapter getMyBaseAdapter(ArrayList<?> list) {
        return new OrderDetailAdapter(list, this);
    }

    @Override
    public void onClick(View v) {
        OrderOperateUtil util = OrderOperateUtil.getUtil();
        switch ((int) v.getTag()) {
            case R.string.button_order_back: // 申请退款
                break;
            case R.string.button_order_cancel: // 取消订单
                util.cancelOrder(new OrderOperateUtil.CancelInterface() {
                    @Override
                    public void beforeCancel() {
                        showLoadDislog();
                    }

                    @Override
                    public void cancelSuccess(String message) {
                        dismissLoadDialog();
                        showToast(message);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void cancelFail() {
                        dismissLoadDialog();
                    }
                }, OrderDetailActivity.this, getIntent().getAction());
                break;
            case R.string.button_order_pay: // 订单支付
                break;
        }
    }

    @Override
    protected void loadData() {
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                try {
                    JSONArray array = new JSONArray(response.datum);
                    JSONObject object = array.getJSONObject(0);
                    ArrayList<OrderDetailBean> list = JsonParser.getInstance().fromJson(array.getString(1),
                            new TypeToken<ArrayList<OrderDetailBean>>() {
                            }.getType());
                    listView.addHeaderView(getHeaderView(object));
                    listView.addFooterView(getFooterView(object));
                    onLoadResult(list);

                    // 显示底部按钮
                    int status = getIntent().getIntExtra("status", -1);
                    if (status > 0) {
                        findViewById(R.id.bottom_button_content).setVisibility(View.VISIBLE);
                        TextView button1 = (TextView) findViewById(R.id.order_button_1);
                        button1.setOnClickListener(OrderDetailActivity.this);
                        switch (status) {
                            case 1:// 待付款
                                button1.setText(R.string.button_order_pay);
                                button1.setTag(R.string.button_order_pay);
                                TextView button2 = (TextView) findViewById(R.id.order_button_2);
                                button2.setVisibility(View.VISIBLE);
                                button2.setOnClickListener(OrderDetailActivity.this);
                                button2.setText(R.string.button_order_cancel);
                                button2.setTag(R.string.button_order_cancel);
                                break;
                            case 2:// 待服务
                                button1.setText(R.string.button_order_back);
                                button1.setTag(R.string.button_order_back);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "order/order_detail_user", "sn", getIntent().getAction());
    }

    // 获取headerview
    private View getHeaderView(JSONObject json) throws JSONException {
        View view = View.inflate(this, R.layout.header_order_detail, null);
        TextView numView = (TextView) view.findViewById(R.id.order_detail_num);
        TextView addressView = (TextView) view.findViewById(R.id.order_detail_address);
        TextView typeView = (TextView) view.findViewById(R.id.order_detail_type);
        TextView timeView = (TextView) view.findViewById(R.id.order_detail_time);
        TextView payView = (TextView) view.findViewById(R.id.order_detail_pay);
        TextView phoneView = (TextView) view.findViewById(R.id.order_detail_phone);
        if (json.has("address")) {
            addressView.setText(json.getString("address"));
        }
        if (json.has("sn")) {
            numView.setText(json.getString("sn"));
        }
        if (json.has("shipping_method_name")) {
            typeView.setText(json.getString("shipping_method_name"));
        }
        if (json.has("payment_method_name")) {
            payView.setText(json.getString("payment_method_name"));
        }
        if (json.has("service_date")) {
            timeView.setText(json.getString("service_date"));
        }
        if (json.has("phone")) {
            phoneView.setText(json.getString("phone"));
        }
        if (json.has("id")) {
            orderId = json.getString("id");
        }
        if (json.has("amount")) {
            orderAmount = json.getString("amount");
        }
        return view;
    }

    // 获取headerview
    private View getFooterView(JSONObject json) throws JSONException {
        View view = View.inflate(this, R.layout.footer_order_detail, null);
        TextView priceView = (TextView) view.findViewById(R.id.order_price);
        TextView timeView = (TextView) view.findViewById(R.id.order_time);
        if (json.has("amount")) {
            priceView.setText(String.format(getString(R.string.price_format), json.getString("amount")));
        }
        if (json.has("creation_date")) {
            timeView.setText(json.getString("creation_date"));
        }
        return view;
    }

    class OrderDetailAdapter extends BaseClassAdapter {
        public OrderDetailAdapter(ArrayList<?> list, BaseActivity context) {
            super(list, context, 1);
        }

        @Override
        protected ParentViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, ParentViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            OrderDetailBean odb = (OrderDetailBean) getItem(position);
            ho.titleView.setText(odb.name);
        }

        @Override
        protected ArrayList<?> getItemList(int position) {
            return ((OrderDetailBean) list.get(position)).list;
        }

        @Override
        protected View createItemView(Context context) {
            return View.inflate(context, R.layout.item_order_detail_item, null);
        }

        @Override
        protected void bindItemData(Object obj, ItemViewHolder holder) {
            OrderItemBean oib = (OrderItemBean) obj;
            OrderViewHolder ho = (OrderViewHolder) holder;
            Tools.setImage(context, ho.imageView, oib.image);
            ho.countView.setText(String.format(getString(R.string.count_format_string), oib.quantity));
            ho.nameView.setText(oib.full_name);
            ho.priceView.setText(String.format(getString(R.string.price_format), oib.pricestr));
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        protected ItemViewHolder bindItemViewHolder(View v) {
            return new OrderViewHolder(v);
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_order_detail, null);
        }

        class ViewHolder extends ParentViewHolder {
            TextView titleView;

            public ViewHolder(View view) {
                super(view);
                titleView = (TextView) view.findViewById(R.id.order_class_title);
            }

            @Override
            protected LinearLayout getContentView(View view) {
                return (LinearLayout) view.findViewById(R.id.order_item_content);
            }
        }

        class OrderViewHolder extends ItemViewHolder {
            ImageView imageView;
            TextView nameView, priceView, countView;

            public OrderViewHolder(View v) {
                super(v);
                imageView = (ImageView) v.findViewById(R.id.shop_detail_photo);
                nameView = (TextView) v.findViewById(R.id.shop_detail_title);
                priceView = (TextView) v.findViewById(R.id.shop_detail_price);
                countView = (TextView) v.findViewById(R.id.shop_detail_count);
            }
        }
    }
}

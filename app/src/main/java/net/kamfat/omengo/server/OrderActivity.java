package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.AddressBean;
import net.kamfat.omengo.bean.BaseProductBean;
import net.kamfat.omengo.bean.CouponBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.component.wheel.WheelLayout;
import net.kamfat.omengo.dialog.DateSelectDialog;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by cjx on 2016/12/23.
 * 商品提交订单页面
 */

public class OrderActivity extends BaseActivity {
    final int RESULT_ADDRESS = 1, RESULT_COUPON = 2;
    AddressBean address;
    ArrayList<CouponBean> couponList;
    ArrayList<BaseProductBean> products;
    CouponBean coupon;
    TextView couponView;
    TextView nameView, addressView, phoneView;
    TextView priceView, countView, timeView;
    EditText remarkVew;

    boolean isFromCart = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setToolBar(true, null, R.string.shop_pay);

        Intent intent = getIntent();
        products = (ArrayList<BaseProductBean>) intent.getSerializableExtra("product");
        isFromCart = intent.getBooleanExtra("fromCart", false);
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_ADDRESS:
                    address = (AddressBean) data.getSerializableExtra("address");
                    initAddressView();
                    break;
                case RESULT_COUPON:
                    CouponBean cb = (CouponBean) data.getSerializableExtra("coupon");
                    coupon = cb;
                    if (cb == null) {
                        couponView.setText(null);
                        couponView.setTag(null);
                    } else {
                        couponView.setText(cb.name);
                        couponView.setTag(cb.id);
                    }
                    initPrice();
                    break;
            }
        }
    }

    private void loadData(){
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                findViewById(R.id.loading_view).setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response.datum);
                    JsonParser parser = JsonParser.getInstance();
                    if (obj.has("receiver")) {
                        address = parser.fromJson(obj.getString("receiver"), AddressBean.class);
                    }
                    if (obj.has("coupon")) {
                        couponList = parser.fromJson(obj.getString("coupon"), new TypeToken<ArrayList<CouponBean>>() {
                        }.getType());
                        if (couponList != null) {
                            for (CouponBean cb : couponList) {
                                cb.initPrice();
                            }
                        }
                    }
                    findViewById();
                    initPrice();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                findViewById(R.id.loading_view).setVisibility(View.GONE);
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "app/memberResource");
    }

    private void findViewById() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_button).setVisibility(View.VISIBLE);
        listView.addHeaderView(getHeaderView());
        listView.setAdapter(new ShopAdapter(products, this));
        priceView = (TextView) findViewById(R.id.order_price);
        countView = (TextView) findViewById(R.id.order_count);
    }

    // 显示收货地址
    private void initAddressView() {
        if (address != null) {
            if (phoneView.getVisibility() == View.GONE) {
                phoneView.setVisibility(View.VISIBLE);
                addressView.setVisibility(View.VISIBLE);
            }
            nameView.setText(address.consignee);
            addressView.setText(address.address);
            phoneView.setText(address.phone);
        }
    }

    private View getHeaderView() {
        View view = View.inflate(this, R.layout.order_header, null);
        nameView = (TextView) view.findViewById(R.id.property_name);
        phoneView = (TextView) view.findViewById(R.id.property_phone);
        addressView = (TextView) view.findViewById(R.id.property_location);
        couponView = (TextView) view.findViewById(R.id.coupon_view);
        timeView = (TextView) view.findViewById(R.id.order_time);
        remarkVew = (EditText) view.findViewById(R.id.order_remark);
        if (couponList == null || couponList.isEmpty()) {
            couponView.setHint(R.string.order_no_coupon);
            view.findViewById(R.id.order_coupon_select).setClickable(false);
        }
        initAddressView();
        return view;
    }

    // 立即购买
    public void buyClick(View v) {
        if (address == null) {
            showToast(getString(R.string.receive_address_null_hint));
            return;
        }
        String time = timeView.getText().toString();
        String remark = remarkVew.getText().toString();
        String cid = (String) couponView.getTag();
        StringBuilder productIds = new StringBuilder();
        StringBuilder quantitys = new StringBuilder();
        for (BaseProductBean pb : products) {
            if (productIds.length() > 0) {
                productIds.append(",");
                quantitys.append(",");
            }
            productIds.append(pb.id);
            quantitys.append(pb.quantity);
        }
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                showToast(response.message);
                try {
                    JSONObject jsonObject = new JSONObject(response.datum);
                    Intent payIntent = new Intent(OrderActivity.this, PayActivity.class);
                    payIntent.putExtra("id", jsonObject.getString("orderId"));
                    payIntent.putExtra("price", jsonObject.getString("amount"));
                    // String sn = jsonObject.getString("orderSn");
                    payIntent.putExtra("title", getIntent().getAction());
                    startActivity(payIntent);
                    if(isFromCart){
                        sendBroadcast(new Intent(OmengoApplication.ACTION_CART_BUY));
                    }else{
                        sendBroadcast(new Intent(OmengoApplication.ACTION_ORDER_CREATE));
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        if (isFromCart) {
            HttpUtils.getInstance().postEnqueue(this, callbackInterface, "order/createByNow", "cartItemIds", productIds.toString(),
                    "receiverId", address.id, "couponcodeId", cid, "dateTime", time, "remarks", remark);
        } else {
            HttpUtils.getInstance().postEnqueue(this, callbackInterface, "order/createByNow", "productIds", productIds.toString(),
                    "quantitys", quantitys.toString(), "receiverId", address.id, "couponcodeId", cid, "dateTime", time, "remarks", remark);
        }
    }

    // 选择收货地址
    public void propertySelect(View v) {
        Intent intent = new Intent(this, AddressSelectActivity.class);
        startActivityForResult(intent, RESULT_ADDRESS);
    }

    // 选择时间
    public void timeSelect(View v) {
        showDateDialog(timeView);
    }

    // 选择优惠券
    public void couponSelect(View v) {
        Intent intent = new Intent(this, CouponSelectActivity.class);
        intent.putExtra("coupon", couponList);
        startActivityForResult(intent, RESULT_COUPON);
    }

    DateSelectDialog dateDialog;

    private void showDateDialog(TextView v) {
        if (dateDialog == null) {
            dateDialog = new DateSelectDialog(this, WheelLayout.TimeStyle.FUTURE);
        }
        dateDialog.bind(v);
        dateDialog.show();
    }

    // 显示价格
    DecimalFormat decimalFormat = new DecimalFormat("0.0");

    private void initPrice() {
        if (products != null) {
            BigDecimal priceDecimal = new BigDecimal("0");
            int count = 0;
            for (BaseProductBean pb : products) {
                count = count + pb.quantity;
                BigDecimal unitPrice = new BigDecimal(pb.price);
                BigDecimal buyCount = new BigDecimal(pb.quantity);
                priceDecimal = priceDecimal.add(buyCount.multiply(unitPrice));
            }
            if (coupon != null) {
                priceDecimal = priceDecimal.subtract(new BigDecimal(coupon.price));
            }
            float price = priceDecimal.floatValue();
            if (price < 0) {
                price = 0;
            }
            priceView.setText(Html.fromHtml(String.format(getString(R.string.order_pay_price_format),
                    "<font color='red'>" + "￥" + decimalFormat.format(price) + "</font>")));
            countView.setText(String.format(getString(R.string.order_count_format), count));
        }
    }

    class ShopAdapter extends MyBaseAdapter {
        public ShopAdapter(ArrayList<?> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_order, null);
        }

        @Override
        protected MyViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, MyViewHolder holder) {
            BaseProductBean pb = (BaseProductBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            Tools.setImage(context, ho.iconView, pb.image);
            ho.titleView.setText(pb.name);
            ho.priceView.setText("￥" + pb.price);
            ho.countView.setText("×" + pb.quantity);
        }

        class ViewHolder extends MyViewHolder {
            ImageView iconView;
            TextView titleView, priceView, countView;

            public ViewHolder(View view) {
                super(view);
                iconView = (ImageView) view.findViewById(R.id.shop_detail_photo);
                titleView = (TextView) view.findViewById(R.id.shop_detail_title);
                priceView = (TextView) view.findViewById(R.id.shop_detail_price);
                countView = (TextView) view.findViewById(R.id.shop_detail_count);
            }
        }
    }
}

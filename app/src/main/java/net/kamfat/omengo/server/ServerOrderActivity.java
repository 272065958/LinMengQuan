package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.AddressBean;
import net.kamfat.omengo.bean.CouponBean;
import net.kamfat.omengo.bean.NormBean;
import net.kamfat.omengo.bean.ServerProductBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.component.wheel.WheelLayout;
import net.kamfat.omengo.dialog.DateSelectDialog;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by cjx on 2016/12/22.
 * 订单
 */

public class ServerOrderActivity extends BaseActivity implements View.OnClickListener {

    final int RESULT_ADDRESS = 1, RESULT_COUPON = 2;

    TextView nameView, addressView, phoneView, countView;
    EditText remarkVew;
    ServerProductBean product;
    AddressBean address;
    ArrayList<CouponBean> couponList;
    TextView timeView, areaView, priceView, couponView;
    PackageAdapter adapter;
    String count = "1";  // 服务数量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_server_order);
        setToolBar(true, null, intent.getAction());
        loadData(intent.getStringExtra("key"));
        registerReceiver(new IntentFilter(OmengoApplication.ACTION_ORDER_CREATE));
    }

    // 收到广播回调
    @Override
    protected void onBroadcastReceive(Intent intent){
        finish();
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
                    if(cb == null){
                        couponView.setText(null);
                        couponView.setTag(null);
                    }else{
                        couponView.setText(cb.name);
                        couponView.setTag(cb.id);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int c = Integer.parseInt(count);
        switch (v.getId()) {
            case R.id.count_add: // 添加数量
                c++;
                count = String.valueOf(c);
                countView.setText(count);
                initPrice();
                break;
            case R.id.count_minus: // 减少数据
                if(c > 1){
                    c --;
                    count = String.valueOf(c);
                    countView.setText(count);
                    initPrice();
                }
                break;
        }
    }

    private void loadData(String key) {
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
                    if(obj.has("coupon")){
                        couponList = parser.fromJson(obj.getString("coupon"), new TypeToken<ArrayList<CouponBean>>(){}.getType());
                        if(couponList != null){
                            for(CouponBean cb : couponList){
                                cb.initPrice();
                            }
                        }
                    }
                    if (obj.has("product")) {
                        product = parser.fromJson(obj.getString("product"),
                                new TypeToken<ServerProductBean>() {
                                }.getType());
                        findViewById(R.id.bottom_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.order_detail_content).setVisibility(View.VISIBLE);
                    }
                    if(product != null){
                        findViewById(getIntent().getStringExtra("type"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                findViewById(R.id.loading_view).setVisibility(View.GONE);
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "app/fourPage", "key", key);
    }

    private void findViewById(String type) {
        nameView = (TextView) findViewById(R.id.property_name);
        phoneView = (TextView) findViewById(R.id.property_phone);
        addressView = (TextView) findViewById(R.id.property_location);
        remarkVew = (EditText) findViewById(R.id.order_remark);
        switch (type) {
            case "11": //清洁
                showPriceView();
                showAreaView();
                break;
            case "12": //维修服务
                break;
            case "13": //家电清洁
                showPriceView();
                showCountView();
                break;
        }
        initAddressView();
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
            initArea();
        }
    }

    // 显示选择数量
    private void showCountView() {
        ViewStub countStub = (ViewStub) findViewById(R.id.order_count_viewstub);
        View v = countStub.inflate();
        countView = (TextView) v.findViewById(R.id.count_view);
        v.findViewById(R.id.count_add).setOnClickListener(this);
        v.findViewById(R.id.count_minus).setOnClickListener(this);
    }

    // 显示价格和优惠券
    private void showPriceView() {
        ViewStub priceStub = (ViewStub) findViewById(R.id.order_price_viewstub);
        View v = priceStub.inflate();
        priceView = (TextView) v.findViewById(R.id.order_price);

        ViewStub couponStub = (ViewStub) findViewById(R.id.order_coupon_viewstub);
        v = couponStub.inflate();
        couponView = (TextView) v.findViewById(R.id.coupon_view);
        if(couponList == null || couponList.isEmpty()){
            couponView.setHint(R.string.order_no_coupon);
            v.findViewById(R.id.order_coupon_select).setClickable(false);
        }
    }

    // 显示面积控件
    private void showAreaView() {
        ViewStub areaStub = (ViewStub) findViewById(R.id.order_area_viewstub);
        View v = areaStub.inflate();
        areaView = (TextView) v.findViewById(R.id.order_area);
        timeView = (TextView) v.findViewById(R.id.order_time);
        if (product.specification_items != null && !product.specification_items.isEmpty()) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new PackageAdapter(product.specification_items, this);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(adapter);
        }
    }

    // 显示住房面积
    private void initArea() {
        if (areaView != null) {
            count = address.user_area;
            areaView.setText(Html.fromHtml(String.format(getString(R.string.order_area_format),
                    "<font color='red'>"+count+"</font>")));
        }
        initPrice();
    }

    // 显示服务价格
    DecimalFormat decimalFormat = new DecimalFormat("0");
    private void initPrice() {
        if(priceView != null){
            BigDecimal unitPrice = new BigDecimal(product.price);
            BigDecimal buyCount = new BigDecimal(count);
            String price = decimalFormat.format(buyCount.multiply(unitPrice).intValue());
            priceView.setText(Html.fromHtml(String.format(getString(R.string.order_price_format),
                    "<font color='red'>" + price + "</font>")));
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
    public void couponSelect(View v){
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

    // 提交订单
    public void submitClick(View v) {
        if(address == null){
            showToast(getString(R.string.receive_address_null_hint));
            return ;
        }
        String time = timeView.getText().toString();
        String remark = remarkVew.getText().toString();
        String pid = getIntent().getStringExtra("key");
        String nid = adapter == null ? null : adapter.getNormId();
        String cid = (String) couponView.getTag();
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                showToast(response.message);
                try {
                    JSONObject jsonObject = new JSONObject(response.datum);
                    Intent payIntent = new Intent(ServerOrderActivity.this, PayActivity.class);
                    payIntent.putExtra("id", jsonObject.getString("orderId"));
                    payIntent.putExtra("price", jsonObject.getString("amount"));
                    // String sn = jsonObject.getString("orderSn");
                    payIntent.putExtra("title", getIntent().getAction());
                    startActivity(payIntent);
                    sendBroadcast(new Intent(OmengoApplication.ACTION_ORDER_CREATE));
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
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "order/create", "receiverId", address.id,
                "couponcodeId", cid, "dateTime", time, "remarks", remark, "productId", pid, "parameterId", nid,
                "quantity", countView == null ? null : countView.getText().toString());
    }

    // 规格的设配器
    class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
        ArrayList<NormBean> list;
        Context context;
        int currentPosition = 0;
        int margin;

        public PackageAdapter(ArrayList<NormBean> list, Context context) {
            this.list = list;
            this.context = context;
            margin = getResources().getDimensionPixelOffset(R.dimen.auto_space);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(context, R.layout.item_norm, null);
            RecyclerView.LayoutParams rlp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setOnClickListener(this);
            rlp.leftMargin = margin;
            switch (viewType) {
                case 2:
                    rlp.rightMargin = margin;
                    break;
                default:

                    break;
            }
            v.setLayoutParams(rlp);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder ho = (ViewHolder) holder;
            NormBean nb = list.get(position);
            ho.normView.setText(nb.name);
            ho.itemView.setTag(R.id.order_position_select, position);
            if (position == currentPosition) {
                ho.itemView.setSelected(true);
            } else {
                ho.itemView.setSelected(false);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position % 3; // 默认3列
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onClick(View v) {
            currentPosition = (int) v.getTag(R.id.order_position_select);
            notifyDataSetChanged();
            NormBean nb = list.get(currentPosition);
            product.price = nb.value;
            initPrice();
        }

        public String getNormId(){
            return list.get(currentPosition).parameterid;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView normView;

            public ViewHolder(View v) {
                super(v);
                normView = (TextView) v.findViewById(R.id.name_view);
            }
        }
    }
}

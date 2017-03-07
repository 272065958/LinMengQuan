package net.kamfat.omengo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseFragment;
import net.kamfat.omengo.activity.MainActivity;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.BaseClassAdapter;
import net.kamfat.omengo.bean.AddressBean;
import net.kamfat.omengo.bean.BaseProductBean;
import net.kamfat.omengo.bean.CartBean;
import net.kamfat.omengo.bean.CouponBean;
import net.kamfat.omengo.bean.ShopItemBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.server.OrderActivity;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/21.
 * 购物车页面
 */
public class ShopCartFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    View loadView;
    ListView listView;
    BaseActivity activity;

    TextView priceView;
    View allSelectView;
    String price = "0";
    SwipeRefreshLayout refreshLayout;

    ArrayList<CouponBean> couponList;
    AddressBean address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_shop_cart, null);
            boolean isMainFragment = getActivity() instanceof MainActivity;
            setToolBar(view, !isMainFragment, null, R.string.main_shop);
            activity = (BaseActivity) getActivity();
            loadView = view.findViewById(R.id.loading_view);
            listView = (ListView) view.findViewById(R.id.list_view);
            listView.setDivider(ContextCompat.getDrawable(activity, R.color.division_line_color));
            listView.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.auto_space));

            refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
            refreshLayout.setOnRefreshListener(this);

            allSelectView = view.findViewById(R.id.shop_cart_select_all);
            allSelectView.setOnClickListener(this);
            view.findViewById(R.id.shop_cart_pay).setOnClickListener(this);
            priceView = (TextView) view.findViewById(R.id.shop_cart_price);
            setPrice();
            loadData();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shop_cart_select_all:
                selectAll(v);
                break;
            case R.id.shop_cart_pay:
                pay();
                break;
        }
    }

    @Override
    public void onRefresh() {
        if(view == null){
            return ;
        }
        price = "0";
        setPrice();
        loadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (adapter != null) {
                    String ids = adapter.getSelectIds();
                    if(ids != null){
                        shopDelete(ids);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 隐藏加载控件
    private void hideLoadView() {
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.GONE);
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    // 加载数据完成后调用
    private void onLoadResult(ArrayList<?> list) {
        hideLoadView();
        displayData(list);
    }

    ShopCartAdapter adapter;

    // 显示数据
    private void displayData(ArrayList<?> list) {
        if (adapter == null) {
            adapter = getMyBaseAdapter(list);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged(list);
        }
        if (adapter.getCount() == 0) {
            view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            ImageView iv = (ImageView) view.findViewById(R.id.empty_icon);
            iv.setImageResource(R.drawable.empty_cart);
            iv.setVisibility(View.VISIBLE);
            TextView tip = (TextView) view.findViewById(R.id.empty_title);
            tip.setText("没有商品!");
        } else {
            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
        }
    }

    private ShopCartAdapter getMyBaseAdapter(ArrayList<?> list) {
        return new ShopCartAdapter(list, (BaseActivity) getActivity());
    }

    // 加载数据
    private void loadData() {
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                hideLoadView();
                parserData(response.datum);
            }

            @Override
            public void error() {
                hideLoadView();
            }
        };
        HttpUtils.getInstance().postEnqueue(activity, callbackInterface, "cart/index");
    }

    // 解析后台返回的数据
    private void parserData(String datum) {
        try {
            if(datum == null){
                onLoadResult(null);
                return ;
            }
            JSONObject obj = new JSONObject(datum);
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
            if (obj.has("cartCount")) {
                int count = obj.getInt("cartCount");
            }
            if (obj.has("cartList")) {
                ArrayList<CartBean> list = parser.fromJson(
                        obj.getString("cartList"), new TypeToken<ArrayList<CartBean>>() {
                        }.getType());
                onLoadResult(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 计算当前价格
    private void caculatePrice() {
        ArrayList<CartBean> list = (ArrayList<CartBean>) adapter.list;
        if (list != null) {
            BigDecimal decimal = new BigDecimal("0");
            for (CartBean cb : list) {
                for (ShopItemBean sib : cb.cartItems) {
                    if (sib.isSelect) {
                        decimal = decimal.add(new BigDecimal(sib.price).multiply(new BigDecimal(sib.quantity)));
                    }
                }
            }
            adapter.notifyDataSetChanged();
            price = decimal.toString();
            setPrice();
        }
    }

    // 显示当前总价
    private void setPrice() {
        priceView.setText(String.format(getString(R.string.cart_shop_price_format), price));
    }

    // 更新购物车的数量
    private void countUpdate(final ShopItemBean sib, final int count, final TextView countView) {
        activity.showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                activity.dismissLoadDialog();
                sib.quantity = count;
                countView.setText(String.valueOf(count));
                if (sib.isSelect) {
                    caculatePrice();
                }
            }

            @Override
            public void error() {
                activity.dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(activity, callbackInterface, "cart/update",
                "id", sib.product_id, "nums", String.valueOf(count));
    }

    // 删除选中商品
    private void shopDelete(String ids) {
        activity.showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                activity.dismissLoadDialog();
                price = "0";
                setPrice();
                parserData(response.datum);
            }

            @Override
            public void error() {
                activity.dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(activity, callbackInterface, "cart/delete",
                "id", ids);
    }

    // 点击全选
    private void selectAll(View v) {
        boolean isSelelct = !v.isSelected();
        v.setSelected(isSelelct); // 点击全选按钮
        ArrayList<CartBean> list = (ArrayList<CartBean>) adapter.list;
        if (list != null) {
            BigDecimal decimal = new BigDecimal("0");
            for (CartBean cb : list) {
                cb.isSelect = isSelelct;
                for (ShopItemBean sib : cb.cartItems) {
                    sib.isSelect = isSelelct;
                    if (isSelelct) {
                        decimal = decimal.add(new BigDecimal(sib.price).multiply(new BigDecimal(sib.quantity)));
                    }
                }
            }
            adapter.notifyDataSetChanged();
            price = decimal.toString();
            setPrice();
        }
    }

    // 确认支付
    private void pay(){
        ArrayList<BaseProductBean> products = new ArrayList<>();
        ArrayList<CartBean> list = (ArrayList<CartBean>) adapter.list;
        if (list != null) {
            for (CartBean cb : list) {
                for (ShopItemBean sib : cb.cartItems) {
                    if(sib.isSelect){
                        products.add(sib);
                    }
                }
            }
        }
        if(products.isEmpty()){
            activity.showToast("请选择要购买的商品");
            return;
        }
        Intent intent = new Intent(activity, OrderActivity.class);
        intent.putExtra("fromCart", true);
        intent.putExtra("product", products);
        intent.putExtra("address", address);
        intent.putExtra("coupon", couponList);
        intent.setAction("购物车结算");
        startActivity(intent);
    }

    class ShopCartAdapter extends BaseClassAdapter {
        public ShopCartAdapter(ArrayList<?> list, BaseActivity context) {
            super(list, context, 1);
        }

        @Override
        protected ParentViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, ParentViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            CartBean cb = (CartBean) getItem(position);
            ho.titleView.setText(cb.name);
            ho.selectView.setTag(cb);
            ho.selectView.setSelected(cb.isSelect);
            ho.contentView.setTag(cb);
            ho.contentView.setTag(R.id.cart_class_select, ho.selectView);
        }

        @Override
        protected ArrayList<?> getItemList(int position) {
            return ((CartBean) list.get(position)).cartItems;
        }

        @Override
        protected View createItemView(Context context) {
            return View.inflate(context, R.layout.item_cart_shop, null);
        }

        @Override
        protected void bindItemData(Object obj, ItemViewHolder holder) {
            ShopItemBean sib = (ShopItemBean) obj;
            ShopViewHolder ho = (ShopViewHolder) holder;
            Tools.setImage(context, ho.imageView, sib.image);
            ho.nameView.setText(sib.name);
            ho.countView.setText(String.valueOf(sib.quantity));
            ho.priceView.setText(String.format(getString(R.string.price_format), sib.price));
            ho.selectView.setTag(sib);
            ho.selectView.setSelected(sib.isSelect);
            ho.addView.setTag(R.id.cart_item_content, sib);
            ho.minusView.setTag(R.id.cart_item_content, sib);
        }

        @Override
        protected ItemViewHolder bindItemViewHolder(View v) {
            return new ShopViewHolder(v);
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_shop_cart, null);
        }

        @Override
        public void onClick(View v) {

        }

        // 获取选中id
        public String getSelectIds() {
            if (list == null) {
                return null;
            }
            StringBuffer ids = new StringBuffer();
            for (Object ob : list) {
                CartBean cb = (CartBean) ob;
                for (ShopItemBean sib : cb.cartItems) {
                    if (sib.isSelect) {
                        if(ids.length() > 0){
                            ids.append(",");
                        }
                        ids.append(sib.id);
                    }
                }
            }
            return ids.length() == 0 ? null : ids.toString();
        }

        class ViewHolder extends ParentViewHolder implements View.OnClickListener {
            TextView titleView;
            View selectView;

            public ViewHolder(View view) {
                super(view);
                titleView = (TextView) view.findViewById(R.id.cart_class_title);
                selectView = view.findViewById(R.id.cart_class_select);
                selectView.setOnClickListener(this);
            }

            @Override
            protected LinearLayout getContentView(View view) {
                return (LinearLayout) view.findViewById(R.id.cart_item_content);
            }

            @Override
            public void onClick(View v) {
                CartBean cb = (CartBean) v.getTag();
                cb.click();
                notifyDataSetChanged();
                if (!cb.isSelect) {
                    selectView.setSelected(false);
                }
                caculatePrice();
            }
        }

        class ShopViewHolder extends ItemViewHolder implements View.OnClickListener {
            View selectView, addView, minusView;
            ImageView imageView;
            TextView nameView, priceView, countView;

            public ShopViewHolder(View v) {
                super(v);
                selectView = v.findViewById(R.id.shop_select_icon);
                selectView.setOnClickListener(this);
                addView = v.findViewById(R.id.count_add);
                addView.setOnClickListener(this);
                minusView = v.findViewById(R.id.count_minus);
                minusView.setOnClickListener(this);
                addView.setTag(R.id.count_view, countView);
                minusView.setTag(R.id.count_view, countView);

                imageView = (ImageView) v.findViewById(R.id.shop_detail_photo);
                nameView = (TextView) v.findViewById(R.id.shop_detail_title);
                priceView = (TextView) v.findViewById(R.id.shop_detail_price);
                countView = (TextView) v.findViewById(R.id.count_view);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.shop_select_icon) {
                    itemSelect(v);
                    caculatePrice();
                } else {
                    ShopItemBean sib = (ShopItemBean) v.getTag(R.id.cart_item_content);
                    TextView countView = (TextView) v.getTag(R.id.count_view);
                    int count = Integer.parseInt(countView.getText().toString());
                    switch (v.getId()) {
                        case R.id.count_add:
                            count++;
                            break;
                        case R.id.count_minus:
                            if (count > 0) {
                                count--;
                            } else {
                                return;
                            }
                            break;
                    }
                    countUpdate(sib, count, countView);
                }
            }

            // 复选当前商品
            private void itemSelect(View v) {
                ShopItemBean sib = (ShopItemBean) v.getTag();
                if (sib.isSelect) {
                    selectView.setSelected(false);
                }
                sib.isSelect = !sib.isSelect;
                v.setSelected(sib.isSelect);
                View contentView = (View) v.getParent().getParent();
                View selectView = (View) contentView.getTag(R.id.cart_class_select);
                CartBean cb = (CartBean) contentView.getTag();
                if (cb.isSelect) { // 原本是分类全选 点击后必然不是分类全选
                    cb.isSelect = false;
                    selectView.setSelected(false);
                } else { // 检查是否全部分类商品都选了, 是的话标记为全选
                    boolean allSelect = true;
                    for (ShopItemBean s : cb.cartItems) {
                        if (!s.isSelect) {
                            allSelect = false;
                            break;
                        }
                    }
                    if (allSelect) {
                        cb.isSelect = true;
                        selectView.setSelected(true);
                    }
                }
            }
        }
    }
}

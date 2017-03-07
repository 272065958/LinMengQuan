package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.BaseProductBean;
import net.kamfat.omengo.bean.GoodsBean;
import net.kamfat.omengo.bean.SortBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/7.
 */
public class ShopListActivity extends GoodBuyActivity {
    ArrayList<SortBean> sortList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        Intent intent = getIntent();
        setToolBar(true, null, intent.getAction());

        addShopCart();

        loadData();
    }

    private void addShopCart() {
        View shopCartView = View.inflate(this, R.layout.view_shop_cart, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        getToolBar().addView(shopCartView);
    }

    private void loadData() {
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                findViewById(R.id.loading_view).setVisibility(View.GONE);
                if (response.datum != null) {
                    try {
                        JSONArray array = new JSONArray(response.datum);
                        sortList = JsonParser.getInstance().fromJson(array.getString(0), new TypeToken<ArrayList<SortBean>>() {
                        }.getType());
                        cartCount = array.getInt(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (sortList == null || sortList.isEmpty()) {
                    showToast("没有数据");
                    finish();
                    return;
                }
                findViewById();
                diaplayData(sortList);
            }

            @Override
            public void error() {
                findViewById(R.id.loading_view).setVisibility(View.GONE);
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "app/newSecondIndex", "key", getIntent().getStringExtra("key"),
                "type", getIntent().getStringExtra("type"));
    }

    protected void findViewById() {
        super.findViewById();
        findViewById(R.id.goods_content).setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_button).setVisibility(View.VISIBLE);
        showCartCount();
    }

    GoodsAdapter goodsAdapter;
    SortAdapter sortAdapter;
    SortBean currentSort;

    // 显示商品
    private void diaplayData(ArrayList<SortBean> list) {
        ListView sortView = (ListView) findViewById(R.id.shop_class_view);
        assert sortView != null;
        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SortAdapter adapter = (SortAdapter) parent.getAdapter();
                SortBean sb = (SortBean) adapter.getItem(position);
                currentSort = sb;
                adapter.setCurrentSelect(position);
                goodsAdapter.notifyDataSetChanged(sb.detail);
            }
        });
        sortAdapter = new SortAdapter(list, this);
        sortView.setAdapter(sortAdapter);
        ListView goodsView = (ListView) findViewById(R.id.shop_list_view);
        assert goodsView != null;
        goodsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        currentSort = list.get(0);
        goodsAdapter = new GoodsAdapter(currentSort.detail, this);
        goodsView.setAdapter(goodsAdapter);
    }

    @Override
    protected ArrayList<BaseProductBean> getProducts() {
        ArrayList<BaseProductBean> list = new ArrayList<>();
        if (sortList != null && !sortList.isEmpty()) {
            for (SortBean sb : sortList) {
                for (GoodsBean gb : sb.buyList) {
                    list.add(gb);
                }
            }
        }
        return list;
    }

    @Override
    protected String[] getProductIdAndCount() {
        StringBuilder ids = new StringBuilder();
        StringBuilder quantitys = new StringBuilder();
        if (sortList != null && !sortList.isEmpty()) {
            for (SortBean sb : sortList) {
                for (GoodsBean gb : sb.buyList) {
                    if (ids.length() > 0) {
                        ids.append(",");
                        quantitys.append(",");
                    }
                    ids.append(gb.id);
                    quantitys.append(gb.quantity);
                }
            }
        }
        return new String[]{ids.toString(), quantitys.toString()};
    }

    class SortAdapter extends MyBaseAdapter {

        int currentSelectPosition = 0;

        public SortAdapter(ArrayList<SortBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_sort, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            View v = ho.getView();
            if (position == currentSelectPosition) {
                v.setBackgroundColor(ContextCompat.getColor(context, R.color.item_pressed_color));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }
            SortBean sb = (SortBean) getItem(position);
            ho.nameView.setText(sb.name);
            if (sb.buyList.isEmpty()) {
                ho.countView.setVisibility(View.GONE);
            } else {
                ho.countView.setVisibility(View.VISIBLE);
                int count = 0;
                for (GoodsBean gb : sb.buyList) {
                    count += gb.quantity;
                }
                ho.countView.setText(String.valueOf(count));
            }
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            return new ViewHolder(v);
        }

        public void setCurrentSelect(int position) {
            currentSelectPosition = position;
            notifyDataSetChanged();
        }

        class ViewHolder extends MyViewHolder {
            TextView countView, nameView;

            public ViewHolder(View v) {
                super(v);
                countView = (TextView) v.findViewById(R.id.shop_count);
                nameView = (TextView) v.findViewById(R.id.shop_sort);
            }
        }
    }

    class GoodsAdapter extends MyBaseAdapter {

        public GoodsAdapter(ArrayList<GoodsBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_goods, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            GoodsBean gb = (GoodsBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            ho.priceView.setText(String.format(getString(R.string.price_format), gb.price));
//            ho.stockView.setText("库存：" + gb.stock);
            ho.titleView.setText(gb.name);
            Tools.setImage(context, ho.imageView, gb.image);
            ho.addView.setTag(gb);
            ho.minusView.setTag(gb);
            ho.addView.setTag(R.id.count_view, ho.countView);
            ho.minusView.setTag(R.id.count_view, ho.countView);
            ho.countView.setText(String.valueOf(gb.quantity));
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            return new ViewHolder(v);
        }

        class ViewHolder extends MyViewHolder implements View.OnClickListener {
            View rebateView, addView, minusView;
            ImageView imageView;
            TextView titleView, priceView, stockView, countView;

            public ViewHolder(View v) {
                super(v);
//                stockView = (TextView) v.findViewById(R.id.goods_count);
                rebateView = v.findViewById(R.id.goods_rebate);
                imageView = (ImageView) v.findViewById(R.id.goods_image);
                titleView = (TextView) v.findViewById(R.id.goods_title);
                priceView = (TextView) v.findViewById(R.id.goods_price);
                addView = v.findViewById(R.id.count_add);
                addView.setOnClickListener(this);
                minusView = v.findViewById(R.id.count_minus);
                minusView.setOnClickListener(this);
                countView = (TextView) v.findViewById(R.id.count_view);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.count_add:
                        shopCountAdd(v);
                        break;
                    case R.id.count_minus:
                        shopCountMinus(v);
                        break;
                }
            }
        }
    }

    public void shopCountMinus(View view) {
        GoodsBean gb = (GoodsBean) view.getTag();
        if (gb.quantity > 0) {
            gb.quantity--;
            TextView buyCountView = (TextView) view.getTag(R.id.count_view);
            buyCountView.setText("" + gb.quantity);
            BigDecimal total = new BigDecimal(allPrice);
            BigDecimal price = new BigDecimal(gb.price);
            allPrice = total.subtract(price).toString();
            showPrice();
            if (gb.quantity == 0) {
                SortBean sb = currentSort;
                sb.buyList.remove(gb);
            }
            sortAdapter.notifyDataSetChanged();
        }
    }

    public void shopCountAdd(View view) {
        GoodsBean gb = (GoodsBean) view.getTag();
//        if(gb.stock > gb.buyCount){ // 有存货才给加
        if (gb.quantity == 0) {
            SortBean sb = currentSort;
            sb.buyList.add(gb);
        }
        gb.quantity++;
        TextView buyCountView = (TextView) view.getTag(R.id.count_view);
        buyCountView.setText("" + gb.quantity);
        BigDecimal total = new BigDecimal(allPrice);
        BigDecimal price = new BigDecimal(gb.price);
        allPrice = total.add(price).toString();
        showPrice();
//        }
        sortAdapter.notifyDataSetChanged();
    }
}
package net.kamfat.omengo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.adapter.MyPagerAdapter;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.component.tablayout.TabLayout;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/8.
 */
public class CouponActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
    ListView[] listViews;
    View[] loadViews;
    CouponAdapter[] adapters;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        setToolBar(true, null, R.string.myself_coupon);

        initPagerView();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (listViews[position].getTag() != null) {
            loadData(position);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    // 初始化界面
    private void initPagerView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        int pageCount;
        String[] titles;

        titles = new String[]{
                getString(R.string.coupon_no_use),
                getString(R.string.coupon_use), getString(R.string.coupon_outtime)};
        pageCount = titles.length;
        listViews = new ListView[pageCount];
        loadViews = new View[pageCount];
        adapters = new CouponAdapter[pageCount];
        View views[] = new View[pageCount];
        AbsListView.LayoutParams alp = new AbsListView.LayoutParams(0, getResources().getDimensionPixelOffset(R.dimen.auto_space));
        for (int i = 0; i < pageCount; i++) {
            View v = View.inflate(this, R.layout.item_list_view, null);
            ListView listView = (ListView) v.findViewById(R.id.list_view);
            listView.setDivider(ContextCompat.getDrawable(this, R.drawable.listview_divider));
            listView.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.auto_space));
            View footer = new View(this);
            footer.setLayoutParams(alp);
            listView.addFooterView(footer);
            views[i] = v;
            listView.setTag(true);
            listViews[i] = listView;
            loadViews[i] = v.findViewById(R.id.loading_view);
        }
        MyPagerAdapter adapter = new MyPagerAdapter(views, titles);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(1).setIcon(R.drawable.new_message);
    }

    private void loadData(int position){
        loadViews[position].setVisibility(View.VISIBLE);
        ListView listView = listViews[position];
        if (listView.getTag() != null) {
            listView.setTag(null);
        }
        ArrayList<CouponBean> list = new ArrayList<>();
        CouponBean cb;
        if(position == 0){
            cb = new CouponBean();
            cb.time = "有效期：2016.05.24-2016.09.24";
            cb.tip = "满 150 使用";
            cb.title = "￥50";
            cb.type = "优惠券";
            cb.use = "单次清洁专用";
            list.add(cb);
            cb = new CouponBean();
            cb.time = "有效期：2016.05.24-2016.09.24";
            cb.tip = "单次清洁服务券";
            cb.title = "单次清洁一次";
            cb.type = "服务卷";
            cb.use = "在线下单后结算时使用";
            list.add(cb);
        }else if(position == 1){
            cb = new CouponBean();
            cb.time = "有效期：2016.05.24-2016.09.24";
            cb.tip = "满 150 使用";
            cb.title = "￥50";
            cb.type = "优惠券";
            cb.use = "单次清洁专用";
            list.add(cb);
        }else {
            cb = new CouponBean();
            cb.time = "有效期：2016.05.24-2016.09.24";
            cb.tip = "单次清洁服务券";
            cb.title = "单次清洁一次";
            cb.type = "服务卷";
            cb.use = "在线下单后结算时使用";
            list.add(cb);
        }
        displayData(position, list);
    }

    private void displayData(int position, ArrayList<CouponBean> list){
        loadViews[position].setVisibility(View.GONE);
        CouponAdapter adapter = adapters[position];
        if (adapter == null) {
            adapter = new CouponAdapter(list, CouponActivity.this);
            listViews[position].setVisibility(View.VISIBLE);
            listViews[position].setAdapter(adapter);
            adapters[position] = adapter;
        } else {
            adapter.notifyDataSetChanged(list);
        }
    }

    class CouponBean {
        String title;
        String tip;
        String time;
        String type;
        String use;
    }

    class CouponAdapter extends MyBaseAdapter{
        public CouponAdapter(ArrayList<CouponBean> list, BaseActivity context){
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_coupon, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            CouponBean cb = (CouponBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;

        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);

            return ho;
        }

        class ViewHolder extends MyViewHolder{
            TextView titleView, tipView, timeView, typeView, useView;
            public ViewHolder(View view){
                super(view);
            }
        }
    }
}

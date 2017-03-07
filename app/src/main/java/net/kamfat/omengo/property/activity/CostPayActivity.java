package net.kamfat.omengo.property.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/9.
 */
public class CostPayActivity extends BaseActivity {
    TextView priceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_pay);

        setToolBar(true, null, R.string.property_cost_pay);

        priceView = (TextView) findViewById(R.id.pay_price);

        initData();
    }

    private void initData() {
        ArrayList<MonthBean> list = new ArrayList<>();
        MonthBean mb = new MonthBean();
        mb.price = 100.50f;
        mb.time = "2016-01";
        list.add(mb);
        mb = new MonthBean();
        mb.price = 200.50f;
        mb.time = "2016-02";
        list.add(mb);
        mb = new MonthBean();
        mb.price = 300.50f;
        mb.time = "2016-03";
        list.add(mb);
        mb = new MonthBean();
        mb.price = 400.50f;
        mb.time = "2016-04";
        list.add(mb);
        mb = new MonthBean();
        mb.price = 120.50f;
        mb.time = "2016-05";
        list.add(mb);
        mb = new MonthBean();
        mb.price = 140.50f;
        mb.time = "2016-06";
        list.add(mb);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new MonthAdapter(list, this));
    }

    public void onClick(View view) {
        Toast.makeText(this, getString(R.string.only_demo), Toast.LENGTH_SHORT).show();
    }

    public void propertySelect(View view) {
        // 选择物业
        Intent intent = new Intent(this, PropertyChangeActivity.class);
        startActivity(intent);
    }

    class MonthBean {
        String time;
        float price;
        boolean isSelect;
    }

    class MonthAdapter extends MyBaseAdapter implements View.OnClickListener {
        public MonthAdapter(ArrayList<MonthBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        public void onClick(View v) {
            v.setSelected(true);
            int currentSelect = (int) v.getTag();
            if(currentSelect < getCount()-1){
                for(int i=currentSelect+1; i<getCount(); i++){
                    ((MonthBean)list.get(i)).isSelect = false;
                }
            }
            float total = 0;
            for(int i=0;i<currentSelect+1; i++){
                MonthBean mb = (MonthBean)list.get(i);
                mb.isSelect = true;
                total += mb.price;
            }
            priceView.setText("￥"+total);
            notifyDataSetChanged();
        }

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.item_month, null);
            view.setOnClickListener(this);
            return view;
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            MonthBean mb = (MonthBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            View v = ho.getView();
            v.setTag(position);
            ho.priceView.setText("￥" + mb.price);
            ho.dateview.setText(mb.time);
            if(mb.isSelect){
                v.setBackgroundResource(R.drawable.main_frame_bg);
            }else{
                v.setBackgroundResource(R.drawable.black_frame_bg);
            }
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.priceView = (TextView) v.findViewById(R.id.month_price);
            ho.dateview = (TextView) v.findViewById(R.id.month_date);
            return ho;
        }

        class ViewHolder extends MyViewHolder {
            TextView priceView, dateview;

            public ViewHolder(View v) {
                super(v);
            }
        }
    }
}

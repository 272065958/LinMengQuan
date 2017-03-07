package net.kamfat.omengo.property.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/9.
 */
public class PropertyCostActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_cost);

        setToolBar(true, null, R.string.property_cost_search);

        initData();
        TextView bottomBtn = (TextView) findViewById(R.id.bottom_button);
        bottomBtn.setText("立即缴费");
    }

    public void onClick(View view){
        Intent payIntent = new Intent(this, CostPayActivity.class);
        startActivity(payIntent);
    }

    public void propertySelect(View view){
        // 选择物业
        Intent intent = new Intent(this, PropertyChangeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.property_cost, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cost:
                Intent intent = new Intent(this, CostRecordActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        ArrayList<CostBean> list = new ArrayList<>();
        CostBean cb = new CostBean();
        cb.date = "2016-09";
        cb.status = 0;
        cb.expend = true;
        cb.hasPay = "已缴\n排污费：￥500.00\n生活垃圾费：￥100.00";
        cb.noPay = "未缴\n物业费：￥400.00\n水费：￥98.00\n电费：1380.00";
        list.add(cb);
        cb = new CostBean();
        cb.date = "2016-08";
        cb.status = 1;
        cb.hasPay = "已缴\n物业费：￥400.00\n水费：￥98.00\n电费：1380.00\n排污费：￥500.00\n生活垃圾费：￥100.00";
        cb.expend = false;
        list.add(cb);
        cb = new CostBean();
        cb.date = "2016-07";
        cb.status = 1;
        cb.hasPay = "已缴\n物业费：￥400.00\n水费：￥98.00\n电费：1380.00\n排污费：￥500.00\n生活垃圾费：￥100.00";
        cb.expend = false;
        list.add(cb);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.addHeaderView(getHeaderView());
        listView.setAdapter(new CostAdapter(list, this));
    }

    private View getHeaderView(){
        return View.inflate(this, R.layout.property_cost_header, null);
    }

    class CostBean {
        String date;
        int status;
        String hasPay;
        String noPay;
        boolean expend;
    }

    class CostAdapter extends MyBaseAdapter implements View.OnClickListener {
        public CostAdapter(ArrayList<CostBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_cost, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            CostBean cb = (CostBean) getItem(position);
            ho.dateView.setText(cb.date);
            if (cb.status == 0) {
                ho.statusView.setText("未缴");
            } else {
                ho.statusView.setText("已缴");
            }
            ho.titleContent.setTag(cb);
            ho.titleContent.setTag(R.id.cost_title_content, ho);
            if (cb.expend) {
                ho.iconView.setImageResource(R.drawable.ic_expend);
                ho.detailContent.setVisibility(View.VISIBLE);
                ho.hasPayView.setText(cb.hasPay);
                ho.noPayView.setText(cb.noPay);
            } else {
                ho.iconView.setImageResource(R.drawable.enter_icon);
                ho.detailContent.setVisibility(View.GONE);
            }
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.dateView = (TextView) v.findViewById(R.id.cost_time);
            ho.statusView = (TextView) v.findViewById(R.id.cost_status);
            ho.noPayView = (TextView) v.findViewById(R.id.cost_no_pay);
            ho.hasPayView = (TextView) v.findViewById(R.id.cost_has_pay);
            ho.iconView = (ImageView) v.findViewById(R.id.cost_detail_icon);
            ho.detailContent = v.findViewById(R.id.cost_pay_content);
            ho.titleContent = v.findViewById(R.id.cost_title_content);
            ho.titleContent.setOnClickListener(this);
            return ho;
        }

        @Override
        public void onClick(View v) {
            CostBean cb = (CostBean) v.getTag();
            ViewHolder ho = (ViewHolder) v.getTag(R.id.cost_title_content);
            if (cb.expend) {
                cb.expend = false;
                ho.iconView.setImageResource(R.drawable.enter_icon);
                ho.detailContent.setVisibility(View.GONE);
            } else {
                cb.expend = true;
                ho.iconView.setImageResource(R.drawable.ic_expend);
                ho.detailContent.setVisibility(View.VISIBLE);
                ho.hasPayView.setText(cb.hasPay);
                ho.noPayView.setText(cb.noPay);
            }
        }

        class ViewHolder extends MyViewHolder {
            TextView dateView, statusView, noPayView, hasPayView;
            ImageView iconView;
            View detailContent, titleContent;

            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}

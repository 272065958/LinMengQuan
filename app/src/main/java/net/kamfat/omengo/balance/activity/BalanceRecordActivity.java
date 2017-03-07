package net.kamfat.omengo.balance.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/8.
 */
public class BalanceRecordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_record);

        setToolBar(true, null, R.string.balance_record);

        initData();
    }

    private void initData(){
        ArrayList<RecordBean> list = new ArrayList<>();
        RecordBean rb = new RecordBean();
        rb.balance = "200.00";
        rb.change = "-12.00";
        rb.operator = "王小帅";
        rb.remark = "散单清洁消费";
        rb.time = "2016-09-08 13:44";
        list.add(rb);
        rb = new RecordBean();
        rb.balance = "212.00";
        rb.change = "-12.00";
        rb.operator = "王小帅";
        rb.remark = "散单清洁消费";
        rb.time = "2016-09-03 12:04";
        list.add(rb);
        rb = new RecordBean();
        rb.balance = "224.00";
        rb.change = "-12.00";
        rb.operator = "王小帅";
        rb.remark = "散单清洁消费";
        rb.time = "2016-09-01 15:04";
        list.add(rb);
        rb = new RecordBean();
        rb.balance = "236.00";
        rb.change = "-25.00";
        rb.operator = "王小帅";
        rb.remark = "散单清洁消费";
        rb.time = "2016-08-27 10:54";
        list.add(rb);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new RecordAdapter(list, this));
    }

    class RecordBean {
        String time;
        String change;
        String balance;
        String remark;
        String operator;
    }

    class RecordAdapter extends MyBaseAdapter {
        public RecordAdapter(ArrayList<RecordBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_balance_record, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            RecordBean rb = (RecordBean)getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            ho.balanceView.setText(rb.balance);
            ho.changeView.setText(rb.change);
            ho.operatorView.setText(rb.operator);
            ho.remarkView.setText(rb.remark);
            ho.timeView.setText(rb.time);
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.balanceView = (TextView) v.findViewById(R.id.balance_record_balance);
            ho.changeView = (TextView) v.findViewById(R.id.balance_record_change);
            ho.operatorView = (TextView) v.findViewById(R.id.balance_record_operator);
            ho.remarkView = (TextView) v.findViewById(R.id.balance_record_remark);
            ho.timeView = (TextView) v.findViewById(R.id.balance_record_time);
            return ho;
        }

        class ViewHolder extends MyViewHolder{
            TextView timeView, changeView, balanceView, remarkView, operatorView;
            public ViewHolder(View v){
                super(v);
            }
        }
    }
}

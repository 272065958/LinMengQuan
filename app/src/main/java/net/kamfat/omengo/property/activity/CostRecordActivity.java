package net.kamfat.omengo.property.activity;

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
 * Created by cjx on 2016/9/9.
 */
public class CostRecordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_record);

        setToolBar(true, null, R.string.property_cost_record);

        initData();
    }

    private void initData(){
        ArrayList<RecordBean> list = new ArrayList<>();
        RecordBean rb = new RecordBean();
        rb.time = "2016-04-14 09:45";
        rb.address = "嘉信城市花园1期22栋";
        rb.months = "2016-01-23  2016-02-23  2016-03-23";
        rb.price = "￥1000.00";
        rb.number = "1367067519";
        list.add(rb);
        rb = new RecordBean();
        rb.time = "2016-04-14 09:45";
        rb.address = "嘉信城市花园1期22栋";
        rb.months = "2016-01-23  2016-02-23  2016-03-23";
        rb.price = "￥1000.00";
        rb.number = "1367067519";
        list.add(rb);
        rb = new RecordBean();
        rb.time = "2016-04-14 09:45";
        rb.address = "嘉信城市花园1期22栋";
        rb.months = "2016-01-23  2016-02-23  2016-03-23";
        rb.price = "￥1000.00";
        rb.number = "1367067519";
        list.add(rb);
        rb = new RecordBean();
        rb.time = "2016-04-14 09:45";
        rb.address = "嘉信城市花园1期22栋";
        rb.months = "2016-01-23  2016-02-23  2016-03-23";
        rb.price = "￥1000.00";
        rb.number = "1367067519";
        list.add(rb);
        rb = new RecordBean();
        rb.time = "2016-04-14 09:45";
        rb.address = "嘉信城市花园1期22栋";
        rb.months = "2016-01-23  2016-02-23  2016-03-23";
        rb.price = "￥1000.00";
        rb.number = "1367067519";
        list.add(rb);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new RecordAdapter(list, this));
    }

    class RecordBean{
        public String time;
        public String address;
        public String months;
        public String price;
        public String number;
    }

    class RecordAdapter extends MyBaseAdapter {

        public RecordAdapter(ArrayList<RecordBean> list, BaseActivity context){
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_cost_record, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            RecordBean rb = (RecordBean) getItem(position);
            ho.addressView.setText(rb.address);
            ho.timeView.setText(rb.time);
            ho.monthView.setText(rb.months);
            ho.priceVIew.setText(rb.price);
            ho.numberView.setText(rb.number);
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.addressView = (TextView) v.findViewById(R.id.record_address);
            ho.timeView = (TextView) v.findViewById(R.id.record_time);
            ho.monthView = (TextView) v.findViewById(R.id.record_month);
            ho.priceVIew = (TextView) v.findViewById(R.id.record_price);
            ho.numberView = (TextView) v.findViewById(R.id.record_number);
            return ho;
        }

        class ViewHolder extends MyViewHolder {
            TextView timeView, addressView, monthView, priceVIew, numberView;
            public ViewHolder(View view){
                super(view);
            }
        }
    }
}

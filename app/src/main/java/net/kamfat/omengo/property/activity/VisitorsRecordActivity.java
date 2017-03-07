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
 * Created by cjx on 2016/9/12.
 */
public class VisitorsRecordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_record);
        setToolBar(true, null, R.string.visitors_record);

        initData();
    }

    private void initData(){
        ArrayList<RecordBean> list = new ArrayList<>();
        RecordBean rb = new RecordBean();
        rb.code = "952727";
        rb.name = "何晓明";
        rb.remark = "访客联系方式:1367067595 多人同进";
        rb.time = "09-30 18:30分前";
        rb.status = "未进门";
        list.add(rb);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new RecordAdapter(list, this));
    }

    class RecordBean{
        String code;
        String name;
        String remark;
        String time;
        String status;
    }

    class RecordAdapter extends MyBaseAdapter{
        public RecordAdapter(ArrayList<RecordBean> list, BaseActivity context){
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_visitors_record, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            RecordBean rb = (RecordBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            ho.codeView.setText(rb.code);
            ho.nameView.setText(rb.name);
            ho.remarkView.setText(rb.remark);
            ho.timeView.setText(rb.time);
            ho.statusView.setText(rb.status);
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.codeView = (TextView) v.findViewById(R.id.visitors_record_code);
            ho.nameView = (TextView) v.findViewById(R.id.visitors_record_visitor);
            ho.remarkView = (TextView) v.findViewById(R.id.visitors_record_remark);
            ho.timeView = (TextView) v.findViewById(R.id.visitors_record_time);
            ho.statusView = (TextView) v.findViewById(R.id.visitors_status);
            return ho;
        }

        class ViewHolder extends MyViewHolder{
            TextView codeView, nameView, remarkView, timeView, statusView;
            public ViewHolder(View view){
                super(view);
            }
        }
    }
}

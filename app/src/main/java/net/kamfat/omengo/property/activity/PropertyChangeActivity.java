package net.kamfat.omengo.property.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.PropertyBean;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/7.
 */
public class PropertyChangeActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_change);
        setToolBar(true, null, R.string.property_select);
        loadData();
        TextView button = (TextView) findViewById(R.id.bottom_button);
        button.setText("新增地址");
    }

    public void onClick(View view){
        Toast.makeText(this, getString(R.string.only_demo), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        finish();
    }

    private void loadData(){
        ArrayList<PropertyBean> list = new ArrayList<>();
        PropertyBean pb = new PropertyBean();
        pb.city = "广东省   佛山市";
        pb.position = "2座3E";
        pb.project = "嘉信城市花园1期";
        list.add(pb);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.addFooterView(new View(this));
        listView.setAdapter(new PropertyAdapter(list, this));
        listView.setOnItemClickListener(this);
    }

    class PropertyAdapter extends MyBaseAdapter {

        public PropertyAdapter(ArrayList<PropertyBean> list, BaseActivity context){
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_property, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            PropertyBean pb = (PropertyBean) getItem(position);
            ho.cityView.setText(pb.city);
            ho.positionView.setText(pb.position);
            ho.projectView.setText(pb.project);
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.cityView = (TextView) v.findViewById(R.id.property_city);
            ho.projectView = (TextView) v.findViewById(R.id.property_project);
            ho.positionView = (TextView) v.findViewById(R.id.property_position);
            ho.selectView = v.findViewById(R.id.property_select);
            return ho;
        }

        class ViewHolder extends MyViewHolder{
            TextView projectView, positionView, cityView;
            View selectView;
            public ViewHolder(View view){
                super((view));
            }
        }
    }
}

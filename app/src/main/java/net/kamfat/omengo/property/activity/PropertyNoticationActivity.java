package net.kamfat.omengo.property.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/9/9.
 */
public class PropertyNoticationActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_notication);

        setToolBar(true, null, R.string.property_notication);
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 排除一个header
        if(position == 0){
            return ;
        }
        Intent intent = new Intent(this, PropertyNoticationDetailActivity.class);
        NoticationBean nb = (NoticationBean) parent.getAdapter().getItem(position);
        intent.putExtra("title", nb.title);
        intent.putExtra("time", nb.time);
        intent.putExtra("detail", nb.detail);
        startActivity(intent);
    }

    private void initData(){
        ArrayList<NoticationBean> list = new ArrayList<>();
        NoticationBean nb = new NoticationBean();
        nb.title = "嘉信城市花园一三五期通告";
        nb.time = "2016-9-9";
        nb.detail = "按顺德天气预计, 8月1日-8月3日顺德将有一次暴雨到大暴雨降水过程, 并伴8-10级大风等强对流天气, 现我区已进入暴雨和强对流天气频发期";
        list.add(nb);
        nb = new NoticationBean();
        nb.title = "嘉信城市花园一三五期通告";
        nb.time = "2016-9-9";
        nb.detail = "按顺德天气预计, 8月1日-8月3日顺德将有一次暴雨到大暴雨降水过程, 并伴8-10级大风等强对流天气, 现我区已进入暴雨和强对流天气频发期";
        list.add(nb);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.addHeaderView(getHeaderView());
        listView.setAdapter(new NoticationAdapter(list, this));
        listView.setOnItemClickListener(this);
    }

    private View getHeaderView(){
        ImageView header = new ImageView(this);
        int screenWidth = ((OmengoApplication)getApplication()).getScreen_width();
        int height = (int) (screenWidth * 22 / 72f);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        header.setLayoutParams(lp);
        return header;
    }

    class NoticationBean{
        String title;
        String detail;
        String time;
    }

    class NoticationAdapter extends MyBaseAdapter{
        public NoticationAdapter(ArrayList<NoticationBean> list, BaseActivity context){
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_notication, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            NoticationBean nb = (NoticationBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            ho.timeView.setText(nb.time);
            ho.titleView.setText(nb.title);
            ho.detailView.setText(nb.detail);
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.timeView = (TextView) v.findViewById(R.id.notication_time);
            ho.titleView = (TextView) v.findViewById(R.id.notication_title);
            ho.detailView = (TextView) v.findViewById(R.id.notication_detail);
            return ho;
        }

        class ViewHolder extends MyViewHolder{
            TextView titleView, timeView, detailView;
            public ViewHolder(View v){
                super(v);
            }
        }
    }
}

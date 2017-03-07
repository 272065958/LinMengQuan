package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.BaseClassAdapter;
import net.kamfat.omengo.base.BaseListActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.ClassBean;
import net.kamfat.omengo.bean.ServerBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/12/29.
 */
public class AllClassActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setToolBar(true, null, getIntent().getAction());
        initView(null);
        setListViweDivider(null, 0);
        loadData();
    }

    @Override
    protected MyBaseAdapter getMyBaseAdapter(ArrayList<?> list) {
        return new ClassAdapter(list, this);
    }

    @Override
    protected void loadData() {
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                hideLoadView();
                try {
                    JSONArray json = new JSONArray(response.datum);
                    String array = json.getString(1);
                    ArrayList<ClassBean> list = JsonParser.getInstance().fromJson(array, new TypeToken<ArrayList<ClassBean>>() {
                    }.getType());
                    onLoadResult(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                hideLoadView();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface,
                "app/secondPage", "key", getIntent().getStringExtra("key"));
    }

    class ClassAdapter extends BaseClassAdapter {
        public ClassAdapter(ArrayList<?> list, BaseActivity context) {
            super(list, context, 3);
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_all_class, null);
        }

        @Override
        protected ParentViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, ParentViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            ClassBean cb = (ClassBean) getItem(position);
            ho.nameView.setText(cb.name);
        }

        @Override
        protected ArrayList<?> getItemList(int position) {
            return ((ClassBean)list.get(position)).children;
        }

        @Override
        protected View createItemView(Context context) {
            return View.inflate(context, R.layout.item_server_class, null);
        }

        @Override
        protected void bindItemData(Object scb, ItemViewHolder holder) {
            ServerBean cb = (ServerBean) scb;
            TextView v = (TextView)holder.getItemView();
            v.setText(cb.name);
            v.setTag(cb);
        }

        @Override
        public void onClick(View v) {
            ServerBean sb = (ServerBean) v.getTag();
            if(sb == null){
                return;
            }
            Intent intent = null;
            switch (sb.type) {
                case "1": // 进入二级菜单页面
                    intent = new Intent(context, ServerSelectActivity.class);
                    break;
                case "2": // 日用商品
                    intent = new Intent(context, ShopListActivity.class);
                    break;
                case "4": // 全部分类
                    intent = new Intent(context, AllClassActivity.class);
                    break;
            }
            if(intent != null){
                intent.putExtra("key", sb.key);
                intent.setAction(sb.name);
                context.startActivity(intent);
            }
        }

        @Override
        protected ItemViewHolder bindItemViewHolder(View v) {
            return new ItemViewHolder(v);
        }

        class ViewHolder extends ParentViewHolder {
            TextView nameView;

            public ViewHolder(View view) {
                super(view);
                nameView = (TextView) view.findViewById(R.id.class_name);
            }

            @Override
            protected LinearLayout getContentView(View view) {
                return (LinearLayout) view.findViewById(R.id.class_content);
            }
        }
    }
}

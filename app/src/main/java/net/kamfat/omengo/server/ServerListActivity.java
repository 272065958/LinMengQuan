package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.ServerBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.Tools;

import java.util.ArrayList;

/**
 * Created by cjx on 2017/3/2.
 * 所有服务类
 */
public class ServerListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        setToolBar(true, null, getIntent().getAction());
        loadData();
    }

    private void loadData() {
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                findViewById(R.id.loading_view).setVisibility(View.GONE);
                ArrayList<ServerBean> serverBeans = JsonParser.getInstance().fromJson(response.datum, new TypeToken<ArrayList<ServerBean>>() {
                }.getType());
                displayData(serverBeans);
            }

            @Override
            public void error() {
                findViewById(R.id.loading_view).setVisibility(View.GONE);
            }
        };
        Intent intent = getIntent();
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "app/newSecondIndex", "type", intent.getStringExtra("type"),
                "key", intent.getStringExtra("key"));
    }

    private void displayData(ArrayList<ServerBean> serverBeans) {
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ServerAdapter(serverBeans, this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServerBean sb = (ServerBean) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ServerListActivity.this, ServerSelectActivity.class);
                intent.putExtra("key", sb.key);
                intent.putExtra("type", sb.type);
                intent.setAction(sb.name);
                startActivity(intent);
            }
        });
        gridView.setVisibility(View.VISIBLE);
    }

    class ServerAdapter extends MyBaseAdapter {

        public ServerAdapter(ArrayList<ServerBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_server, null);
        }

        @Override
        protected MyViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, MyViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            ServerBean sb = (ServerBean) getItem(position);
            ho.nameView.setText(sb.name);
            Tools.setImage(context, ho.iconView, sb.image);
        }

        class ViewHolder extends MyViewHolder {
            ImageView iconView;
            TextView nameView;
            public ViewHolder(View v) {
                super(v);
                iconView = (ImageView) v.findViewById(R.id.server_icon);
                nameView = (TextView) v.findViewById(R.id.server_name);
            }
        }
    }
}

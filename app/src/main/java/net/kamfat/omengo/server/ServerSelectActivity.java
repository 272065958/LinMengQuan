package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.BaseListActivity;
import net.kamfat.omengo.bean.ImageBean;
import net.kamfat.omengo.bean.ServerChildBean;
import net.kamfat.omengo.bean.ServerTypeBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by cjx on 2016/12/21.
 * 服务的二级页面
 */
public class ServerSelectActivity extends BaseListActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_select);

        setToolBar(true, null, getIntent().getAction());
        initView(null);
        setListViweDivider(ContextCompat.getDrawable(this, R.color.background_bg),
                getResources().getDimensionPixelOffset(R.dimen.auto_space));
        loadData();
        registerReceiver(new IntentFilter(OmengoApplication.ACTION_ORDER_CREATE));
    }

    // 收到广播回调
    @Override
    protected void onBroadcastReceive(Intent intent){
        finish();
    }

    @Override
    protected MyBaseAdapter getMyBaseAdapter(ArrayList<?> list) {
        return new ServerAdapter(list, this);
    }

    @Override
    protected void loadData() {
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                hideLoadView();
                try {
                    JSONArray array = new JSONArray(response.datum);
                    JsonParser parser = JsonParser.getInstance();
                    ImageBean ib = parser.fromJson(array.getString(0), ImageBean.class);
                    showImage(ib);
                    Type type = new TypeToken<ArrayList<ServerTypeBean>>() {
                    }.getType();
                    ArrayList<ServerTypeBean> list = parser.fromJson(array.getString(1), type);
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
        Intent intent = getIntent();
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "app/thirdIcon", "key", intent.getStringExtra("key"),
                "type", intent.getStringExtra("type"));
    }

    private void showImage(ImageBean ib) {
        ImageView adverImage = (ImageView) findViewById(R.id.image_view);
        int screenWidth = ((OmengoApplication) getApplication()).getScreen_width();
        int height = (int) (screenWidth * 22 / 72f);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) adverImage.getLayoutParams();
        lp.height = height;
        Tools.setImage(this, adverImage, ib.image);
    }

    class ServerAdapter extends MyBaseAdapter implements View.OnClickListener {
        LinearLayout.LayoutParams leftParams;
        LinearLayout.LayoutParams rightParams;
        LinearLayout.LayoutParams llp;
        LinearLayout.LayoutParams lineParams;

        public ServerAdapter(ArrayList<?> list, BaseActivity context) {
            super(list, context);
            OmengoApplication app = (OmengoApplication) context.getApplication();
            int viewSpace = getResources().getDimensionPixelOffset(R.dimen.view_space);
            int width = (app.getScreen_width() - viewSpace) / 2;
            llp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            leftParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewSpace);
            rightParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            rightParams.leftMargin = viewSpace;
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_server_select_content, null);
        }

        @Override
        protected MyViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, MyViewHolder holder) {
            ServerTypeBean si = (ServerTypeBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            initContentView(ho.itemContentView, si.list);
            ho.titleView.setText(si.name);
        }

        class ViewHolder extends MyViewHolder {
            TextView titleView;
            LinearLayout itemContentView;

            public ViewHolder(View v) {
                super(v);
                titleView = (TextView) v.findViewById(R.id.title_view);
                itemContentView = (LinearLayout) v.findViewById(R.id.linear_layout);
            }
        }

        @Override
        public void onClick(View v) {
            ServerChildBean scb = (ServerChildBean) v.getTag();
            Intent intent = new Intent(ServerSelectActivity.this, ServerDetailActivity.class);
            intent.setAction(scb.name);
            intent.putExtra("key", scb.key);
            intent.putExtra("type", scb.type);
            startActivity(intent);
        }

        Stack<View> itemStack = new Stack<>();
        Stack<View> lineStack = new Stack<>();
        Stack<LinearLayout> contentStack = new Stack<>();

        private void initContentView(LinearLayout contentView, ArrayList<ServerChildBean> list) {
            int childCount = contentView.getChildCount();
            for (int i = childCount - 1; i > -1; i--) {
                View view = contentView.getChildAt(i);
                contentView.removeView(view);
                if(view instanceof LinearLayout){
                    if (view.isSelected()) {
                        LinearLayout linearLayout = (LinearLayout) view;
                        for (int j = 1; j > -1; j--) {
                            View itemView = linearLayout.getChildAt(j);
                            itemStack.add(itemView);
                            linearLayout.removeView(itemView);
                        }
                        contentStack.add(linearLayout);
                    } else {
                        itemStack.add(view);
                    }
                }else{
                    lineStack.add(view);
                }
            }
            if (list == null || list.isEmpty()) {
                return;
            }
            int size = list.size();
            int line = (int) Math.ceil(size / 2.0f);
            for (int i = 0; i < line; i++) {
                if(i > 0){
                    contentView.addView(getLineView(), lineParams);
                }
                int p = i * 2;
                View v1 = getItemView(list.get(p));
                if (p == size - 1) {
                    contentView.addView(v1, llp);
                } else {
                    View v2 = getItemView(list.get(p + 1));
                    LinearLayout linearLayout = getContentView();
                    linearLayout.addView(v1, leftParams);
                    linearLayout.addView(v2, rightParams);
                    contentView.addView(linearLayout);
                }
            }
        }

        private LinearLayout getContentView() {
            if (contentStack.isEmpty()) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setBackgroundResource(R.color.divider_color);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setSelected(true);
                return linearLayout;
            } else {
                return contentStack.pop();
            }
        }

        private View getItemView(ServerChildBean scb) {
            View v;
            ItemViewHolder holder;
            if (itemStack.isEmpty()) {
                v = View.inflate(context, R.layout.item_server_select, null);
                holder = new ItemViewHolder();
                holder.imageView = (ImageView) v.findViewById(R.id.image_view);
                holder.titleView = (TextView) v.findViewById(R.id.title_view);
                holder.descView = (TextView) v.findViewById(R.id.desc_view);
                v.setTag(R.id.tag_viewholder, holder);
                v.setOnClickListener(this);
            } else {
                v = itemStack.pop();
                holder = (ItemViewHolder) v.getTag(R.id.tag_viewholder);
            }
            Tools.setImage(context, holder.imageView, scb.image);
            holder.titleView.setText(scb.name);
            holder.descView.setText(scb.desc);
            v.setTag(scb);
            return v;
        }

        private View getLineView(){
            if (lineStack.isEmpty()) {
                return View.inflate(context, R.layout.item_divider, null);
            } else {
                return lineStack.pop();
            }
        }

        class ItemViewHolder {
            ImageView imageView;
            TextView titleView, descView;
        }
    }

}

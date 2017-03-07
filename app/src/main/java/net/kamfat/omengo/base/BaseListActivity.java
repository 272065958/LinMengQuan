package net.kamfat.omengo.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.MyCallbackInterface;
import net.kamfat.omengo.util.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by cjx on 2016/10/28.
 * 列表界面
 */
public abstract class BaseListActivity extends BaseActivity {
    protected View loadView;
    protected ListView listView;
    AdapterView.OnItemClickListener itemClickListener;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_LOGIN:
                    loadData();
                    break;
            }
        }
    }

    protected void initView(AdapterView.OnItemClickListener itemClickListener) {
        loadView = findViewById(R.id.loading_view);
        listView = (ListView) findViewById(R.id.list_view);
        this.itemClickListener = itemClickListener;
    }

    // 设置listView的分割线
    protected void setListViweDivider(Drawable divider, int dividerHeight) {
        if (listView != null) {
            listView.setDivider(divider);
            listView.setDividerHeight(dividerHeight);
        }
    }

    // 隐藏加载控件
    protected void hideLoadView() {
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.GONE);
        }
    }

    // 加载数据完成后调用
    protected void onLoadResult(ArrayList<?> list){
        hideLoadView();
        displayData(list);
    }

    // 获取一个默认的加载数据回调
    protected MyCallbackInterface getMyCallbackInterface(Type type){
        return new BaseCallInterface(type);
    }

    class BaseCallInterface implements MyCallbackInterface {
        Type type;
        public BaseCallInterface(Type type){
            this.type = type;
        }

        @Override
        public void success(DatumResponse response) {
            ArrayList<?> list = JsonParser.getInstance().fromJson(response.datum, type);
            onLoadResult(list);
        }

        @Override
        public void error() {
            hideLoadView();
        }
    }

    MyBaseAdapter adapter;

    // 显示数据
    protected void displayData(ArrayList<?> list) {
        if (adapter == null) {
            adapter = getMyBaseAdapter(list);
            listView.setAdapter(adapter);
            if(itemClickListener != null){
                listView.setOnItemClickListener(itemClickListener);
            }
        } else {
            adapter.notifyDataSetChanged(list);
        }
        if (adapter.getCount() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
        }
    }

    protected abstract MyBaseAdapter getMyBaseAdapter(ArrayList<?> list);

    // 加载数据
    protected abstract void loadData();
}

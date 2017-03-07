package net.kamfat.omengo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.base.TreeSelectActivity;
import net.kamfat.omengo.bean.TreeBean;
import net.kamfat.omengo.component.tablayout.TabLayout;
import net.kamfat.omengo.http.HttpUtils;

import java.util.ArrayList;


/**
 * Created by cjx on 2016/12/26.
 */
public class PositionSelectActivity extends TreeSelectActivity {
    MenuItem completeMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadChildTree(currentIntent.getStringExtra("tree_id"));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if (tab.getPosition() == 0 && completeMenu != null) {
            completeMenu.setVisible(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TreeBean tb = (TreeBean) adapter.getItem(position);
        if (tb.isChild()) {
            returnPosition(tb);
        } else {
            TabLayout.Tab tab = tabLayout.newTab().setText(tb.project_name);
            tabLayout.addTab(tab, true);
            if (completeMenu != null && !completeMenu.isVisible()) {
                completeMenu.setVisible(true);
            }
            loadChildTree(tb._id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }

    private StringBuilder getNavitionPath() {
        StringBuilder pChild = new StringBuilder();
        int count = tabLayout.getTabCount();
        for (int i = 1; i < count; i++) {
            TabLayout.Tab t = tabLayout.getTabAt(i);
            pChild.append(t.getText());
        }
        return pChild;
    }

    // 选中一个item返回
    private void returnPosition(TreeBean tb) {
        StringBuilder pChild = getNavitionPath();
        pChild.append(tb.project_name);
        returnResult(tb._id, pChild.toString());
    }

    // 返回数据
    private void returnResult(String id, String path) {
        Intent data = new Intent(id);
        data.putExtra("child", path);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void loadList(String id) {
        HttpUtils.getInstance().postEnqueue(this, getMyCallbackInterface(id, new TypeToken<ArrayList<TreeBean>>() {
        }.getType()), "receiver/selAddress", "parentId", id);
    }
}
package net.kamfat.omengo.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.activity.LoginActivity;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.BaseListActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.AddressBean;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.dialog.TipDialog;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;

import java.util.ArrayList;

/**
 * Created by cjx on 2016/12/22.
 * 选择收货地址
 */

public class AddressSelectActivity extends BaseListActivity implements AdapterView.OnItemClickListener {

    OmengoApplication app;
    AddressAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setToolBar(true, null, R.string.server_address);
        app = (OmengoApplication) getApplication();
        if(getIntent().getAction() != null){
            initView(null);
        }else{
            initView(this);
        }
        setListViweDivider(ContextCompat.getDrawable(this, R.color.background_bg),
                getResources().getDimensionPixelOffset(R.dimen.auto_space));
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    protected MyBaseAdapter getMyBaseAdapter(ArrayList<?> list) {
        if(adapter == null){
            adapter = new AddressAdapter(list, this);
        }
        return adapter;
    }

    @Override
    protected void loadData() {
        if (!app.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        HttpUtils.getInstance().postEnqueue(this, getMyCallbackInterface(new TypeToken<ArrayList<AddressBean>>() {
        }.getType()), "receiver/index", "userid", app.user.id);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AddressBean ab = (AddressBean) adapter.getItem(position);
        Intent data = new Intent();
        data.putExtra("address", ab);
        setResult(RESULT_OK, data);
        finish();
    }

    public void addClick(View view) {
        Intent intent = new Intent(this, AddressAddActivity.class);
        intent.putExtra("title", getString(R.string.receive_address_add));
        startActivityForResult(intent, 1);
    }

    class AddressAdapter extends MyBaseAdapter {
        public AddressAdapter(ArrayList<?> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        protected View createView(Context context) {
            return View.inflate(context, R.layout.item_address, null);
        }

        @Override
        protected MyViewHolder bindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindData(int position, MyViewHolder holder) {
            AddressBean ab = (AddressBean) getItem(position);
            ViewHolder ho = (ViewHolder) holder;
            if (ab.is_default) {
                ho.autoView.setSelected(true);
            } else {
                ho.autoView.setSelected(false);
            }
            ho.nameView.setText(ab.consignee);
            ho.phoneView.setText(ab.phone);
            ho.projectView.setText(ab.address);
            ho.autoView.setTag(ab);
            ho.deleteView.setTag(ab);
            ho.updateView.setTag(ab);
        }

        class ViewHolder extends MyViewHolder implements View.OnClickListener {
            View autoView, deleteView, updateView;
            TextView nameView, phoneView, projectView;

            public ViewHolder(View v) {
                super(v);
                nameView = (TextView) v.findViewById(R.id.property_name);
                phoneView = (TextView) v.findViewById(R.id.property_phone);
                projectView = (TextView) v.findViewById(R.id.property_location);
                autoView = v.findViewById(R.id.address_auto);
                updateView = v.findViewById(R.id.address_update);
                updateView.setOnClickListener(this);
                deleteView = v.findViewById(R.id.address_delete);
                deleteView.setOnClickListener(this);
                autoView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                AddressBean ab = (AddressBean) v.getTag();
                switch (v.getId()) {
                    case R.id.address_delete:
                        showDeleteDialog(ab);
                        break;
                    case R.id.address_update:
                        updateAddress(ab);
                        break;
                    case R.id.address_auto:
                        autoAddress(ab);
                        break;
                }
            }
        }
    }

    // 删除收货地址
    private void deleteAddress(final AddressBean ab) {
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                showToast(response.message);
                loadData();
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "receiver/delete", "receiverid", ab.id);
    }

    TipDialog deleteDialog;

    // 显示删除收货地址提示对话框
    private void showDeleteDialog(Object obj) {
        if (deleteDialog == null) {
            deleteDialog = new TipDialog(this);
            deleteDialog.setText(R.string.tip_title, R.string.exit_tip, R.string.button_sure, R.string.button_cancel)
                    .setTipComfirmListener(new TipDialog.ComfirmListener() {
                        @Override
                        public void comfirm() {
                            deleteDialog.dismiss();
                            deleteAddress((AddressBean) deleteDialog.getTag());
                        }

                        @Override
                        public void cancel() {
                            deleteDialog.dismiss();
                        }
                    });
        }
        deleteDialog.setTag(obj);
        deleteDialog.show();
    }

    private void updateAddress(AddressBean ab) {
        Intent intent = new Intent(this, AddressAddActivity.class);
        intent.putExtra("title", getString(R.string.button_update));
        intent.setAction(ab.id);
        intent.putExtra("name", ab.consignee);
        intent.putExtra("phone", ab.phone);
        intent.putExtra("address", ab.address);
        startActivityForResult(intent, 1);
    }

    private void autoAddress(final AddressBean ab) {
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                showToast(response.message);
                for(Object obj : adapter.list){
                    AddressBean addressBean = (AddressBean) obj;
                    if(addressBean == ab){
                        addressBean.is_default = true;
                    }else{
                        addressBean.is_default = false;

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "receiver/setDefault", "userid",
                app.user.id, "receiverid", ab.id);
    }
}

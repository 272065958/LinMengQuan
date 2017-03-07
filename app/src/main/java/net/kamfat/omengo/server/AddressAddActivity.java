package net.kamfat.omengo.server;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.activity.LoginActivity;
import net.kamfat.omengo.activity.PositionSelectActivity;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;

/**
 * Created by cjx on 2016/12/22.
 * 新增地址
 */

public class AddressAddActivity extends BaseActivity {
    private final int RESULT_POSITION = 2;
    EditText nameView, phoneView;
    TextView projectView;
    OmengoApplication app;
    String action;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        Intent intent = getIntent();
        setToolBar(true, null, intent.getStringExtra("title"));
        app = (OmengoApplication)getApplication();
        findViewById();
        action = intent.getAction();
        if(action != null){
            nameView.setText(intent.getStringExtra("name"));
            phoneView.setText(intent.getStringExtra("phone"));
            projectView.setText(intent.getStringExtra("address"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case RESULT_POSITION:
                String pId = data.getAction();
                projectView.setTag(pId);
                projectView.setText(data.getStringExtra("child"));
                break;
        }
    }

    private void findViewById(){
        nameView = (EditText) findViewById(R.id.name_view);
        phoneView = (EditText) findViewById(R.id.phone_view);
        projectView = (TextView) findViewById(R.id.project_view);
    }

    public void projectSelect(View v){
        // 选择地址
        Intent positionIntent = new Intent(this, PositionSelectActivity.class);
        positionIntent.putExtra("tab_name", getString(R.string.receive_address_position));
        positionIntent.putExtra("title", R.string.receive_address_title);
        startActivityForResult(positionIntent, RESULT_POSITION);
    }

    public void submitClick(View v){
        // 保存地址
        String pId = (String) projectView.getTag();
        if(action == null){
            if(TextUtils.isEmpty(pId)){
                showToast(getString(R.string.receive_address_project_hint));
                return ;
            }
        }
        String name = nameView.getText().toString();
        if(TextUtils.isEmpty(name)){
            showToast(getString(R.string.receive_address_name_hint));
            return ;
        }
        String phone = phoneView.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showToast(getString(R.string.register_phone_hint));
            return ;
        }
        if(!app.isLogin()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return ;
        }
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                showToast(response.message);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface,
                action == null ? "receiver/save" : "receiver/update", "userid", app.user.id,
                "addressId", pId, "consignee", name, "mobile", phone, "address", TextUtils.isEmpty(pId) ? null : projectView.getText().toString(),
                "receiverid", action);
    }

}

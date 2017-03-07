package net.kamfat.omengo.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseGetCodeActivity;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.component.GetCodeView;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;

/**
 * Created by cjx on 2016/12/19.
 * 注册界面
 */

public class RegisterActivity extends BaseGetCodeActivity {
    EditText nameView, passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setToolBar(true, null, R.string.button_register);
        type = "register";
        findViewById();
        registerReceiver(new IntentFilter(OmengoApplication.ACTION_LOGIN));
    }

    // 收到广播回调
    protected void onBroadcastReceive(Intent intent){
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register: // 注册
                register();
                break;
            case R.id.button_get_code: // 获取验证码
                getCode();
                break;
        }
    }

    // 获取界面元素
    private void findViewById() {
        nameView = (EditText) findViewById(R.id.register_name);
        phoneView = (EditText) findViewById(R.id.register_phone);
        codeView = (EditText) findViewById(R.id.register_code);
        passwordView = (EditText) findViewById(R.id.register_password);
        getCodeView = (GetCodeView) findViewById(R.id.button_get_code);
    }

    private void register() {
        // 判断验证码
        if(!checkCode()){
            return ;
        }
        String name = nameView.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getString(R.string.register_name_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        final String phone = phoneView.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.register_phone_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.register_password_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                OmengoApplication app = (OmengoApplication) getApplication();
                app.setUser(response.datum);
                if(app.user != null){
                    Toast.makeText(RegisterActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    sendBroadcast(new Intent(OmengoApplication.ACTION_LOGIN));
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", phone);
                    editor.putString("password", password);
                    editor.apply();
                    finish();
                    setResult(RESULT_OK);
                }else{
                    Toast.makeText(RegisterActivity.this, "获取登录信息失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "member/register", "username", name,
                "mobile", phone, "password", password);
    }

}

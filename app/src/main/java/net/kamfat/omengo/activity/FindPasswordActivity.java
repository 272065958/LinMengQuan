package net.kamfat.omengo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseGetCodeActivity;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.component.GetCodeView;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;

/**
 * Created by cjx on 2017/1/16.
 */
public class FindPasswordActivity extends BaseGetCodeActivity {
    EditText passwordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        setToolBar(true, null, getIntent().getAction());
        type = "reset";
        findViewById();
    }

    private void findViewById(){
        phoneView = (EditText) findViewById(R.id.register_phone);
        codeView = (EditText) findViewById(R.id.register_code);
        passwordView = (EditText) findViewById(R.id.register_password);
        getCodeView = (GetCodeView) findViewById(R.id.button_get_code);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register: // 修改
                submit();
                break;
            case R.id.button_get_code: // 获取验证码
                getCode();
                break;
        }
    }

    private void submit() {
        // 判断验证码
        if(!checkCode()){
            return ;
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
                Toast.makeText(FindPasswordActivity.this, response.message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "member/submit_resetpassword",
                "mobile", phone, "newpassword", password);
    }
}

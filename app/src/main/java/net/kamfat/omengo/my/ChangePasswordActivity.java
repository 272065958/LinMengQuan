package net.kamfat.omengo.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;

/**
 * Created by cjx on 2017/1/11.
 */
public class ChangePasswordActivity extends BaseActivity {
    EditText oldPwdView, passwordView, comfirmView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setToolBar(true, null, R.string.myself_change_pwd);
        findViewById();
    }

    // 获取界面元素
    private void findViewById() {
        oldPwdView = (EditText) findViewById(R.id.change_password_old);
        passwordView = (EditText) findViewById(R.id.change_password_new);
        comfirmView = (EditText) findViewById(R.id.register_password_comfirm);
    }

    public void onClick(View v){
        String oldPwd = oldPwdView.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            Toast.makeText(this, getString(R.string.change_password_old_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.change_password_new_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        String comfirm = comfirmView.getText().toString();
        if(!password.equals(comfirm)){
            Toast.makeText(this, getString(R.string.register_password_comfirm_error), Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadDislog();
        MyCallbackInterface callbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();

            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, callbackInterface, "member/register", "username", oldPwd,
                "password", password);
    }
}

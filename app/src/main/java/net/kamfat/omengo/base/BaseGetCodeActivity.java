package net.kamfat.omengo.base;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import net.kamfat.omengo.R;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.component.GetCodeView;
import net.kamfat.omengo.http.HttpUtils;
import net.kamfat.omengo.http.MyCallbackInterface;

/**
 * Created by cjx on 2016/12/12.
 */
public class BaseGetCodeActivity extends BaseActivity {
    protected EditText phoneView, codeView;
    protected GetCodeView getCodeView;
    protected String type;

    @Override
    protected void onDestroy() {
        getCodeView.stopTimer();
        super.onDestroy();
    }

    // 获取验证码
    protected void getCode() {
        String phone = phoneView.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.register_phone_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadDislog();
        MyCallbackInterface myCallbackInterface = new MyCallbackInterface() {
            @Override
            public void success(DatumResponse response) {
                dismissLoadDialog();
                codeView.setTag(response.datum);
                getCodeView.startTimer();
            }

            @Override
            public void error() {
                dismissLoadDialog();
            }
        };
        HttpUtils.getInstance().postEnqueue(this, myCallbackInterface, "member/sendSmsApi", "mobile",
                phone, "op", type);
    }

    // 检查验证码是否正确
    protected boolean checkCode(){
        String trueCode = (String) codeView.getTag();
        if(TextUtils.isEmpty(trueCode)){
            Toast.makeText(this, getString(R.string.register_true_code_null_hint), Toast.LENGTH_SHORT).show();
            return false;
        }
        String code = codeView.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, getString(R.string.register_code_null_hint), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!code.equals(trueCode)) {
            Toast.makeText(this, getString(R.string.register_code_error_hint), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

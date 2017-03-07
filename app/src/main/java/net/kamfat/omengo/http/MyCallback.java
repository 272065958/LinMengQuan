package net.kamfat.omengo.http;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.activity.LoginActivity;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.api.Code;
import net.kamfat.omengo.bean.api.DatumResponse;
import net.kamfat.omengo.util.JsonParser;
import net.kamfat.omengo.util.Tools;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cjx on 2016/7/18.
 */
public class MyCallback implements Callback {

    BaseActivity activity;
    MyCallbackInterface callbackInterface;
    Request request;
    public MyCallback(BaseActivity activity, MyCallbackInterface callbackInterface, Request request){
        this.activity = activity;
        this.callbackInterface = callbackInterface;
        this.request = request;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        if(activity == null){
            return ;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callbackInterface.error();
                if(e instanceof SocketTimeoutException){
                    Toast.makeText(activity, activity.getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, activity.getString(R.string.http_steam_exception), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String body = response.body().string();
        final DatumResponse r = JsonParser.getInstance().getDatumResponse(body);
        if(r == null){
            // 保存异常信息到文件
            Tools.saveToFile(activity, "http_response", body);
        }
        if(activity == null){
            return ;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(r == null){
                    callbackInterface.error();
                    Toast.makeText(activity, activity.getString(R.string.http_exception), Toast.LENGTH_SHORT).show();
                    return ;
                }
                Log.e("TAG", body);
                switch (r.code){
                    case Code.SUCCESS:
                        callbackInterface.success(r);
                        break;
                    case Code.TOKEN_INVALID:
                        ((OmengoApplication)activity.getApplication()).setUser(null);
                        HttpUtils.getInstance().clearCookie();
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                                activity.getString(R.string.app_name), Activity.MODE_PRIVATE);
                        String oldAcc = sharedPreferences.getString("username", null);

                        if(!TextUtils.isEmpty(oldAcc)){
                            String oldPwd = sharedPreferences.getString("password", null);
                            autoLogin(oldAcc, oldPwd, request);
                        }else{
                            callbackInterface.error();
                            Toast.makeText(activity, r.message, Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(activity, LoginActivity.class);
                            activity.startActivityForResult(loginIntent, activity.RESULT_LOGIN);
                        }
                        break;
                    default:
                        callbackInterface.error();
                        Toast.makeText(activity, r.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void autoLogin(String acc, String pwd, Request request){
        CustomCallback myCallback = new CustomCallback(this, request);
        HttpUtils.getInstance().postEnqueue(myCallback, "member/login", "username", acc, "password", pwd);
    }

    class CustomCallback implements Callback{
        Callback callback;
        Request request;
        public CustomCallback(Callback callback, Request request){
            this.callback = callback;
            this.request = request;
        }
        @Override
        public void onFailure(Call call, IOException e) {
            callback.onFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(activity == null){
                return ;
            }
            DatumResponse r = JsonParser.getInstance().getDatumResponse(response.body().string());
            if(r != null && r.code == Code.SUCCESS){
                HttpUtils.getInstance().enqueue(callback, request);
                OmengoApplication app = (OmengoApplication) activity.getApplication();
                app.setUser(r.datum);
                activity.sendBroadcast(new Intent(OmengoApplication.ACTION_LOGIN)); //发送登录成功广播
            }else{
                Intent loginIntent = new Intent(activity, LoginActivity.class);
                activity.startActivityForResult(loginIntent, activity.RESULT_LOGIN);
            }
        }
    }
}

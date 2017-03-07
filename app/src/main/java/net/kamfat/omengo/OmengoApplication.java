package net.kamfat.omengo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.baidu.mobstat.StatService;

import net.kamfat.omengo.bean.UserBean;
import net.kamfat.omengo.util.JsonParser;

/**
 * Created by cjx on 2016/8/20.
 */
public class OmengoApplication extends Application {
    static OmengoApplication instance;
    public final static String ACTION_LOGIN = "action_login_omengo";
    public final static String ACTION_WEIXIN_PAY = "action_weixin_pay_omengo";
    public final static String ACTION_ORDER_CREATE = "action_order_create_omengo";
    public final static String ACTION_CART_BUY = "action_cart_buy_omengo";
    public final static String ACTION_INFO_UPDATE = "action_info_update_omengo";

    public final static String WEIXIN_APPID = "wxfe31593ab3199bdf";

    private int SCREEN_WIDTH = 0, SCREEN_HEIGHT = 0;
    public UserBean user = null;
    public boolean isInLoginView = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SharedPreferences sp = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String body = sp.getString("userInfo", null);
        if (body != null) {
            login(body);
        }
    }

    public static OmengoApplication getInstance() {
        return instance;
    }

    // 获取上次登录信息
    private void login(String body) {
        user = JsonParser.getInstance().fromJson(body, UserBean.class);
    }

    // 判断是否已经登录
    public boolean isLogin() {
        return user != null;
    }

    // 保存用户信息
    public void setUser(String info) {
        SharedPreferences sp = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (info == null) {
            editor.remove("userInfo");
            user = null;
        } else {
            editor.putString("userInfo", info);
            login(info);
        }
        editor.apply();
    }

    /**
     * 获取屏幕尺寸
     */
    private void measureScreen() {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        SCREEN_WIDTH = displayMetrics.widthPixels;
    }

    /**
     * 获取当前手机宽度
     */
    public int getScreen_width() {
        if (SCREEN_WIDTH <= 0) {
            measureScreen();
        }
        return SCREEN_WIDTH;
    }

    /**
     * 获取当前手机高度
     */
    public int getScreen_height() {
        if (SCREEN_HEIGHT <= 0) {
            measureScreen();
        }
        return SCREEN_HEIGHT;
    }
}

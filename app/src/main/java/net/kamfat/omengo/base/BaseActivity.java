package net.kamfat.omengo.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import net.kamfat.omengo.R;
import net.kamfat.omengo.dialog.LoadDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cjx on 2016/6/1.
 */
public class BaseActivity extends AppCompatActivity {
    public final int RESULT_LOGIN = 201;
    TextView toolbarTitle;
    Toolbar toolbar;
    public LoadDialog loadDialog;

    BroadcastReceiver refreshReceiver;

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置toolbar
     *
     * @param hasBack      是否包含返回按钮
     * @param backListener 返回按钮监听
     * @param titleRes     标题的资源id
     */
    public void setToolBar(boolean hasBack, View.OnClickListener backListener, int titleRes) {
        setToolbar(hasBack, backListener);
        if (toolbarTitle != null) {
            if (titleRes > 0) {
                toolbarTitle.setText(titleRes);
            }
        }
    }

    public Toolbar getToolBar(){
        return toolbar;
    }

    /**
     * 设置toolbar
     *
     * @param hasBack      是否包含返回按钮
     * @param backListener 返回按钮监听
     * @param title        标题的资源id
     */
    public void setToolBar(boolean hasBack, View.OnClickListener backListener, String title) {
        setToolbar(hasBack, backListener);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    private void setToolbar(boolean hasBack, View.OnClickListener backListener) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (hasBack) {
                toolbar.setNavigationIcon(R.drawable.back_icon);
                setSupportActionBar(toolbar);
                if (backListener == null) {
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else {
                    toolbar.setNavigationOnClickListener(backListener);
                }

            } else {
                setSupportActionBar(toolbar);
            }
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        }
    }

    /**
     * 设置toolbar的title
     *
     * @param title 标题
     */
    public void setToolbarTitle(String title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    /**
     * 设置toolbar的title
     *
     * @param titleRes 标题资源
     */
    public void setToolbarTitle(int titleRes) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(titleRes);
        }
    }

    /**
     * 显示加载对话框
     */
    public void showLoadDislog() {
        if (loadDialog == null) {
            loadDialog = new LoadDialog(this);
        }
        loadDialog.show();
    }

    /**
     * 显示加载对话框
     */
    public void showLoadDislog(DialogInterface.OnCancelListener listener) {
        if (loadDialog == null) {
            loadDialog = new LoadDialog(this);
        }
        loadDialog.setOnCancelListener(listener);
        loadDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoadDialog() {
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }


    Timer timer;
    InputMethodManager imm;

    // 显示键盘
    public void showInput() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (imm == null) {
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);//这里的时间大概是自己测试的
    }

    // 注册广播
    protected void registerReceiver(IntentFilter filter) {
        if (refreshReceiver != null) {
            return;
        }
        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceive(intent);
            }
        };
        registerReceiver(refreshReceiver, filter);
    }

    // 收到广播回调
    protected void onBroadcastReceive(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            imm = null;
        }
        if (refreshReceiver != null) {
            unregisterReceiver(refreshReceiver);
        }
        super.onDestroy();
    }
}

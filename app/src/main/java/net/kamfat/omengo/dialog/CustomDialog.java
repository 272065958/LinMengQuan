package net.kamfat.omengo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.kamfat.omengo.R;


/**
 * Created by cjx on 2015/11/30.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context, R.style.loading_dialog);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initStyle();
    }

    public void setContentView(View view){
        super.setContentView(view);
        initStyle();
    }

    private void initStyle(){
        // 设置对话框在底部
        Window mWindow = getWindow();
        mWindow.setWindowAnimations(R.style.my_dialog);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}

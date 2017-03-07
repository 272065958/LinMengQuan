package net.kamfat.omengo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;


/**
 * Created by cjx on 2016/8/3.
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Context context){
        super(context, R.style.loading_dialog);
        if(context instanceof Activity){
            Window dw = getWindow();
            WindowManager.LayoutParams wl = dw.getAttributes();
            float scale = ((OmengoApplication)((Activity)context).getApplication()).getScreen_width() / 720f;
            wl.width = (int) (540 * scale);
            dw.setAttributes(wl);
        }
    }

    Object tag;
    /**
     * 设置一个标签
     * */
    public void setTag(Object tag){
        this.tag = tag;
        return;
    }

    public Object getTag(){
        return tag;
    }
}

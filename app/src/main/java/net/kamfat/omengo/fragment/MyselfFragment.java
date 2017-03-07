package net.kamfat.omengo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kamfat.omengo.OmengoApplication;
import net.kamfat.omengo.R;
import net.kamfat.omengo.activity.FindPasswordActivity;
import net.kamfat.omengo.base.BaseFragment;
import net.kamfat.omengo.activity.FeedbackActivity;
import net.kamfat.omengo.activity.LoginActivity;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.bean.UserBean;
import net.kamfat.omengo.dialog.TipDialog;
import net.kamfat.omengo.my.ChangePasswordActivity;
import net.kamfat.omengo.my.MyCouponActivity;
import net.kamfat.omengo.my.MyOrderActivity;
import net.kamfat.omengo.my.UserInfoActivity;
import net.kamfat.omengo.property.activity.PropertyBalanceActivity;
import net.kamfat.omengo.util.Tools;

/**
 * Created by cjx on 2016/8/22.
 */
public class MyselfFragment extends BaseFragment {
    TextView nameView;
    ImageView headView;
    UserBean currentUser;
    View logoutView, loginView;

    boolean isVisibleToUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_myself, null);
            findViewById();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (view != null && isVisibleToUser) {
            checkLogin();
        }
    }

    private void findViewById() {
        nameView = (TextView) view.findViewById(R.id.user_name);
        headView = (ImageView) view.findViewById(R.id.user_head);
        loginView = view.findViewById(R.id.login_content);
        logoutView = view.findViewById(R.id.myself_logout);
        int width = OmengoApplication.getInstance().getScreen_width();
        int height = (int) (281 * width / 750f);
        loginView.getLayoutParams().height = height;
        if (isVisibleToUser) {
            checkLogin();
        }
    }

    // 根据登录状态显示/隐藏控件
    public void checkLogin() {
        if(getActivity() == null){
            return;
        }
        UserBean user = ((OmengoApplication) getActivity().getApplication()).user;
        if (currentUser == user) {
            if(user == null){
                startLogin();
            }
            return;
        }
        currentUser = user;
        if (user == null) {
            logoutView.setVisibility(View.GONE);
            loginView.setClickable(true);
            nameView.setText(R.string.myself_login);

            headView.setImageResource(R.drawable.user_head);
            startLogin();
        } else {
            if(logoutView.getVisibility() == View.GONE){
                logoutView.setVisibility(View.VISIBLE);
            }
            loginView.setClickable(false);
            nameView.setText(currentUser.username);
            Tools.setHeadImage(getActivity(), headView, currentUser.avatar);
        }
    }

    private void startLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void myselfOnClick(BaseActivity activity, View view) {
        switch (view.getId()) {
            case R.id.user_balance: // 余额
                Intent balanceIntent = new Intent(activity, PropertyBalanceActivity.class);
                startActivity(balanceIntent);
                break;
            case R.id.user_order: // 订单
                Intent orderIntent = new Intent(activity, MyOrderActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.user_coupon: // 优惠券
                Intent couponIntent = new Intent(activity, MyCouponActivity.class);
                startActivity(couponIntent);
                break;
            case R.id.myself_info: // 个人资料
                Intent infoIntent = new Intent(activity, UserInfoActivity.class);
                startActivity(infoIntent);
                break;
            case R.id.myself_change_pwd: // 修改密码
                Intent pwdIntent = new Intent(activity, FindPasswordActivity.class);
                pwdIntent.setAction(getString(R.string.myself_change_pwd));
                startActivity(pwdIntent);
                break;
            case R.id.myself_feedback: // 反馈
                Intent feedbackIntent = new Intent(activity, FeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.myself_server://联系客服
                showCallDialog(activity);
                break;
            case R.id.myself_logout: // 注销
                OmengoApplication.getInstance().setUser(null);
                checkLogin();
                break;
            case R.id.login_content:
                startLogin();
                break;
        }
    }

    TipDialog callDialog;

    private void showCallDialog(BaseActivity activity) {
        if (callDialog == null) {
            callDialog = new TipDialog(activity);
            callDialog.setText(getString(R.string.property_contact_property), "拨打客服电话400-930-3688",
                    getString(R.string.button_sure), getString(R.string.button_cancel)).setTipComfirmListener(
                    new TipDialog.ComfirmListener() {
                        @Override
                        public void comfirm() {
                            callDialog.dismiss();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:400-930-3688"));
                            startActivity(callIntent);
                        }

                        @Override
                        public void cancel() {
                            callDialog.dismiss();
                        }
                    });
        }
        callDialog.show();
    }
}

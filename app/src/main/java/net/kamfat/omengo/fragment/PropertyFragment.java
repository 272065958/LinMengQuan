package net.kamfat.omengo.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseFragment;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.base.MyBaseAdapter;
import net.kamfat.omengo.bean.ItemBean;
import net.kamfat.omengo.dialog.TipDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
public class PropertyFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_property, null);
//            initView(view);
        }
        return view;
    }

//    private void initView(View view) {
//        int screenWidth = ((OmengoApplication) getActivity().getApplication()).getScreen_width();
//        View messageContentView = view.findViewById(R.id.property_message_content);
//        int height = (int) (screenWidth * 73 / 150f);
//        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) messageContentView.getLayoutParams();
//        llp.height = height;
//
//        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
//        List<ItemBean> list = getPropertyList();
//        gridView.setOnItemClickListener(this);
//        gridView.setAdapter(new PropertyAdapter(list, getActivity()));
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ItemBean pb = (ItemBean) parent.getAdapter().getItem(position);
//        if (pb != null && pb.titleRes > 0) {
//            switch (pb.titleRes) {
//                case R.string.property_notication: // 物业公告
//                    Intent noticationIntent = new Intent(getActivity(), PropertyNoticationActivity.class);
//                    startActivity(noticationIntent);
//                    break;
//                case R.string.property_cost_search: // 费用查询
//                    Intent costIntent = new Intent(getActivity(), PropertyCostActivity.class);
//                    startActivity(costIntent);
//                    break;
//                case R.string.property_publish_repair: // 公共报修
//                    Intent repariIntent = new Intent(getActivity(), PropertyRepairActivity.class);
//                    startActivity(repariIntent);
//                    break;
//                case R.string.property_home_authenticate: // 房屋认证
//                    Intent authInten = new Intent(getActivity(), PropertyAuthActivity.class);
//                    startActivity(authInten);
//                    break;
//                case R.string.property_complain_praise: // 投诉表扬
//                    Intent ppInetnt = new Intent(getActivity(), ComplainPraiseActivity.class);
//                    startActivity(ppInetnt);
//                    break;
//                case R.string.property_contact_property: // 联系物管
//                    showCallDialog();
//                    break;
//                case R.string.property_visitor_auth: // 访客授权
//                    Intent visitorIntent = new Intent(getActivity(), PropertyVisitorsActivity.class);
//                    startActivity(visitorIntent);
//                    break;
//            }
//        }
    }

    TipDialog callDialog;

    private void showCallDialog() {
        if (callDialog == null) {
            callDialog = new TipDialog(getActivity());
            callDialog.setText(getString(R.string.property_contact_property), "拨打物管电话400-930-3688",
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

    private List<ItemBean> getPropertyList() {
        List<ItemBean> list = new ArrayList<>();
        ItemBean ib = new ItemBean();
        ib.iconRes = R.drawable.ic_property_notication;
        ib.titleRes = R.string.property_notication;
        list.add(ib);
        ib = new ItemBean();
        ib.iconRes = R.drawable.ic_cost_search;
        ib.titleRes = R.string.property_cost_search;
        list.add(ib);
        ib = new ItemBean();
        ib.iconRes = R.drawable.ic_publish_repair;
        ib.titleRes = R.string.property_publish_repair;
        list.add(ib);
        ib = new ItemBean();
        ib.iconRes = R.drawable.ic_visitor_authenticate;
        ib.titleRes = R.string.property_visitor_auth;
        list.add(ib);
        ib = new ItemBean();
        ib.iconRes = R.drawable.ic_home_authenticate;
        ib.titleRes = R.string.property_home_authenticate;
        list.add(ib);
        ib = new ItemBean();
        ib.iconRes = R.drawable.ic_complain_praise;
        ib.titleRes = R.string.property_complain_praise;
        list.add(ib);
        ib = new ItemBean();
        ib.iconRes = R.drawable.ic_contact_property;
        ib.titleRes = R.string.property_contact_property;
        list.add(ib);

        return list;
    }

    class PropertyAdapter extends MyBaseAdapter {
        public PropertyAdapter(ArrayList<ItemBean> list, BaseActivity context) {
            super(list, context);
        }

        @Override
        public View createView(Context context) {
            return View.inflate(context, R.layout.item_operate, null);
        }

        @Override
        public void bindData(int position, MyViewHolder holder) {
            ViewHolder ho = (ViewHolder) holder;
            ItemBean ib = (ItemBean) getItem(position);
            ho.imageView.setImageResource(ib.iconRes);
            ho.titleView.setText(ib.titleRes);
        }

        @Override
        public MyViewHolder bindViewHolder(View v) {
            ViewHolder ho = new ViewHolder(v);
            ho.imageView = (ImageView) v.findViewById(R.id.operate_icon);
            ho.titleView = (TextView) v.findViewById(R.id.operate_name);
            return ho;
        }

        class ViewHolder extends MyViewHolder {
            TextView titleView;
            ImageView imageView;

            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}

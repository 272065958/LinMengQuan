package net.kamfat.omengo.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.kamfat.omengo.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/21.
 */
public abstract class MyBaseAdapter extends BaseAdapter {

    public ArrayList<?> list;
    public int count;
    public BaseActivity context;

    public MyBaseAdapter(ArrayList<?> list, BaseActivity context) {
        this.list = list;
        count = this.list == null ? 0 : this.list.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = createView(context);
            holder = bindViewHolder(convertView);
            convertView.setTag(R.id.tag_viewholder, holder);
        } else {
            holder = (MyViewHolder) convertView.getTag(R.id.tag_viewholder);
        }
        bindData(position, holder);
        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<?> list) {
        this.list = list;
        count = this.list == null ? 0 : this.list.size();
        notifyDataSetChanged();
    }

    abstract protected View createView(Context context);

    abstract protected MyViewHolder bindViewHolder(View view);

    abstract protected void bindData(int position, MyViewHolder holder);

    public class MyViewHolder {
        View view;

        public MyViewHolder(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }

}
/**
 * demo
 * class XAdapter extends MyBaseAdapter{
 * public XAdapter(ArrayList<?> list, BaseActivity context){
 * super(list, context);
 * }
 *
 * @Override protected View createView(Context context) {
 * return null;
 * }
 * @Override protected MyViewHolder bindViewHolder(View view) {
 * return null;
 * }
 * @Override protected void bindData(int position, MyViewHolder holder) {
 * <p>
 * }
 * <p>
 * class ViewHolder extends MyViewHolder{
 * public ViewHolder(View v){
 * super(v);
 * }
 * }
 * }
 **/
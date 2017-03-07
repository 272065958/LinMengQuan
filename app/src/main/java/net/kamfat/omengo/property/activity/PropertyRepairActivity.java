package net.kamfat.omengo.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.activity.BitmapDealActivity;
import net.kamfat.omengo.activity.CameraActivity;
import net.kamfat.omengo.util.Tools;

/**
 * Created by cjx on 2016/9/9.
 */
public class PropertyRepairActivity extends BaseActivity {
    private final int RESULT_TAKE = 101, RESULT_DEAL = 102;
    String takePath;
    LinearLayout imageContentView;
    View currentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_repair);

        setToolBar(true, null, R.string.property_publish_repair);

        initView();

        TextView bottomBtn = (TextView)findViewById(R.id.bottom_button);
        bottomBtn.setText(R.string.button_sure);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case RESULT_TAKE:
                Intent dealIntent = new Intent(this, BitmapDealActivity.class);
                dealIntent.putExtra("photo", takePath);
                startActivityForResult(dealIntent, RESULT_DEAL);
                break;
            case RESULT_DEAL:
                addImage(data.getAction());
                scrollToEnd();
                break;
        }
    }

    private void initView(){
        imageContentView = (LinearLayout) findViewById(R.id.photo_content);
        addImageSelectView();
    }

    public void onClick(View view){
        // 提交报修信息
        Toast.makeText(this, getString(R.string.only_demo), Toast.LENGTH_SHORT).show();
    }

    // 添加一个选择图片的view
    private void addImageSelectView() {
        currentImageView = View.inflate(this, R.layout.select_image_view, null);
        currentImageView.setOnClickListener(addImageListener);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        llp.leftMargin = getResources().getDimensionPixelOffset(R.dimen.auto_space);
        currentImageView.setLayoutParams(llp);
        imageContentView.addView(currentImageView);
    }

    // 添加一张图片到view
    private void addImage(String path) {
        ImageView imageView = (ImageView) currentImageView.findViewById(R.id.image_content);
        imageView.setOnClickListener(null);
        Glide.with(this).load(path).into(imageView);
        ImageView delView = (ImageView) currentImageView.findViewById(R.id.delete_image);
        delView.setImageResource(R.drawable.delete_image_icon);
        delView.setOnClickListener(delImageListener);
        addImageSelectView();
    }

    // 滚动图片容器到最右边
    private void scrollToEnd() {
        ((HorizontalScrollView) imageContentView.getParent()).fullScroll(ScrollView.FOCUS_RIGHT);
    }

    View.OnClickListener addImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PropertyRepairActivity.this, CameraActivity.class);
            if (takePath == null) {
                takePath = Tools.getTempPath(PropertyRepairActivity.this) + System.currentTimeMillis() + "tempphoto.jpeg";
            }
            intent.setAction(takePath);
            startActivityForResult(intent, RESULT_TAKE);
        }
    };

    View.OnClickListener delImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageContentView.removeView((View) v.getParent());
        }
    };
}

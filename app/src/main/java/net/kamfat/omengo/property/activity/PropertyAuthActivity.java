package net.kamfat.omengo.property.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;
import net.kamfat.omengo.util.Tools;

import java.io.File;

/**
 * Created by cjx on 2016/9/9.
 */
public class PropertyAuthActivity extends BaseActivity {
    final int REQUEST_CAMERA_PERMISSION = 2;
    final int REQUEST_IMAGE_CAPTURE = 101;

    TextView prevIdentityView;
    View ownerContent, noOwnerContent;
    String mCurrentPhotoPath;
    ImageView currentCaptureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_auth);

        setToolBar(true, null, R.string.property_home_authenticate);

        TextView buttonView = (TextView) findViewById(R.id.bottom_button);
        buttonView.setText("申请认证");

        prevIdentityView = (TextView) findViewById(R.id.identity_owner);
        prevIdentityView.setSelected(true);
        prevIdentityView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        noOwnerContent = findViewById(R.id.identity_owner_family_detail);
        ownerContent = findViewById(R.id.identity_owner_detail);
        View imageConten1 = findViewById(R.id.identity_image_1);
        imageConten1.setOnClickListener(imageClick);
        View imageConten2 = findViewById(R.id.identity_image_2);
        imageConten2.setOnClickListener(imageClick);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PropertyAuthActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "无法使用相机功能", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                setImage(mCurrentPhotoPath);
                break;
        }
    }

    public void identityClick(View view){
        if(view != prevIdentityView){
            prevIdentityView.setSelected(false);
            prevIdentityView.setTextColor(ContextCompat.getColor(this, R.color.text_main_color));
            prevIdentityView = (TextView) view;
            prevIdentityView.setSelected(true);
            prevIdentityView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            if(prevIdentityView.getId() == R.id.identity_owner){
                noOwnerContent.setVisibility(View.GONE);
                ownerContent.setVisibility(View.VISIBLE);
            }else{
                noOwnerContent.setVisibility(View.VISIBLE);
                ownerContent.setVisibility(View.GONE);
            }
        }
    }

    public void onClick(View view){
        Toast.makeText(this, getString(R.string.only_demo), Toast.LENGTH_SHORT).show();
    }

    private void setImage(String path){
        Glide.with(this).load(path).into(currentCaptureView);
    }

    public void propertySelect(View view){
        // 选择物业
        Intent intent = new Intent(this, PropertyChangeActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener imageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentCaptureView = (ImageView) v;
            try{
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    if (mCurrentPhotoPath == null) {
                        mCurrentPhotoPath = Tools.getTempPath(PropertyAuthActivity.this) + System.currentTimeMillis() + "tempphoto.jpeg";
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCurrentPhotoPath)));
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(PropertyAuthActivity.this, "no camera find", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };
}

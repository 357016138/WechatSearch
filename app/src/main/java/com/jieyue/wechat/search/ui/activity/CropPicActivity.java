package com.jieyue.wechat.search.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;
import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropPicActivity extends BaseActivity {
    @BindView(R.id.cropView)
    CropView cropView;

    Uri srouceUri;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crop_pic);
    }

    @Override
    public void dealLogicBeforeInitView() {
        srouceUri = getIntent().getData();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        cropView.of(srouceUri)
                .asSquare()
                .withOutputSize(360, 360)
                .initialize(this);
    }

    @Override
    public void dealLogicAfterInitView() {

    }
    @OnClick({R.id.tv_ok,R.id.tv_cancel})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:                  //确定
                Bitmap croppedBitmap = cropView.getOutput();
//                CropUtil.saveOutput(context, saveUri, croppedImage, quality);
                break;
            case R.id.tv_cancel:             //取消

                break;
            default:
                break;
        }
    }



    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

}



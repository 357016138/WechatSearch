package com.jieyue.wechat.search.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.common.Constants;
import com.jieyue.wechat.search.log.JYLogUtils;

public class BizPhotoWallFragment extends BaseFragment {
    private String url = "";
    private Bitmap mBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle _Bundle = getArguments();
        if (_Bundle != null) {
            if (_Bundle.containsKey("url")) {
                url = (String) _Bundle.getString("url");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 将界面inflate到fragment中
        View view = inflater.inflate(R.layout.fragment_image_wall, null);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        ImageView image = (ImageView) view.findViewById(R.id.image);
        if (!TextUtils.isEmpty(url)) {
            if (url.contains(Constants.HTTP)) {
                Glide.with(getActivity()).load(url).into(image);
            } else {
                Glide.with(getActivity()).load(url).into(image);
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            JYLogUtils.d("", "释放Bitmap");
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}